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
    NO_SUCH_USER(103, R.string.login_error_no_such_login),
    INCORRECT_PASSWORD(301, R.string.room_creation_error_invalid_password),
    ROOM_IS_FULL(302, R.string.room_is_full),
    CANT_START_GAME(303, R.string.room_cant_start_game),
    GAME_HAS_BEEN_ALREADY_STARTED(304, R.string.room_game_already_started),
    USER_HAS_MAX_ACTIVE_ROOMS_NUMBER(305, R.string.room_user_has_max_active_rooms_number),
    ROOM_ALREADY_EXIST(306, R.string.room_creation_error_name_already_exist),
    SUCH_PLAYER_ALREADY_IN_ROOM(307,  R.string.room_user_already_in_room),
    UNAUTHORIZED_PLAYER(401, R.string.room_unauthorized_player),

    EMPTY_ROOM_ID(10, R.string.error_server),
    EMPTY_NAME(11, R.string.room_creation_error_name_empty),
    INVALID_ROOM_ID(14, R.string.error_server),
    EMPTY_PASSWORD(3, R.string.room_creation_error_password_empty),
    INVALID_PASSWORD(6, R.string.room_creation_error_password_incorrect),
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
