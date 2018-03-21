package com.transcendensoft.hedbanz.data.repository;
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
import com.transcendensoft.hedbanz.data.models.mapper.UserModelDataMapper;
import com.transcendensoft.hedbanz.data.network.source.UserApiDataSource;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Interface that represents a Repository (or Gateway)
 * for getting {@link com.transcendensoft.hedbanz.domain.entity.User} related data.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ApplicationScope
public class UserDataRepositoryImpl implements UserDataRepository {
    private UserApiDataSource mUserApiDataSource;
    private UserModelDataMapper mUserModelDataMapper;
    private PreferenceManager mPreferenceManager;

    @Inject
    public UserDataRepositoryImpl(UserApiDataSource mUserApiDataSource,
                                  UserModelDataMapper mUserModelDataMapper,
                                  PreferenceManager preferenceManager) {
        this.mUserApiDataSource = mUserApiDataSource;
        this.mUserModelDataMapper = mUserModelDataMapper;
        this.mPreferenceManager = preferenceManager;
    }

    @Override
    public Observable<User> registerUser(User user) {
        UserDTO userDTO = mUserModelDataMapper.convert(user);
        return mUserApiDataSource.registerUser(userDTO).map(mUserModelDataMapper::convert);
    }

    @Override
    public Observable<User> authUser(User user) {
        UserDTO userDTO = mUserModelDataMapper.convert(user);
        return mUserApiDataSource.authUser(userDTO).map(mUserModelDataMapper::convert);
    }

    @Override
    public Observable<User> updateUser(long id, String newLogin,
                                       String oldPassword, String newPassword,
                                       DataPolicy dataPolicy) {
        if (dataPolicy == DataPolicy.API) {
            return mUserApiDataSource.updateUser(id, newLogin, oldPassword, newPassword)
                    .map(mUserModelDataMapper::convert);
        } else if (dataPolicy == DataPolicy.DB) {
            UserDTO currentUser = mPreferenceManager.getUser();
            currentUser.setId(id);
            currentUser.setLogin(newLogin);
            currentUser.setPassword(newPassword);
            mPreferenceManager.setUser(currentUser);

            return Observable.just(currentUser).map(mUserModelDataMapper::convert);
        }
        return Observable.error(new UnsupportedOperationException());
    }
}
