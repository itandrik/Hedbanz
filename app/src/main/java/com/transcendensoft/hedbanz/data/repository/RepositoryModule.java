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

import com.transcendensoft.hedbanz.data.source.DataSourceModule;
import com.transcendensoft.hedbanz.domain.repository.FriendsDataRepository;
import com.transcendensoft.hedbanz.domain.repository.RoomDataRepository;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;

import dagger.Binds;
import dagger.Module;

/**
 * Module that binds repository dependencies
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Module(includes = DataSourceModule.class)
public interface RepositoryModule {
    @Binds
    RoomDataRepository bindRoomDataRepository(RoomDataRepositoryImpl roomDataRepository);

    @Binds
    UserDataRepository bindUserDataRepository(UserDataRepositoryImpl userDataRepository);

    @Binds
    FriendsDataRepository bindFriendsDataRepository(FriendsDataRepositoryImpl friendsDataRepository);
}
