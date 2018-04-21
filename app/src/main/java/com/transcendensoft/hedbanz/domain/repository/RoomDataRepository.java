package com.transcendensoft.hedbanz.domain.repository;
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

import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.RoomFilter;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Interface that represents a Repository for getting {@link Room} related data.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface RoomDataRepository {
    Observable<List<Room>> getRooms(int page, DataPolicy dataPolicy);

    Observable<Room> createRoom(Room room, long userId);

    Observable<List<Room>> filterRooms(int page, RoomFilter roomFilter, DataPolicy dataPolicy);

    Completable isPasswordCorrect(long roomId, String password, DataPolicy dataPolicy);
}
