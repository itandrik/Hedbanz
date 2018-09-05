package com.transcendensoft.hedbanz.data.source;
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

import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Base interface for remote and local data that
 * describes methods of getting or updating data
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface RoomDataSource {
    Observable<RoomListDTO> getRooms(int page, long userId);

    Observable<RoomDTO> createRoom(RoomDTO roomDTO, long userId);

    Observable<RoomListDTO> filterRooms(int page, long userId, RoomFilterDTO roomFilter);

    Maybe<Object> isPasswordCorrect(long userId, long roomId, String password);

    Maybe<Object> inviteFriend(InviteDTO inviteDTO);
}
