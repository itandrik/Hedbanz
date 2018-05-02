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

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Common entity for game activity and menu fragment to show
 * room info reactively
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RxRoom {
    private Room mRoom;
    private List<RxUser> mPlayers;
    private PublishSubject<Room> mSubject;

    public RxRoom(Room room) {
        this.mRoom = room;
        if(mRoom != null && mRoom.getPlayers() != null) {
            for (User user : mRoom.getPlayers()) {
                mPlayers.add(new RxUser(user));
            }
        }
        mSubject = PublishSubject.create();
    }

    public Room getRoom() {
        return mRoom;
    }

    public void setRoom(Room mRoom) {
        this.mRoom = mRoom;
        mSubject.onNext(mRoom);
    }

    public List<RxUser> getRxPlayers() {
        return mPlayers;
    }

    public void setId(long id) {
        mRoom.setId(id);
        mSubject.onNext(mRoom);
    }

    public void setPassword(String password) {
        mRoom.setPassword(password);
        mSubject.onNext(mRoom);
    }

    public void setMaxPlayers(byte maxPlayers) {
        mRoom.setMaxPlayers(maxPlayers);
        mSubject.onNext(mRoom);
    }

    public void setPlayers(List<RxUser> players) {
        mPlayers = players;
        mSubject.onNext(mRoom);
    }

    public void setCurrentPlayersNumber(byte currentPlayersNumber) {
        mRoom.setCurrentPlayersNumber(currentPlayersNumber);
        mSubject.onNext(mRoom);
    }

    public void setStartDate(long startDate) {
        mRoom.setStartDate(startDate);
        mSubject.onNext(mRoom);
    }

    public void setEndDate(long endDate) {
        mRoom.setEndDate(endDate);
        mSubject.onNext(mRoom);
    }

    public void setName(String name) {
        mRoom.setName(name);
        mSubject.onNext(mRoom);
    }

    public void setWithPassword(boolean withPassword) {
        mRoom.setWithPassword(withPassword);
        mSubject.onNext(mRoom);
    }

    public void addPlayer(User user){
        mPlayers.add(new RxUser(user));
        mRoom.getPlayers().add(user);
        mSubject.onNext(mRoom);
    }

    public void removePlayer(User user){
        RxUser rxUserResult = null;
        for (RxUser rxUser:mPlayers) {
            if(rxUser.getUser().equals(user)){
                rxUserResult = rxUser;
                break;
            }
        }
        if(rxUserResult != null){
            mPlayers.remove(rxUserResult);
            mRoom.getPlayers().remove(user);
            mSubject.onNext(mRoom);
        }
    }

    public Disposable subscribe(@NonNull Consumer<Room> onNext){
        return mSubject.subscribe(onNext, Timber::e);
    }
}
