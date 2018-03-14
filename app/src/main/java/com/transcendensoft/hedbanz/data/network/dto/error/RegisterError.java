package com.transcendensoft.hedbanz.data.network.dto.error;
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
 * All errors that can receive user while register.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public enum RegisterError {
    SUCH_LOGIN_ALREADY_EXIST(5, R.string.login_error_login_not_available),
    SUCH_EMAIL_ALREADY_USING(6, R.string.login_error_email_not_available),
    EMPTY_LOGIN(7, R.string.login_error_empty_field),
    EMPTY_PASSWORD(8, R.string.login_error_empty_field),
    EMPTY_EMAIL(9, R.string.login_error_empty_field);

    private int errorCode;
    private @StringRes int errorMessage;

    RegisterError(int errorCode, @StringRes int errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public @StringRes int getErrorMessage() {
        return this.errorMessage;
    }
}
