package com.transcendensoft.hedbanz.presentation.game.models;
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
import com.transcendensoft.hedbanz.domain.entity.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;

/**
 * Common entity for game activity and menu fragment to show
 * room info reactively
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class RxRoom {
    private Room mRoom;
    private List<RxUser> mPlayers;
    private PublishSubject<Room> mRoomInfoSubject;
    private PublishSubject<RxUser> mRemoveUserSubject;
    private PublishSubject<RxUser> mAddUserSubject;

    @Inject ObservableTransformer mSchedulersTransformer;

    public RxRoom(Room room) {
        this.mRoom = room;

        mPlayers = new ArrayList<>();
        if (mRoom != null && mRoom.getPlayers() != null) {
            for (User user : mRoom.getPlayers()) {
                mPlayers.add(new RxUser(user));
            }
        }
        mRoomInfoSubject = PublishSubject.create();
        mRemoveUserSubject = PublishSubject.create();
        mAddUserSubject = PublishSubject.create();
    }

    public Room getRoom() {
        return mRoom;
    }

    public void setRoom(Room mRoom) {
        this.mRoom = mRoom;
        mRoomInfoSubject.onNext(mRoom);
    }

    public List<RxUser> getRxPlayers() {
        return mPlayers;
    }

    public void setId(long id) {
        mRoom.setId(id);
        mRoomInfoSubject.onNext(mRoom);
    }

    public void setPassword(String password) {
        mRoom.setPassword(password);
        mRoomInfoSubject.onNext(mRoom);
    }

    public void setMaxPlayers(byte maxPlayers) {
        mRoom.setMaxPlayers(maxPlayers);
        mRoomInfoSubject.onNext(mRoom);
    }

    public void setPlayers(List<RxUser> players) {
        mPlayers = players;
        mRoomInfoSubject.onNext(mRoom);
    }

    public void setCurrentPlayersNumber(byte currentPlayersNumber) {
        mRoom.setCurrentPlayersNumber(currentPlayersNumber);
        mRoomInfoSubject.onNext(mRoom);
    }

    public void setStartDate(long startDate) {
        mRoom.setStartDate(startDate);
        mRoomInfoSubject.onNext(mRoom);
    }

    public void setEndDate(long endDate) {
        mRoom.setEndDate(endDate);
        mRoomInfoSubject.onNext(mRoom);
    }

    public void setName(String name) {
        mRoom.setName(name);
        mRoomInfoSubject.onNext(mRoom);
    }

    public void setWithPassword(boolean withPassword) {
        mRoom.setWithPassword(withPassword);
        mRoomInfoSubject.onNext(mRoom);
    }

    public void addPlayer(User user) {
        RxUser rxUser = new RxUser(user);
        mPlayers.add(rxUser);
        mRoom.getPlayers().add(user);
        mAddUserSubject.onNext(rxUser);
    }

    public void removePlayer(User user) {
        RxUser rxUserResult = null;
        for (RxUser rxUser : mPlayers) {
            if (rxUser.getUser().equals(user)) {
                rxUserResult = rxUser;
                break;
            }
        }
        if (rxUserResult != null) {
            mPlayers.remove(rxUserResult);
            mRoom.getPlayers().remove(user);
            mRemoveUserSubject.onNext(rxUserResult);
        }
    }

    public Observable<Room> roomInfoObservable() {
        return mRoomInfoSubject;
    }

    public Observable<RxUser> addUserObservable() {
        return mAddUserSubject;
    }

    public Observable<RxUser> removeUserObservable() {
        return mRemoveUserSubject;
    }
}
