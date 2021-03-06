package com.transcendensoft.hedbanz.data.models.mapper;
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

import com.transcendensoft.hedbanz.data.models.RoomFilterDTO;
import com.transcendensoft.hedbanz.domain.entity.RoomFilter;

import javax.inject.Inject;

/**
 * Mapper class used to transform {@link com.transcendensoft.hedbanz.data.models.RoomFilterDTO}
 * (in the data layer) to {@link com.transcendensoft.hedbanz.domain.entity.RoomFilter} in the
 * domain layer.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RoomFilterModelDataMapper {
    @Inject
    public RoomFilterModelDataMapper() {
    }

    public RoomFilterDTO convert(RoomFilter roomFilter){
        RoomFilterDTO roomFilterDTO = null;
        if(roomFilter != null){
            roomFilterDTO = new RoomFilterDTO.Builder()
                    .setRoomName(roomFilter.getRoomName())
                    .setIsPrivate(roomFilter.isPrivate())
                    .setMaxPlayers(roomFilter.getMaxPlayers())
                    .setMinPlayers(roomFilter.getMinPlayers())
                    .build();
        }
        return roomFilterDTO;
    }
}
