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

import com.transcendensoft.hedbanz.HedbanzApplication;
import com.transcendensoft.hedbanz.data.network.manager.ApiManager;
import com.transcendensoft.hedbanz.di.AppModule;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;
import com.transcendensoft.hedbanz.logging.LoggingModule;

import dagger.Component;

/**
 * Component for Dagger 2 in order to create
 * Application level graph.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ApplicationScope
@Component(modules = {LoggingModule.class, AppModule.class, })
public interface AppComponent {
    void inject(HedbanzApplication hedbanzApplication);
    void inject(ApiManager dataRepository);
}
