package com.transcendensoft.hedbanz.presentation.mainscreen;
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

import com.transcendensoft.hedbanz.di.scope.FragmentScope;
import com.transcendensoft.hedbanz.di.scope.ViewPagerFragmentScope;
import com.transcendensoft.hedbanz.presentation.mainscreen.roomcreation.CreateRoomContract;
import com.transcendensoft.hedbanz.presentation.mainscreen.roomcreation.CreateRoomFragment;
import com.transcendensoft.hedbanz.presentation.mainscreen.roomcreation.CreateRoomPresenter;
import com.transcendensoft.hedbanz.presentation.mainscreen.rooms.RoomsContract;
import com.transcendensoft.hedbanz.presentation.mainscreen.rooms.RoomsFragment;
import com.transcendensoft.hedbanz.presentation.mainscreen.rooms.RoomsPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Module that provides fragments, presenter
 * and other instances for room creation and rooms list
 * views
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Module
public interface MainFragmentModule {
    @ViewPagerFragmentScope
    @ContributesAndroidInjector
    CreateRoomFragment createRoomFragment();

    @ViewPagerFragmentScope
    @ContributesAndroidInjector
    RoomsFragment roomsFragment();

    @FragmentScope
    @Binds
    CreateRoomContract.Presenter createRoomPresenter(CreateRoomPresenter createRoomPresenter);

    @FragmentScope
    @Binds
    RoomsContract.Presenter roomsPresenter(RoomsPresenter roomsPresenter);
}
