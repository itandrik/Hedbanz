package com.transcendensoft.hedbanz.presentation.roomcreation;
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

import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.presentation.rooms.models.RoomList;
import com.transcendensoft.hedbanz.presentation.base.BaseView;

/**
 * View and Presenter interfaces contract for room creation.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface CreateRoomContract {

    interface View extends BaseView {
        void showIncorrectRoomName(@StringRes int errorMessage);

        void showIncorrectRoomPassword(@StringRes int errorMessage);

        void createRoomSuccess(Room room);

        void createRoomError();

        void showMaxActiveRoomsError();

        void setPresenterModel(RoomList model);
    }

    interface Presenter {
        void createRoom(Room room);
    }
}
