package com.transcendensoft.hedbanz.domain.interactor.rooms.exception;
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

import com.transcendensoft.hedbanz.domain.validation.RoomError;

import java.util.ArrayList;
import java.util.List;

/**
 * Exceptions, that can be thrown from interactor in order to
 * show {@link com.transcendensoft.hedbanz.domain.entity.Room}
 * entity errors on creation.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class RoomCreationException extends RuntimeException {
    private List<RoomError> mRoomErrors;

    public RoomCreationException() {
        mRoomErrors = new ArrayList<>();
    }

    public RoomCreationException(RoomError roomError) {
        mRoomErrors = new ArrayList<>();
        mRoomErrors.add(roomError);
    }

    public void addRoomError(RoomError roomError){
        mRoomErrors.add(roomError);
    }

    public List<RoomError> getErrors() {
        return mRoomErrors;
    }
}
