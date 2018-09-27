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

import android.content.res.Configuration;
import android.support.v7.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.component.DaggerAppComponent;
import com.transcendensoft.hedbanz.domain.entity.Language;
import com.transcendensoft.hedbanz.utils.logging.CrashReportingTree;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.HasServiceInjector;
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
public class HedbanzApplication extends DaggerApplication implements HasServiceInjector {
    @Inject Timber.DebugTree mDebugTimberTree;
    @Inject CrashReportingTree mReleaseTimberTree;
    @Inject PreferenceManager mPreferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        initThirdParties();
        initLanguage();
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
        EmojiManager.install(new IosEmojiProvider());
    }

    private void initLanguage(){
        setLocale();
    }

    public void setLocale() {
        String lang = mPreferenceManager.getLocale();
        Language language = Language.Companion.getLanguageByCode(this, lang);
        String country = null;
        if(language != null){
            country = getString(language.getCountryCode());
        }

        Locale locale;
        if(country != null) {
            locale = new Locale(lang, country);
        } else {
            locale = new Locale(lang);
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale();
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            Language language = Language.Companion.getLanguageByCode(
                    newBase, new PreferenceManager(newBase).getLocale());

            super.attachBaseContext(HedbanzContextWrapper.wrap(
                    newBase, language));
        }
        else {
            super.attachBaseContext(newBase);
        }
    }*/

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
