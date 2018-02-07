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

import com.transcendensoft.hedbanz.model.entity.User;
import com.transcendensoft.hedbanz.presenter.BasePresenter;
import com.transcendensoft.hedbanz.presenter.LoginPresenter;
import com.transcendensoft.hedbanz.view.LoginView;

/**
 * Presenter from MVP pattern, that contains
 * methods to process log in work.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class LoginPresenterImpl extends BasePresenter<User, LoginView> implements LoginPresenter{
    @Override
    protected void updateView() {

    }

    @Override
    public void login() {

    }
}
