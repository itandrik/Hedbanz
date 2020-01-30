package com.transcendensoft.hedbanz.data.repository;
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

import androidx.annotation.NonNull;

import com.transcendensoft.hedbanz.data.models.RoomDTO;
import com.transcendensoft.hedbanz.data.models.RoomFilterDTO;
import com.transcendensoft.hedbanz.data.models.RoomListDTO;
import com.transcendensoft.hedbanz.data.models.mapper.InviteModelDataMapper;
import com.transcendensoft.hedbanz.data.models.mapper.RoomFilterModelDataMapper;
import com.transcendensoft.hedbanz.data.models.mapper.RoomModelDataMapper;
import com.transcendensoft.hedbanz.data.network.source.RoomsApiDataSource;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;
import com.transcendensoft.hedbanz.domain.entity.Invite;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.RoomFilter;
import com.transcendensoft.hedbanz.domain.repository.RoomDataRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Interface that represents a Repository (or Gateway)
 * for getting {@link com.transcendensoft.hedbanz.domain.entity.Room} related data.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
@ApplicationScope
public class RoomDataRepositoryImpl implements RoomDataRepository {
    private RoomsApiDataSource mRoomsApiDataSource;
    private RoomModelDataMapper mRoomModelDataMapper;
    private RoomFilterModelDataMapper mRoomFilterModelDataMapper;
    private InviteModelDataMapper mInviteModelDataMapper;
    @Inject
    public RoomDataRepositoryImpl(RoomsApiDataSource roomsApiManager,
                                  RoomModelDataMapper roomModelDataMapper,
                                  RoomFilterModelDataMapper roomFilterModelDataMapper,
                                  InviteModelDataMapper inviteModelDataMapper) {
        this.mRoomsApiDataSource = roomsApiManager;
        this.mRoomModelDataMapper = roomModelDataMapper;
        this.mRoomFilterModelDataMapper = roomFilterModelDataMapper;
        this.mInviteModelDataMapper = inviteModelDataMapper;
    }

    @Override
    public Observable<List<Room>> getRooms(int page, long userId, DataPolicy dataPolicy) {
        if (dataPolicy == DataPolicy.API) {
            return mRoomsApiDataSource.getRooms(page, userId)
                    .map(this::mapActiveRooms)
                    .map(this::sortRooms)
                    .map(mRoomModelDataMapper::convert);
        } else if (dataPolicy == DataPolicy.DB) {
            return Observable.error(new UnsupportedOperationException());
        }
        return Observable.error(new UnsupportedOperationException());
    }

    private RoomListDTO mapActiveRooms(RoomListDTO roomListDTO) {
        if (roomListDTO != null && roomListDTO.getActiveRooms() != null) {
            for (RoomDTO roomDTO : roomListDTO.getActiveRooms()) {
                if (roomDTO != null) {
                    roomDTO.setActive(true);
                }
            }
        }
        return roomListDTO;
    }

    @NonNull
    private List<RoomDTO> sortRooms(RoomListDTO roomListDTO) {
        List<RoomDTO> roomDTOS = new ArrayList<>();
        if (roomListDTO.getActiveRooms() != null) {
            roomDTOS.addAll(roomListDTO.getActiveRooms());
        }
        if (roomListDTO.getAllRooms() != null) {
            for (RoomDTO roomDTO : roomListDTO.getAllRooms()) {
                if (!roomDTOS.contains(roomDTO)) {
                    roomDTOS.add(roomDTO);
                }
            }
        }

        return roomDTOS;
    }

    @Override
    public Observable<Room> createRoom(Room room, long userId) {
        //TODO add room to DB after adding to API.
        RoomDTO roomDTO = mRoomModelDataMapper.convert(room);
        return mRoomsApiDataSource.createRoom(roomDTO, userId).map(mRoomModelDataMapper::convert);
    }

    @Override
    public Observable<List<Room>> filterRooms(int page, long userId, RoomFilter roomFilter, DataPolicy dataPolicy) {
        RoomFilterDTO roomFilterDTO = mRoomFilterModelDataMapper.convert(roomFilter);
        if (dataPolicy == DataPolicy.API) {
            return mRoomsApiDataSource.filterRooms(page, userId, roomFilterDTO)
                    .map(this::mapActiveRooms)
                    .map(this::sortRooms)
                    .map(mRoomModelDataMapper::convert);
        } else if (dataPolicy == DataPolicy.DB) {
            return Observable.error(new UnsupportedOperationException());
        }
        return Observable.error(new UnsupportedOperationException());
    }

    @Override
    public Maybe<Object> isPasswordCorrect(long userId, long roomId, String password, DataPolicy dataPolicy) {
        if (dataPolicy == DataPolicy.API) {
            return mRoomsApiDataSource.isPasswordCorrect(userId, roomId, password);
        } else if (dataPolicy == DataPolicy.DB) {
            return Maybe.error(new UnsupportedOperationException());
        }
        return Maybe.error(new UnsupportedOperationException());
    }

    @Override
    public Maybe<Object> inviteFriend(Invite invite) {
        return mRoomsApiDataSource.inviteFriend(mInviteModelDataMapper.convert(invite));
    }
}
