package com.transcendensoft.hedbanz.data.models.common;
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

/**
 * Describes status, that device can receive from server
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public enum ServerStatus {
    SUCCESS("success"), ERROR("error");

    String status;

    ServerStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ServerStatus getServerStatusBasedOnString(String status){
        for (ServerStatus serverStatus: ServerStatus.values()) {
            if(serverStatus.status.equalsIgnoreCase(status)){
                return serverStatus;
            }
        }
        return ERROR;
    }
}
