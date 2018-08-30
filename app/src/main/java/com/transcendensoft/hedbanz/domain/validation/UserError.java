package com.transcendensoft.hedbanz.domain.validation;
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
 * All errors that can receive user while log in,
 * sign up or change credentials.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public enum UserError {
    NO_SUCH_USER(103, R.string.login_error_no_such_login),      //+
    INCORRECT_PASSWORD(6, R.string.login_error_incorrect_password),          //+
    EMPTY_LOGIN(2, R.string.login_error_empty_field),                        //+
    EMPTY_PASSWORD(3, R.string.login_error_empty_field),                      //+
    EMPTY_EMAIL(4, R.string.login_error_empty_field),                         //+
    SUCH_LOGIN_ALREADY_EXIST(351, R.string.login_error_login_not_available),    //+
    SUCH_EMAIL_ALREADY_USING(352, R.string.login_error_email_not_available),    //+
    INVALID_PASSWORD(1001, R.string.register_validate_password),                     //+
    INVALID_PASSWORD_CONFIRMATION(1002, R.string.register_validate_confirm_password),//+
    INVALID_LOGIN(7, R.string.register_validate_login),  //+
    INVALID_EMAIL(8, R.string.register_validate_email),      //+
    INVALID_OLD_PASSWORD(1005, R.string.credentials_error_confirm_password),         //+
    EMPTY_PASSWORD_CONFIRMATION(1004, R.string.login_error_empty_field),             //+
    EMPTY_OLD_PASSWORD(1003, R.string.login_error_empty_field),                     //+
    CANT_SEND_FRIENDSHIP_REQUEST(353, R.string.friends_cant_send_friendship_request),
    ALREADY_FRIENDS(354, R.string.friends_already_friends),
    NOT_FRIENDS(355, R.string.friends_not_friends),
    UNDEFINED_ERROR(100, R.string.error_undefined_error);

    private int errorCode;
    private @StringRes int errorMessage;

    UserError(int errorCode, @StringRes int errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public @StringRes
    int getErrorMessage() {
        return this.errorMessage;
    }

    public static UserError getUserErrorByCode(int code) {
        for (UserError userError : UserError.values()) {
            if (userError.getErrorCode() == code) {
                return userError;
            }
        }
        return UserError.UNDEFINED_ERROR;
    }
}
