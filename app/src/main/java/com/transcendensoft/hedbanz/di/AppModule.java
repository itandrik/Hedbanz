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
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.transcendensoft.hedbanz.HedbanzApplication;
import com.transcendensoft.hedbanz.data.network.ApiServiceModule;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.qualifier.ApplicationContext;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Dagger 2 module for HedbanzApplication
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Module(includes = {ApiServiceModule.class, ActivityBindingModule.class})
public abstract class AppModule {
    @Provides
    public static Application provideApplication(HedbanzApplication hedbanzApplication) {
        return hedbanzApplication;
    }

    @Binds
    @ApplicationContext
    public abstract Context bindContext(HedbanzApplication hedbanzApplication);

    @Binds
    public abstract DaggerAppCompatActivity bindBaseActivity(BaseActivity baseActivity);

    @Provides
    @ApplicationScope
    @NonNull
    public static PreferenceManager providePreferenceManger(@ApplicationContext Context context) {
        return new PreferenceManager(context);
    }

    @Provides
    @ApplicationScope
    @NonNull
    public static FirebaseAnalytics provideFirebaseAnalytics(@ApplicationContext Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        //Sets whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        firebaseAnalytics.setSessionTimeoutDuration(30000);

        return FirebaseAnalytics.getInstance(context);
    }
}
