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

import com.transcendensoft.hedbanz.data.network.service.firebase.FirebaseInstanceIdModule;
import com.transcendensoft.hedbanz.data.network.service.firebase.FirebaseMessagingServiceModule;
import com.transcendensoft.hedbanz.data.network.service.firebase.HedbanzFirebaseInstanceIdService;
import com.transcendensoft.hedbanz.data.network.service.firebase.HedbanzFirebaseMessagingService;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.di.scope.FragmentScope;
import com.transcendensoft.hedbanz.di.scope.ServiceScope;
import com.transcendensoft.hedbanz.presentation.StartActivity;
import com.transcendensoft.hedbanz.presentation.StartActivityModule;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.game.GameActivity;
import com.transcendensoft.hedbanz.presentation.game.GameModule;
import com.transcendensoft.hedbanz.presentation.language.LanguageActivity;
import com.transcendensoft.hedbanz.presentation.language.LanguageModule;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivityModule;
import com.transcendensoft.hedbanz.presentation.restorepwd.RestorePasswordActivity;
import com.transcendensoft.hedbanz.presentation.restorepwd.RestorePasswordModule;
import com.transcendensoft.hedbanz.presentation.usercrud.RegisterActivity;
import com.transcendensoft.hedbanz.presentation.usercrud.UserCrudModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Dagger-Android module that binds all needed activities
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
@Module
public interface ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector()
    BaseActivity baseActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = UserCrudModule.class)
    RegisterActivity registerActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = GameModule.class)
    GameActivity gameActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = StartActivityModule.class)
    StartActivity startActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    MainActivity mainActivity();

    @ServiceScope
    @ContributesAndroidInjector(modules = FirebaseInstanceIdModule.class)
    HedbanzFirebaseInstanceIdService hedbanzFirebaseInstanceIdService();

    @ServiceScope
    @ContributesAndroidInjector(modules = FirebaseMessagingServiceModule.class)
    HedbanzFirebaseMessagingService hedbanzFirebaseMessagingService();

    @ActivityScope
    @ContributesAndroidInjector(modules = RestorePasswordModule.class)
    RestorePasswordActivity restorePasswordActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = LanguageModule.class)
    LanguageActivity languageActivity();
}
