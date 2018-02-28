package com.transcendensoft.hedbanz.presenter.validation;
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
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.model.entity.Room;

import java.util.regex.Pattern;

/**
 * Validation methods for room creation form.
 * Validation of room name and password.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class RoomValidator implements Validator<Room> {
    private static final String PASSWORD_REGEX = "\\S{4,14}";

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);

    private Room mRoom;
    private @StringRes int mErrorMessage;

    public RoomValidator(Room room) {
        this.mRoom = room;
        if (room == null) {
            room = new Room.Builder().build();
            Crashlytics.log("Error while validation on create room. " +
                    "Room entity is null. RoomValidator class");
        }
    }

    public boolean isNameValid(){
        String password = mRoom.getPassword();
        if (TextUtils.isEmpty(password.trim())) {
            mErrorMessage = R.string.room_creation_error_name_empty;
            return false;
        }
        return true;
    }

    public boolean isPasswordValid(){
        String password = mRoom.getPassword();
        if (TextUtils.isEmpty(password.trim())) {
            mErrorMessage = R.string.room_creation_error_password_empty;
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            mErrorMessage = R.string.room_creation_error_password_incorrect;
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid(Room model) {
        return false;
    }

    @Override
    public int getErrorMessage() {
        return 0;
    }
}
