package com.transcendensoft.hedbanz.data.network;
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

import com.transcendensoft.hedbanz.BuildConfig;
import com.transcendensoft.hedbanz.data.network.retrofit.AuthorizationHeaderInterceptor;
import com.transcendensoft.hedbanz.data.network.retrofit.ConnectivityInterceptor;
import com.transcendensoft.hedbanz.di.qualifier.ApplicationContext;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Module
public class NetworkModule {
    private static final int CACHE_SIZE = 10 * 1000 * 1000; //10MB cache

    @Provides
    @ApplicationScope
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Timber.i(message));
        if(BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return loggingInterceptor;
    }

    @Provides
    @ApplicationScope
    public ConnectivityInterceptor provideConnectivityInterceptor(@ApplicationContext Context context){
        return new ConnectivityInterceptor(context);
    }

    @Provides
    @ApplicationScope
    public File provideCacheFile(@ApplicationContext Context context) {
        File cacheFile = new File(context.getCacheDir(), "okhttp_cache");
        cacheFile.mkdirs();
        return cacheFile;
    }

    @Provides
    @ApplicationScope
    public Cache provideCache(File cacheFile) {
        return new Cache(cacheFile, CACHE_SIZE);
    }

    @Provides
    @ApplicationScope
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor,
                                            AuthorizationHeaderInterceptor headerInterceptor,
                                            ConnectivityInterceptor connectivityInterceptor,
                                            Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .addInterceptor(connectivityInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.HOURS)
                .readTimeout(1, TimeUnit.HOURS)
                .cache(cache)
                .build();
    }
}
