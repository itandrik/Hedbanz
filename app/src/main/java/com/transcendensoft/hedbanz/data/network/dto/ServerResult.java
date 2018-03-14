package com.transcendensoft.hedbanz.data.network.dto;
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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.transcendensoft.hedbanz.data.network.dto.error.ServerError;

/**
 * Generic class, that describes fields which device
 * can receive from server.
 * There are 3 basic fields:
 * 1) Status - {@link ServerStatus} can be success or error
 * 2) Error message - message to output or null if success
 * 3) Data - result object from server
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class ServerResult<T> {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private ServerError serverError;
    @SerializedName("data")
    @Expose
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ServerError getServerError() {
        return serverError;
    }

    public void setServerError(ServerError serverError) {
        this.serverError = serverError;
    }

    public String getStatus() {
        return status;
    }
}
