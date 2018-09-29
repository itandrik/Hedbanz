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

import com.transcendensoft.hedbanz.domain.entity.PlayerStatus;
import com.transcendensoft.hedbanz.domain.entity.User;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Common entity for game activity and menu fragment to show
 * players info reactively.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RxUser {
    private User mUser;
    private PublishSubject<User> mSubject;

    public RxUser(User mUser) {
        this.mUser = mUser;
        this.mSubject = PublishSubject.create();
    }

    public User getUser() {
        return mUser;
    }

    public void setId(long id) {
        this.mUser.setId(id);
    }

    public void setLogin(String login) {
        mUser.setLogin(login);
        mSubject.onNext(mUser);
    }

    public void setStatus(PlayerStatus status) {
        mUser.setPlayerStatus(status);
        mSubject.onNext(mUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RxUser rxUser = (RxUser) o;

        return mUser != null ? mUser.equals(rxUser.mUser) : rxUser.mUser == null;
    }

    @Override
    public int hashCode() {
        return mUser != null ? mUser.hashCode() : 0;
    }

    public void setIsWinner(boolean isWinner){
        mUser.setWinner(isWinner);
        mSubject.onNext(mUser);
        Timber.i("RXUSER: setIsWinner " + isWinner + " to user " + mUser.getLogin());
    }

    public void setIsFriend(boolean isFriend){
        mUser.setFriend(isFriend);
        mSubject.onNext(mUser);
    }

    public void setWord(String word){
        mUser.setWord(word);
        mSubject.onNext(mUser);
    }

    public void setWordVisible(Boolean wordVisible) {
        if(wordVisible == null){
            wordVisible = false;
        }
        if(wordVisible != mUser.isWordVisible()) {
            mUser.setWordVisible(wordVisible);
            mSubject.onNext(mUser);
        }
    }

    public void setUser(User user){
        mUser = user;
    }

    public Observable<User> userObservable(){
        return mSubject;
    }
}
