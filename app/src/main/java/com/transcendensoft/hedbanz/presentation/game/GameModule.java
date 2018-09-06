package com.transcendensoft.hedbanz.presentation.game;
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
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.presentation.game.menu.GameMenuContract;
import com.transcendensoft.hedbanz.presentation.game.menu.GameMenuFragment;
import com.transcendensoft.hedbanz.presentation.game.menu.GameMenuPresenter;
import com.transcendensoft.hedbanz.presentation.game.models.RxRoom;
import com.transcendensoft.hedbanz.presentation.invite.InviteContract;
import com.transcendensoft.hedbanz.presentation.invite.InviteDialogFragment;
import com.transcendensoft.hedbanz.presentation.invite.InvitePresenter;
import com.transcendensoft.hedbanz.presentation.userdetails.UserDetailsContract;
import com.transcendensoft.hedbanz.presentation.userdetails.UserDetailsDialogFragment;
import com.transcendensoft.hedbanz.presentation.userdetails.UserDetailsPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Module that provides fragments, presenter
 * and other instances for game mode screens.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Module
public interface GameModule {
    @ActivityScope
    @Binds GameContract.Presenter bindGamePresenter(GamePresenter gamePresenter);

    @FragmentScope
    @Binds GameMenuContract.Presenter bindGameMenuPresenter(GameMenuPresenter gameMenuPresenter);

    @FragmentScope
    @Binds
    UserDetailsContract.Presenter bindUserDetailsPresenter(UserDetailsPresenter userDetailsPresenter);

    @FragmentScope
    @Binds
    InviteContract.Presenter bindInvitePresenter(InvitePresenter invitePresenter);

    @FragmentScope
    @ContributesAndroidInjector
    GameMenuFragment gameMenuFragment();

    @FragmentScope
    @ContributesAndroidInjector
    UserDetailsDialogFragment userDetailsDialogFragment();

    @FragmentScope
    @ContributesAndroidInjector
    InviteDialogFragment inviteDialogFragment();

    @ActivityContext
    @Binds Context bindActivityContext(GameActivity gameActivity);

    @Provides
    @ActivityScope
    static RxRoom provideRxRoom(){
        return new RxRoom(new Room());
    }

    @Provides
    @ActivityScope
    static Boolean provideIsGameActive(){
        return Boolean.FALSE;
    }
}
