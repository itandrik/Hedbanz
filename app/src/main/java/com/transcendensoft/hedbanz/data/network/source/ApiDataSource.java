package com.transcendensoft.hedbanz.data.network.source;
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


import com.transcendensoft.hedbanz.BuildConfig;
import com.transcendensoft.hedbanz.data.network.service.ApiService;

import javax.inject.Inject;

/**
 * Manager to get entity API data source.
 * Contains common string constants for API purposes.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public abstract class ApiDataSource {
    public static final String HOST = BuildConfig.HOST_LINK;
    public static final String BASE_URL = HOST + BuildConfig.PORT_API;

    public static final String PORT_SOCKET = BuildConfig.PORT_SOCKET;
    public static final String LOGIN_SOCKET_NSP = "/login";
    public static final String GAME_SOCKET_NSP = "/game";

    @Inject ApiService mService;

    protected ApiDataSource() {
    }
}
