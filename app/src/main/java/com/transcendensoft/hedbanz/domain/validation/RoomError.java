package com.transcendensoft.hedbanz.domain.validation;
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

import android.support.annotation.StringRes;

import com.transcendensoft.hedbanz.R;

/**
 * All errors that can receive user while create room.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public enum RoomError {
    NO_SUCH_USER(1, R.string.error_server),
    EMPTY_NAME(2, R.string.room_creation_error_name_empty),
    EMPTY_PASSWORD(3, R.string.room_creation_error_password_empty),
    INVALID_PASSWORD(4, R.string.room_creation_error_password_incorrect),
    ROOM_ALREADY_EXIST(17, R.string.room_creation_error_name_already_exist),
    UNDEFINED_ERROR(100, R.string.error_undefined_error);

    private int errorCode;
    private @StringRes int errorMessage;

    RoomError(int errorCode, @StringRes int errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public @StringRes
    int getErrorMessage() {
        return this.errorMessage;
    }

    public static RoomError getRoomErrorByCode(int code) {
        for (RoomError roomError : RoomError.values()) {
            if (roomError.getErrorCode() == code) {
                return roomError;
            }
        }
        return RoomError.UNDEFINED_ERROR;
    }
}
