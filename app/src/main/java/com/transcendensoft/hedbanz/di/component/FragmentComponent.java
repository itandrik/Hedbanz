package com.transcendensoft.hedbanz.di.component;
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

import com.transcendensoft.hedbanz.di.FragmentModule;
import com.transcendensoft.hedbanz.di.scope.FragmentScope;
import com.transcendensoft.hedbanz.presentation.mainscreen.menu.MenuFragment;
import com.transcendensoft.hedbanz.presentation.mainscreen.roomcreation.CreateRoomFragment;
import com.transcendensoft.hedbanz.presentation.mainscreen.rooms.RoomsFragment;
import com.transcendensoft.hedbanz.presentation.usercrud.login.LoginFragment;

import dagger.Subcomponent;

/**
 * Component for Dagger 2 in order to create
 * Fragment level graph.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Subcomponent(modules = FragmentModule.class)
@FragmentScope
@Deprecated
public interface FragmentComponent {
    @Subcomponent.Builder
    interface Builder {
        FragmentComponent.Builder fragmentModule(FragmentModule fragmentModule);

        FragmentComponent build();
    }

    //void inject(BaseFragment baseFragment);
    void inject(RoomsFragment roomsFragment);

    void inject(CreateRoomFragment createRoomFragment);

    void inject(LoginFragment loginFragment);

    void inject(MenuFragment menuFragment);
}
