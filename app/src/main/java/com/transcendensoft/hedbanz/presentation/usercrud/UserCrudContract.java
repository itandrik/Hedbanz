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

import androidx.annotation.StringRes;
import android.widget.EditText;

import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BaseView;

/**
 * View and Presenter interfaces contract for user registration
 * and updating credentials presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface UserCrudContract {
    interface View extends BaseView {
        void showIncorrectLogin(@StringRes int message);

        void showIncorrectEmail(@StringRes int message);

        void showIncorrectPassword(@StringRes int message);

        void showIncorrectConfirmPassword(@StringRes int message);

        void showIncorrectOldPassword(@StringRes int message);

        void startSmileAnimation();

        void stopSmileAnimation();

        void showLoginAvailable();

        void showLoginUnavailable();

        void showLoginAvailabilityLoading();

        void hideLoginAvailability();

        void crudOperationSuccess();
    }

    interface Presenter {
        void registerUser(User user);

        void updateUser(User user, String oldPassword);

        void initAnimEditTextListener(EditText editText);

        void initNameCheckingListener(EditText editText);
    }
}
