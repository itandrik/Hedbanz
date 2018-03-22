package com.transcendensoft.hedbanz.domain.interactor.rooms;
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

import com.transcendensoft.hedbanz.data.repository.RoomDataRepositoryImpl;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.RoomFilter;
import com.transcendensoft.hedbanz.domain.interactor.PaginationState;
import com.transcendensoft.hedbanz.domain.interactor.PaginationUseCase;
import com.transcendensoft.hedbanz.domain.repository.RoomDataRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for filtering data related to a specific list of
 * {@link com.transcendensoft.hedbanz.domain.entity.Room}.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class FilterRoomsInteractor extends PaginationUseCase<Room, Void, RoomFilter> {
    public static final String NULL_ROOM_FILTER_MSG = "RoomFilter is NULL when filtering." +
            "Call refresh() before method invocation";
    private final RoomDataRepository mRoomRepository;
    private RoomFilter mRoomFilter;

    @Inject
    public FilterRoomsInteractor(ObservableTransformer mSchedulersTransformer,
                                 CompositeDisposable mCompositeDisposable,
                                 RoomDataRepositoryImpl roomDataRepository) {
        super(mSchedulersTransformer, mCompositeDisposable);
        mRoomRepository = roomDataRepository;
    }

    @Override
    public FilterRoomsInteractor refresh(RoomFilter roomFilter) {
        super.refresh(roomFilter);
        mRoomFilter = roomFilter;
        return this;
    }

    @Override
    protected Observable<PaginationState<Room>> buildUseCaseObservable(Void params) {
        if (mRoomFilter == null) {
            throw new NullPointerException(NULL_ROOM_FILTER_MSG);
        }

        return mRoomRepository.filterRooms(mCurrentPage, mRoomFilter, DataPolicy.API)
                .flatMap(this::convertEntitiesToPagingResult);
    }
}
