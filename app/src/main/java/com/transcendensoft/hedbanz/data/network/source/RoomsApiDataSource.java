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

import com.transcendensoft.hedbanz.data.models.InviteDTO;
import com.transcendensoft.hedbanz.data.models.RoomDTO;
import com.transcendensoft.hedbanz.data.models.RoomFilterDTO;
import com.transcendensoft.hedbanz.data.models.RoomListDTO;
import com.transcendensoft.hedbanz.data.source.RoomDataSource;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Create and get rooms from our server
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ApplicationScope
public class RoomsApiDataSource extends ApiDataSource implements RoomDataSource {
    @Inject
    RoomsApiDataSource() {
        super();
    }

    public Observable<RoomListDTO> getRooms(int page, long userId) {
        return mService.getRooms(page, userId);
    }

    public Observable<RoomDTO> createRoom(RoomDTO roomDTO, long userId) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("password", roomDTO.getPassword());
        result.put("name", roomDTO.getName());
        result.put("maxPlayers", roomDTO.getMaxPlayers());
        result.put("userId", userId);
        result.put("isPrivate", roomDTO.isPrivate());
        result.put("iconId", roomDTO.getIconId());
        result.put("stickerId", roomDTO.getStickerId());

        return mService.createRoom(result);
    }

    public Observable<RoomListDTO> filterRooms(int page, long userId, RoomFilterDTO roomFilter) {
        return mService.filterRooms(page, userId, roomFilter);
    }

    public Maybe<Object> isPasswordCorrect(long userId, long roomId, String password){
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("userId", userId);
        result.put("roomId", roomId);
        result.put("password", password);

        return mService.checkRoomPasswordCorrect(result);
    }

    public Maybe<Object> inviteFriend(InviteDTO inviteDTO){
        return mService.inviteFriendToRoom(inviteDTO);
    }
}
