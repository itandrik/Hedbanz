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

import android.app.Application;
import android.support.annotation.NonNull;

import com.transcendensoft.hedbanz.data.network.ApiServiceModule;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 module for HedbanzApplication
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Module(includes = ApiServiceModule.class)
public class AppModule {
    private Application mApplication;
    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    public Application provideApplication(){
        return mApplication;
    }

    @Provides
    @ApplicationScope
    @NonNull
    public PreferenceManager providePreferenceManger(){
        return new PreferenceManager(mApplication);
    }
}
