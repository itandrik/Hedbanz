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
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.User;

import java.util.regex.Pattern;

/**
 * Validation methods for register form.
 * Validation of email, login, password and
 * password confirmation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class UserCrudValidator implements Validator<User> {
    private static final String EMAIL_REGEX = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    private static final String LOGIN_REGEX = "^[a-zA-Z0-9.]{3,10}$";
    private static final String PASSWORD_REGEX = "\\S{4,14}";

    private static final Pattern LOGIN_PATTERN =
            Pattern.compile(LOGIN_REGEX);
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX);
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);

    private User mUser;
    private @StringRes int mErrorMessage;

    public UserCrudValidator(User mUser) {
        this.mUser = mUser;
        if (mUser == null) {
            mUser = new User.Builder().build();
            Crashlytics.log("Error while validation on register. " +
                    "UserDTO entity is null. UserCrudValidator class");
        }
    }

    @Override
    public boolean isValid(User model) {
        return false;
    }

    public boolean isEmailValid() {
        String email = mUser.getEmail();
        if (TextUtils.isEmpty(email.trim())) {
            mErrorMessage = R.string.register_validate_empty_field;
            return false;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            mErrorMessage = R.string.register_validate_email;
            return false;
        }
        return true;
    }

    public boolean isPasswordValid() {
        String password = mUser.getPassword();
        if (TextUtils.isEmpty(password.trim())) {
            mErrorMessage = R.string.register_validate_empty_field;
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            mErrorMessage = R.string.register_validate_password;
            return false;
        }
        return true;
    }

    public boolean isConfirmPasswordValid() {
        String password = mUser.getConfirmPassword();
        if (TextUtils.isEmpty(password.trim())) {
            mErrorMessage = R.string.register_validate_empty_field;
            return false;
        } else if (!password.equals(mUser.getPassword())) {
            mErrorMessage = R.string.register_validate_confirm_password;
            return false;
        }
        return true;
    }

    public boolean isLoginValid() {
        String name = mUser.getLogin();
        if (TextUtils.isEmpty(name.trim())) {
            mErrorMessage = R.string.register_validate_empty_field;
            return false;
        } else if (!LOGIN_PATTERN.matcher(name).matches()) {
            mErrorMessage = R.string.register_validate_login;
            return false;
        }
        return true;
    }

    public boolean isOldPasswordValid(String oldPassword){
        if(TextUtils.isEmpty(oldPassword)){
            mErrorMessage = R.string.credentials_error_confirm_password;
            return false;
        }
        return true;
    }

    @Override
    public @StringRes
    int getErrorMessage() {
        return mErrorMessage;
    }
}