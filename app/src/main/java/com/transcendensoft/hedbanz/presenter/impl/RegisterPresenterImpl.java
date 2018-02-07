package com.transcendensoft.hedbanz.presenter.impl;
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
import com.transcendensoft.hedbanz.model.api.manager.LoginRegisterManager;
import com.transcendensoft.hedbanz.model.entity.ServerResult;
import com.transcendensoft.hedbanz.model.entity.ServerStatus;
import com.transcendensoft.hedbanz.model.entity.User;
import com.transcendensoft.hedbanz.model.entity.error.RegisterError;
import com.transcendensoft.hedbanz.model.entity.error.ServerError;
import com.transcendensoft.hedbanz.presenter.BasePresenter;
import com.transcendensoft.hedbanz.presenter.RegisterPresenter;
import com.transcendensoft.hedbanz.presenter.Socketable;
import com.transcendensoft.hedbanz.presenter.validation.RegisterValidator;
import com.transcendensoft.hedbanz.util.AndroidUtils;
import com.transcendensoft.hedbanz.view.RegisterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

import static com.transcendensoft.hedbanz.model.api.manager.ApiManager.HOST;
import static com.transcendensoft.hedbanz.model.api.manager.ApiManager.PORT_SOCKET;
import static com.transcendensoft.hedbanz.model.api.manager.ApiManager.SOCKET_NSP;

/**
 * Presenter from MVP pattern, that contains
 * methods to process register work with repository
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RegisterPresenterImpl extends BasePresenter<User, RegisterView>
        implements RegisterPresenter, Socketable {
    public static final String TAG = RegisterPresenterImpl.class.getName();
    public static final String LOGIN_RESULT_LISTENER = "loginResult";
    public static final String CHECK_LOGIN_EMIT_KEY = "checkLogin";
    public static final String IS_LOGIN_AVAILABLE = "isLoginAvailable";

    private Socket mSocket;
    private Emitter.Listener mLoginResultSocketListener;

    @Override
    protected void updateView() {

    }

    /*------------------------------------*
     *----------- Registration -----------*
     *------------------------------------*/
    @Override
    public void registerUser(User user) {
        setModel(user);
        if (isUserValid(user)) {
            Disposable disposable = LoginRegisterManager.getInstance()
                    .registerUser(user)
                    .subscribe(
                            this::processRegisterOnNext,
                            this::processRegisterOnError,
                            () -> view().registerSuccess(),
                            this::processRegisterOnSubscribe);
            addDisposable(disposable);
        }
    }

    private boolean isUserValid(User user) {
        RegisterValidator validator = new RegisterValidator(user);
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

    private void processRegisterOnNext(ServerResult<User> result) {
        if (result == null) {
            throw new RuntimeException("Server result is null");
        } else if (!result.getStatus().equalsIgnoreCase(ServerStatus.SUCCESS.toString())) {
            ServerError serverError = result.getServerError();
            if (serverError != null) {
                processErrorFromServer(serverError);
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
        Log.e(TAG, "Error " + err.getMessage());
        Crashlytics.logException(err);
        view().showServerError();
    }

    private void processRegisterOnSubscribe(Disposable d) {
        if (!d.isDisposed() && view().provideContext() != null) {
            if (AndroidUtils.isNetworkConnected(view().provideContext())) {
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
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .subscribe(
                                text -> view().stopSmileAnimation(),
                                err -> {
                                    Log.e(TAG, "Error while setting start/stop smile animation. " +
                                            "Message : " + err.getMessage());
                                },
                                () -> {
                                },
                                d -> view().startSmileAnimation()));
    }

    /*------------------------------------*
     *- Checking for login availability --*
     *------------------------------------*/
    @Override
    public void initSockets() {
        try {
            mSocket = IO.socket(HOST + PORT_SOCKET + SOCKET_NSP);
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
            boolean isLoginAvaliable;
            try {
                isLoginAvaliable = data.getBoolean(IS_LOGIN_AVAILABLE);
            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
                return;
            }
            if (isLoginAvaliable) {
                view().showLoginAvailable();
            } else {
                view().showLoginUnavailable();
            }
        };
    }

    @Override
    public void initNameCheckingListener(EditText editText) {
        RxTextView.textChanges(editText).debounce(400, TimeUnit.MILLISECONDS)
                .filter(this::isCorrectLoginInput)
                .subscribe(text -> {
                    if (mSocket != null && mSocket.connected()) {
                        view().showLoginAvailabilityLoading();
                        mSocket.emit(CHECK_LOGIN_EMIT_KEY, text.toString().trim());
                    }
                });
    }

    private boolean isCorrectLoginInput(CharSequence text) {
        if (text != null && !TextUtils.isEmpty(text)) {
            User user = new User.Builder().setLogin(text.toString()).build();
            RegisterValidator validator = new RegisterValidator(user);
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
