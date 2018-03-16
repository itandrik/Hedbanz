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

import com.transcendensoft.hedbanz.di.ActivityModule;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.presentation.StartActivity;
import com.transcendensoft.hedbanz.presentation.base.BaseViewModule;
import com.transcendensoft.hedbanz.presentation.game.GameActivity;
import com.transcendensoft.hedbanz.presentation.usercrud.CredentialsActivity;
import com.transcendensoft.hedbanz.presentation.usercrud.RegisterActivity;

import dagger.Subcomponent;

/**
 * Component for Dagger 2 in order to create
 * Activity level graph.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Subcomponent(modules = {ActivityModule.class, BaseViewModule.class})
@ActivityScope
public interface ActivityComponent {
    @Subcomponent.Builder
    interface Builder {
        ActivityComponent.Builder activityModule(ActivityModule activityModule);

        ActivityComponent.Builder baseViewModule(BaseViewModule baseViewModule);

        ActivityComponent build();
    }

    FragmentComponent.Builder fragmentComponentBuilder();

    void inject(RegisterActivity registerActivity);

    void inject(GameActivity gameActivity);

    void inject(CredentialsActivity credentialsActivity);

    void inject(StartActivity startActivity);
}
