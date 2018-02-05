package com.transcendensoft.hedbanz.model.api.manager;
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

import com.transcendensoft.hedbanz.model.api.service.ApiService;
import com.transcendensoft.hedbanz.model.entity.ServerResult;
import com.transcendensoft.hedbanz.model.entity.User;

import io.reactivex.Observable;

/**
 * Login and register requests to our server
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class LoginRegisterManager {
    private ApiService mService;

    private static final class Holder {
        static final LoginRegisterManager INSTANCE = new LoginRegisterManager();
    }

    public LoginRegisterManager() {
        mService = ApiManager.getInstance().getService();
    }

    public static LoginRegisterManager getInstance() {
        return LoginRegisterManager.Holder.INSTANCE;
    }

    public Observable<ServerResult<User>> registerUser(User user){
        return mService.registerUser(user);
    }

    public Observable<ServerResult<User>> authUser(User user){
        return mService.authUser(user);
    }
}
