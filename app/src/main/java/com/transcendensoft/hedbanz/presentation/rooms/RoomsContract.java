package com.transcendensoft.hedbanz.presentation.rooms;
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

import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.RoomFilter;
import com.transcendensoft.hedbanz.presentation.rooms.models.RoomList;
import com.transcendensoft.hedbanz.presentation.base.BaseView;

import java.util.List;

/**
 * View and Presenter interfaces contract for rooms presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */

public class RoomsContract {

    public interface View extends BaseView {
        void addRoomsToRecycler(List<Room> rooms);

        void clearRooms();

        void removeLastRoom();

        void showEmptyList();

        void setPresenterModel(RoomList model);

        void closeSearchAndRefresh();

        void stopRefreshingBar();

        void showUnauthorizedError();

        void forceLogout();
    }

    public interface Presenter {
        void loadNextRooms();

        void refreshRooms();

        void filterRooms(RoomFilter roomFilter);

        void updateFilter(RoomFilter roomFilter);

        void clearFilters();

        void clearTextFilter();

        void unbindFirebaseToken();
    }
}
