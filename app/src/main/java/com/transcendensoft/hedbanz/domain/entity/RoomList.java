package com.transcendensoft.hedbanz.domain.entity;
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

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Common entity for {@link com.transcendensoft.hedbanz.presentation.rooms.RoomsFragment} and
 * {@link com.transcendensoft.hedbanz.presentation.roomcreation.CreateRoomFragment}
 * in order to dynamically change list with rooms after room creation.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RoomList {
    private List<Room> mRooms;
    private PublishSubject<Room> mSubject;
    private CompositeDisposable mCompositeDisposable;

    public RoomList(List<Room> mRooms) {
        this.mRooms = mRooms;
        this.mSubject = PublishSubject.create();
        mCompositeDisposable = new CompositeDisposable();
    }

    public void addRoom(Room room){
        mRooms.add(room);
        mSubject.onNext(room);
    }

    public void addAllRooms(List<Room> rooms){
        mRooms.addAll(rooms);
    }

    public void clearRooms(){
        mRooms.clear();
    }

    public boolean isEmpty(){
        return mRooms.isEmpty();
    }

    public List<Room> getRooms() {
        return mRooms;
    }

    public void subscribe(@NonNull Consumer<Room> onNext){
        mCompositeDisposable.add(mSubject.subscribe(onNext, Timber::e));
    }

    public void dispose(){
        mCompositeDisposable.dispose();
    }
}
