package com.transcendensoft.hedbanz.model.api.manager;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.transcendensoft.hedbanz.model.api.service.ApiService;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Manager to get
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public abstract class ApiManager {
    public static final String HOST = "http://77.47.204.201";

    private static final String PORT_API = ":8080/";
    private static final String BASE_URL = HOST + PORT_API;

    public static final String PORT_SOCKET = ":9092";
    public static final String LOGIN_SOCKET_NSP = "/login";
    public static final String GAME_SOCKET_NSP = "/game";

    private Retrofit mClient;
    protected ApiService mService;
    protected final ObservableTransformer mSchedulersTransformer;

    protected ApiManager() {
        mSchedulersTransformer =  o -> o
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        initRetrofit();
        initService();
    }

    /**
     * Initialises Retrofit with a BASE_URL and GSON converter.
     */
    private void initRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.HOURS)
                .readTimeout(1, TimeUnit.HOURS)
                .build();

        mClient = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private void initService() {
        mService = mClient.create(ApiService.class);
    }

    @SuppressWarnings("unchecked")
    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) mSchedulersTransformer;
    }
}
