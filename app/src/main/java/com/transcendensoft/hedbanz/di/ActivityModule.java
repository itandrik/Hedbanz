package com.transcendensoft.hedbanz.di;
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
import android.support.v7.app.AppCompatActivity;

import com.transcendensoft.hedbanz.di.qualifier.ActivityContext;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.presentation.game.GameContract;
import com.transcendensoft.hedbanz.presentation.game.GamePresenter;
import com.transcendensoft.hedbanz.presentation.usercrud.UserCrudContract;
import com.transcendensoft.hedbanz.presentation.usercrud.UserCrudPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 module for all activities
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Module
public class ActivityModule {
    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    @ActivityContext
    public Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityScope
    public GameContract.Presenter provideGamePresenter(GamePresenter gamePresenter){
        return gamePresenter;
    }

    @Provides
    @ActivityScope
    public UserCrudContract.Presenter provideUserCrudPresenter(UserCrudPresenter userCrudPresenter){
        return userCrudPresenter;
    }
}
