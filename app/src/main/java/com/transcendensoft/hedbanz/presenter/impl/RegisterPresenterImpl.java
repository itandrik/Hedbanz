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

import com.transcendensoft.hedbanz.model.api.manager.LoginRegisterManager;
import com.transcendensoft.hedbanz.model.entity.User;
import com.transcendensoft.hedbanz.presenter.BasePresenter;
import com.transcendensoft.hedbanz.presenter.RegisterPresenter;
import com.transcendensoft.hedbanz.presenter.validation.RegisterValidator;
import com.transcendensoft.hedbanz.view.RegisterView;

import io.reactivex.disposables.Disposable;

/**
 * Presenter from MVP pattern, that contains
 * methods to process register work with repository
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RegisterPresenterImpl extends BasePresenter<User, RegisterView>
        implements RegisterPresenter {
    @Override
    protected void updateView() {

    }

    @Override
    public void registerUser(User user) {
        RegisterValidator validator = new RegisterValidator(user);

        //Disposable disposable = LoginRegisterManager.getInstance()
        //        .registerUser(user);
    }
}
