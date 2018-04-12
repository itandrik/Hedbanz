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
    public static final String HOST = "http://35.196.42.112"; //Google
    //public static final String HOST = "http://77.47.204.201";//Raspberry and Laptop

    private static final String PORT_API = ":8080/";// Google
    //private static final String PORT_API = ":8083/";// Laptop
    //private static final String PORT_API = ":8083/";// Raspberry
    //private static final String PORT_API = ":8083/";//OPENSHIFT
    public static final String BASE_URL = HOST + PORT_API;

     public static final String PORT_SOCKET = ":9092"; // Google
    // public static final String PORT_SOCKET = ":9092"; // Laptop
    // public static final String PORT_SOCKET = ":9093"; // Raspberry
    public static final String LOGIN_SOCKET_NSP = "/login";
    public static final String GAME_SOCKET_NSP = "/game";

    @Inject ApiService mService;

    protected ApiDataSource() {
    }
}
