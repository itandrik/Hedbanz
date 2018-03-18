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
import com.transcendensoft.hedbanz.presentation.mainscreen.menu.MenuFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

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
    @ContributesAndroidInjector(modules = MainFragmentModule.class)
    MainFragment mainFragment();

    @FragmentScope
    @ContributesAndroidInjector
    MenuFragment menuFragment();
}
