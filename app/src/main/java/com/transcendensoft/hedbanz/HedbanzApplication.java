package com.transcendensoft.hedbanz;
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

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.transcendensoft.hedbanz.di.component.DaggerAppComponent;
import com.transcendensoft.hedbanz.utils.logging.CrashReportingTree;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Base application class with initialization of
 * Crashlytics, i18n and other staff.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class HedbanzApplication extends DaggerApplication{
    @Inject Timber.DebugTree mDebugTimberTree;
    @Inject CrashReportingTree mReleaseTimberTree;

    @Override
    public void onCreate() {
        super.onCreate();

        initThirdParties();
    }

    private void initThirdParties() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Fabric.with(this, new Crashlytics());
        if (BuildConfig.DEBUG) {
           // AndroidDevMetrics.initWith(this);
            Timber.plant(mDebugTimberTree);
        } else {
           Timber.plant(mReleaseTimberTree);
        }
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
