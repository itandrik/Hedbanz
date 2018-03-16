package com.transcendensoft.hedbanz.data.network.manager;
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

import com.transcendensoft.hedbanz.data.network.service.ApiService;
import com.transcendensoft.hedbanz.di.AppModule;
import com.transcendensoft.hedbanz.di.component.DaggerAppComponent;

import javax.inject.Inject;

import io.reactivex.ObservableTransformer;

/**
 * Manager to get
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class ApiManager {
    public static final String HOST = "http://77.47.204.201";

    private static final String PORT_API = ":8080/";
    public static final String BASE_URL = HOST + PORT_API;

    public static final String PORT_SOCKET = ":9092";
    public static final String LOGIN_SOCKET_NSP = "/login";
    public static final String GAME_SOCKET_NSP = "/game";

    @Inject ApiService mService;
    @Inject ObservableTransformer mSchedulersTransformer;

    protected ApiManager(Application application) {
        DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build()
                .inject(this);
    }

    @SuppressWarnings("unchecked")
    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) mSchedulersTransformer;
    }
}
