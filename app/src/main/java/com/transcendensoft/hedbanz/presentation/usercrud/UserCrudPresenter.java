package com.transcendensoft.hedbanz.presentation.usercrud;
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

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.transcendensoft.hedbanz.data.entity.ServerResult;
import com.transcendensoft.hedbanz.data.entity.ServerStatus;
import com.transcendensoft.hedbanz.data.entity.User;
import com.transcendensoft.hedbanz.data.entity.error.RegisterError;
import com.transcendensoft.hedbanz.data.entity.error.ServerError;
import com.transcendensoft.hedbanz.data.network.manager.UserCrudApiManager;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.base.Socketable;
import com.transcendensoft.hedbanz.validation.UserCrudValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static com.transcendensoft.hedbanz.data.network.manager.ApiManager.HOST;
import static com.transcendensoft.hedbanz.data.network.manager.ApiManager.LOGIN_SOCKET_NSP;
import static com.transcendensoft.hedbanz.data.network.manager.ApiManager.PORT_SOCKET;

/**
 * Presenter from MVP pattern, that contains
 * methods to process register work with repository
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ActivityScope
public class UserCrudPresenter extends BasePresenter<User, UserCrudContract.View>
        implements UserCrudContract.Presenter, Socketable {
    private static final String TAG = UserCrudPresenter.class.getName();
    private static final String LOGIN_RESULT_LISTENER = "loginResult";
    private static final String CHECK_LOGIN_EMIT_KEY = "checkLogin";
    private static final String IS_LOGIN_AVAILABLE = "isLoginAvailable";

    private Socket mSocket;
    private Emitter.Listener mLoginResultSocketListener;

    private UserCrudApiManager mApiManager;
    private PreferenceManager mPreferenceManager;

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    @Inject
    public UserCrudPresenter(UserCrudApiManager mApiManager, PreferenceManager mPreferenceManager) {
        this.mApiManager = mApiManager;
        this.mPreferenceManager = mPreferenceManager;
    }

    @Override
    protected void updateView() {

    }

    /*------------------------------------*
     *--------- Crud operations ----------*
     *------------------------------------*/
    @Override
    public void registerUser(User user) {
        setModel(user);
        if (isUserValid(user)) {
            Disposable disposable = mApiManager
                    .registerUser(user)
                    .subscribe(
                            this::processRegisterOnNext,
                            this::processRegisterOnError,
                            () -> view().crudOperationSuccess(),
                            this::processRegisterOnSubscribe);
            addDisposable(disposable);
        }
    }

    @Override
    public void updateUser(User user, String oldPassword) {
        setModel(user);
        if (isUserValid(user) && isOldPasswordValid(oldPassword)) {
            Disposable disposable = mApiManager
                    .updateUser(user.getId(), user.getLogin(), oldPassword, user.getPassword())
                    .subscribe(
                            this::processRegisterOnNext,
                            this::processRegisterOnError,
                            () -> view().crudOperationSuccess(),
                            this::processRegisterOnSubscribe);
            addDisposable(disposable);
        }
    }

    private boolean isUserValid(User user) {
        UserCrudValidator validator = new UserCrudValidator(user);
        boolean result = true;
        if (!validator.isLoginValid()) {
            view().showIncorrectLogin(validator.getErrorMessage());
            result = false;
        }
        if (!validator.isEmailValid()) {
            view().showIncorrectEmail(validator.getErrorMessage());
            result = false;
        }
        if (!validator.isPasswordValid()) {
            view().showIncorrectPassword(validator.getErrorMessage());
            result = false;
        }
        if (!validator.isConfirmPasswordValid()) {
            view().showIncorrectConfirmPassword(validator.getErrorMessage());
            result = false;
        }
        return result;
    }

    private boolean isOldPasswordValid(String oldPassword){
        UserCrudValidator validator = new UserCrudValidator(new User.Builder().build());
        if(!validator.isOldPasswordValid(oldPassword)){
            view().showIncorrectOldPassword(validator.getErrorMessage());
            return false;
        } else {
            return true;
        }
    }

    private void processRegisterOnNext(ServerResult<User> result) {
        if (result == null) {
            throw new RuntimeException("Server result is null");
        } else if (!result.getStatus().equalsIgnoreCase(ServerStatus.SUCCESS.toString())) {
            ServerError serverError = result.getServerError();
            if (serverError != null) {
                processErrorFromServer(serverError);
            }
            throw new IllegalStateException();
        } else {
            if(result.getData() != null) {
                mPreferenceManager.setUser(result.getData());
            } else {
                throw new RuntimeException("User comes NULL from server while login");
            }
        }
    }

    private void processErrorFromServer(ServerError serverError) {
        for (RegisterError registerError : RegisterError.values()) {
            if (registerError.getErrorCode() == serverError.getErrorCode()) {
                switch (registerError) {
                    case SUCH_EMAIL_ALREADY_USING:
                    case EMPTY_EMAIL:
                        view().showIncorrectEmail(registerError.getErrorMessage());
                        break;
                    case SUCH_LOGIN_ALREADY_EXIST:
                        view().showLoginUnavailable();
                        break;
                    case EMPTY_LOGIN:
                        view().showIncorrectLogin(registerError.getErrorMessage());
                        break;
                    case EMPTY_PASSWORD:
                        view().showIncorrectPassword(registerError.getErrorMessage());
                        break;
                    default:
                        view().showServerError();
                        break;
                }
            }
        }
    }

    private void processRegisterOnError(Throwable err) {
        if(!(err instanceof IllegalStateException)) {
            Log.e(TAG, "Error " + err.getMessage());
            Crashlytics.logException(err);
            view().showServerError();
        }
    }

    private void processRegisterOnSubscribe(Disposable d) {
        if (!d.isDisposed()) {
            if (view().isNetworkConnected()) {
                view().showLoading();
            } else {
                view().showNetworkError();
            }
        }
    }

    /*------------------------------------*
     *------- Animation for smile --------*
     *------------------------------------*/
    @Override
    public void initAnimEditTextListener(EditText editText) {
        addDisposable(
                RxTextView.textChanges(editText)
                        .skip(1)
                        .doOnEach(text -> {
                            view().startSmileAnimation();
                        })
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .subscribe(
                                text -> {
                                    view().stopSmileAnimation();
                                },
                                err -> {
                                    Log.e(TAG, "Error while setting start/stop smile animation. " +
                                            "Message : " + err.getMessage());
                                }));
    }

    /*------------------------------------*
     *- Checking for login availability --*
     *------------------------------------*/
    @Override
    public void initSockets() {
        try {
            mSocket = IO.socket(HOST + PORT_SOCKET + LOGIN_SOCKET_NSP);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        initSocketListeners();
        mSocket.on(LOGIN_RESULT_LISTENER, mLoginResultSocketListener);
        mSocket.connect();
    }

    private void initSocketListeners() {
        mLoginResultSocketListener = args -> {
            JSONObject data = (JSONObject) args[0];
            boolean isLoginAvailable;
            try {
                isLoginAvailable = data.getBoolean(IS_LOGIN_AVAILABLE);
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }
            if (isLoginAvailable) {
                view().showLoginAvailable();
            } else {
                view().showLoginUnavailable();
            }
        };
    }

    @Override
    public void initNameCheckingListener(EditText editText) {
        addDisposable(RxTextView.textChanges(editText).debounce(400, TimeUnit.MILLISECONDS)
                .filter(this::isCorrectLoginInput)
                .subscribe(text -> {
                    if (mSocket != null && mSocket.connected()) {
                        view().showLoginAvailabilityLoading();
                        mSocket.emit(CHECK_LOGIN_EMIT_KEY, text.toString().trim());
                    }
                }));
    }

    private boolean isCorrectLoginInput(CharSequence text) {
        if (text != null && !TextUtils.isEmpty(text)) {
            User user = new User.Builder().setLogin(text.toString()).build();
            UserCrudValidator validator = new UserCrudValidator(user);
            if (!validator.isLoginValid()) {
                view().showIncorrectLogin(validator.getErrorMessage());
                return false;
            }
            return true;
        } else {
            view().hideLoginAvailability();
            return false;
        }
    }

    @Override
    public void disconnectSockets() {
        mSocket.disconnect();
        mSocket.off(LOGIN_RESULT_LISTENER, mLoginResultSocketListener);
    }
}
