package com.transcendensoft.hedbanz.data.network.manager;
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

import com.transcendensoft.hedbanz.data.network.dto.Room;
import com.transcendensoft.hedbanz.data.network.dto.RoomFilter;
import com.transcendensoft.hedbanz.data.network.dto.ServerResult;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * Create and get rooms from our server
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RoomsCrudApiManager extends ApiManager{
    private static final class Holder {
        static final RoomsCrudApiManager INSTANCE = new RoomsCrudApiManager();
    }

    private RoomsCrudApiManager() {
        super();
    }

    public static RoomsCrudApiManager getInstance() {
        return RoomsCrudApiManager.Holder.INSTANCE;
    }

    public Observable<ServerResult<List<Room>>> getRooms(int page){
        return mService.getRooms(page)
                .compose(applySchedulers());
    }
    public Observable<ServerResult<Room>> createRoom(String password, String name,
                                                     byte maxPlayers, long userId){
        HashMap<String, Object> result = new HashMap<>();
        result.put("password", password);
        result.put("name", name);
        result.put("maxPlayers", maxPlayers);
        result.put("userId", userId);

        return mService.createRoom(result)
                .compose(applySchedulers());
    }

    public Observable<ServerResult<List<Room>>> filterRooms(int page, RoomFilter roomFilter){
        return mService.filterRooms(page, roomFilter)
                .compose(applySchedulers());
    }

}
