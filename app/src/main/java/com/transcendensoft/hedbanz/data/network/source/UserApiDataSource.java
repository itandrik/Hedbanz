package com.transcendensoft.hedbanz.data.network.source;
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

import com.transcendensoft.hedbanz.data.models.UserDTO;
import com.transcendensoft.hedbanz.data.source.UserDataSource;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Login and register requests to our server
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ApplicationScope
public class UserApiDataSource extends ApiDataSource implements UserDataSource{
    @Inject
    UserApiDataSource() {
        super();
    }

    @Override
    public Observable<UserDTO> registerUser(UserDTO user) {
        return mService.registerUser(user)
                .compose(applySchedulers());
    }

    @Override
    public Observable<UserDTO> authUser(UserDTO user) {
        return mService.authUser(user)
                .compose(applySchedulers());
    }

    @Override
    public Observable<UserDTO> updateUser(long id, String newLogin,
                                                        String oldPassword, String newPassword){
        HashMap<String, Object> result = new HashMap<>();
        result.put("newLogin", newLogin);
        result.put("id", id);
        result.put("oldPassword", oldPassword);
        result.put("newPassword", newPassword);

        return mService.updateUser(result)
                .compose(applySchedulers());
    }
}
