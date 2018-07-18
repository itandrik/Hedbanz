package com.transcendensoft.hedbanz.data.repository;
/**
 * Copyright 2017. Andrii Chernysh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.transcendensoft.hedbanz.data.models.UserDTO;
import com.transcendensoft.hedbanz.data.models.mapper.UserModelDataMapper;
import com.transcendensoft.hedbanz.data.network.retrofit.AuthorizationHeaderInterceptor;
import com.transcendensoft.hedbanz.data.network.source.UserApiDataSource;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;

import org.json.JSONObject;

import java.net.URISyntaxException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import timber.log.Timber;

import static com.transcendensoft.hedbanz.data.network.source.ApiDataSource.HOST;
import static com.transcendensoft.hedbanz.data.network.source.ApiDataSource.LOGIN_SOCKET_NSP;
import static com.transcendensoft.hedbanz.data.network.source.ApiDataSource.PORT_SOCKET;

/**
 * Interface that represents a Repository (or Gateway)
 * for getting {@link com.transcendensoft.hedbanz.domain.entity.User} related data.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ApplicationScope
public class UserDataRepositoryImpl implements UserDataRepository {
    private static final String LOGIN_RESULT_LISTENER = "loginResult";
    private static final String CHECK_LOGIN_EMIT_KEY = "checkLogin";

    private UserApiDataSource mUserApiDataSource;
    private UserModelDataMapper mUserModelDataMapper;
    private PreferenceManager mPreferenceManager;
    private AuthorizationHeaderInterceptor mAuthorizationHeaderInterceptor;
    private Socket mSocket;

    @Inject
    public UserDataRepositoryImpl(UserApiDataSource mUserApiDataSource,
                                  UserModelDataMapper mUserModelDataMapper,
                                  PreferenceManager preferenceManager,
                                  AuthorizationHeaderInterceptor authorizationHeaderInterceptor) {
        this.mUserApiDataSource = mUserApiDataSource;
        this.mUserModelDataMapper = mUserModelDataMapper;
        this.mPreferenceManager = preferenceManager;
        this.mAuthorizationHeaderInterceptor = authorizationHeaderInterceptor;
    }

    @Override
    public Observable<User> registerUser(User user) {
        UserDTO userDTO = mUserModelDataMapper.convert(user);
        return mUserApiDataSource.registerUser(userDTO)
                .doOnNext(dto -> {
                    mPreferenceManager.setAuthorizationToken(dto.getSecurityToken());
                    mAuthorizationHeaderInterceptor.setSessionToken(dto.getSecurityToken());
                })
                .map(mUserModelDataMapper::convert);
    }

    @Override
    public Observable<User> authUser(User user) {
        UserDTO userDTO = mUserModelDataMapper.convert(user);
        return mUserApiDataSource.authUser(userDTO)
                .doOnNext(dto -> {
                    mPreferenceManager.setAuthorizationToken(dto.getSecurityToken());
                    mAuthorizationHeaderInterceptor.setSessionToken(dto.getSecurityToken());
                })
                .map(mUserModelDataMapper::convert);
    }

    @Override
    public Observable<User> updateUser(long id, String newLogin,
                                       String oldPassword, String newPassword,
                                       DataPolicy dataPolicy) {
        if (dataPolicy == DataPolicy.API) {
            return mUserApiDataSource.updateUser(id, newLogin, oldPassword, newPassword)
                    .map(mUserModelDataMapper::convert);
        } else if (dataPolicy == DataPolicy.DB) {
            User currentUser = mPreferenceManager.getUser();
            currentUser.setId(id);
            currentUser.setLogin(newLogin);
            currentUser.setPassword(newPassword);
            mPreferenceManager.setUser(currentUser);

            return Observable.just(currentUser);
        }
        return Observable.error(new UnsupportedOperationException());
    }

    @Override
    public void connectIsLoginAvailable() {
        try {
            //IO.Options opts = new IO.Options();
           // opts.transports = new String[]{ WebSocket.NAME};

            //opts.forceNew = true;

            mSocket = IO.socket(HOST + PORT_SOCKET + LOGIN_SOCKET_NSP);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Observable<JSONObject> isLoginAvailableObservable() {
        return Observable.create(source -> {
            Emitter.Listener loginResultSocketListener = args -> {
                JSONObject data = (JSONObject) args[0];
                source.onNext(data);
            };
            if (mSocket == null) {
                connectIsLoginAvailable();
            }
            mSocket.on(LOGIN_RESULT_LISTENER, loginResultSocketListener);

            mSocket.on(Socket.EVENT_CONNECT, args -> {
                Timber.e("Socket connected!");
            });
            mSocket.on(Socket.EVENT_DISCONNECT, args -> {
                Timber.e("Socket disconnected!");
            });
            mSocket.on(Socket.EVENT_ERROR, args -> {
                Timber.e("Socket error!");
            });

            mSocket.on(Socket.EVENT_CONNECT_ERROR, args ->{
                Timber.e("Socket connect error!");
            });

            mSocket.connect();
        });
    }

    @Override
    public void checkIsLoginAvailable(String login) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit(CHECK_LOGIN_EMIT_KEY, login);
        }
    }

    @Override
    public void disconnectIsLoginAvailable() {
        if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
            mSocket.off(LOGIN_RESULT_LISTENER);
        }
    }
}
