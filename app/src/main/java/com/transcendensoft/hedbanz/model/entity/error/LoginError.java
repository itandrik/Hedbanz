package com.transcendensoft.hedbanz.model.entity.error;
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

import android.support.annotation.StringRes;

import com.transcendensoft.hedbanz.R;

/**
 * All errors that can receive user while log in.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public enum LoginError {
    NO_SUCH_USER(1, R.string.login_error_no_such_login),
    INCORRECT_PASSWORD(2,R.string.login_error_incorrect_password),
    EMPTY_LOGIN(3, R.string.login_error_empty_field),
    EMPTY_PASSWORD(4, R.string.login_error_empty_field);

    private int errorCode;
    private @StringRes int errorMessage;
    LoginError(int errorCode, @StringRes int errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode(){
        return this.errorCode;
    }

    public @StringRes int getErrorMessage(){
        return this.errorMessage;
    }
}
