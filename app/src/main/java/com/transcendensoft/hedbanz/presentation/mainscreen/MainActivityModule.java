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

import android.content.Context;

import com.transcendensoft.hedbanz.di.qualifier.ActivityContext;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.di.scope.FragmentScope;
import com.transcendensoft.hedbanz.presentation.changeicon.ChangeIconModule;
import com.transcendensoft.hedbanz.presentation.feedback.FeedbackModule;
import com.transcendensoft.hedbanz.presentation.friends.FriendsModule;
import com.transcendensoft.hedbanz.presentation.language.LanguageFragment;
import com.transcendensoft.hedbanz.presentation.language.LanguageModule;
import com.transcendensoft.hedbanz.presentation.menu.MenuFragmentModule;
import com.transcendensoft.hedbanz.presentation.roomcreation.CreateRoomContract;
import com.transcendensoft.hedbanz.presentation.roomcreation.CreateRoomFragment;
import com.transcendensoft.hedbanz.presentation.roomcreation.CreateRoomPresenter;
import com.transcendensoft.hedbanz.presentation.rooms.RoomsContract;
import com.transcendensoft.hedbanz.presentation.rooms.RoomsFragment;
import com.transcendensoft.hedbanz.presentation.rooms.RoomsPresenter;
import com.transcendensoft.hedbanz.presentation.rooms.list.RoomItemContract;
import com.transcendensoft.hedbanz.presentation.rooms.list.RoomItemPresenterImpl;
import com.transcendensoft.hedbanz.presentation.rooms.models.RoomList;
import com.transcendensoft.hedbanz.presentation.menu.MenuFragment;
import com.transcendensoft.hedbanz.presentation.usercrud.UserCrudModule;

import java.util.ArrayList;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Module that provides fragments, presenters
 * and other instances for menu and rooms presentation.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Module
public interface MainActivityModule {
    @FragmentScope
    @ContributesAndroidInjector
    CreateRoomFragment createRoomFragment();

    @FragmentScope
    @ContributesAndroidInjector
    RoomsFragment roomsFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = MenuFragmentModule.class)
    MenuFragment menuFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = UserCrudModule.class)
    CredentialsFragment credentialsFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = FriendsModule.class)
    FriendsFragment friendsFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = ChangeIconModule.class)
    ChangeIconFragment changeIconFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = FeedbackModule.class)
    FeedbackFragment feedbackFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = LanguageModule.class)
    LanguageFragment languageFragment();

    @ActivityContext
    @Binds
    Context bindActivityContext(MainActivity mainActivity);

    @Binds
    @ActivityScope
    DaggerAppCompatActivity bindMainActivity(MainActivity mainActivity);

    @Binds
    CreateRoomContract.Presenter createRoomPresenter(CreateRoomPresenter createRoomPresenter);

    @Binds
    RoomsContract.Presenter roomsPresenter(RoomsPresenter roomsPresenter);

    @Binds
    RoomItemContract.Presenter roomItemPresenter(RoomItemPresenterImpl roomItemPresenter);

    @Provides
    @ActivityScope
    static RoomList provideModel(){
        return new RoomList(new ArrayList<>());
    }
}
