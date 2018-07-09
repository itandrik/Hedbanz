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

import com.transcendensoft.hedbanz.data.models.mapper.FriendModelDataMapper;
import com.transcendensoft.hedbanz.data.network.source.FriendsApiDataSource;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.domain.repository.FriendsDataRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Interface that represents a Repository (or Gateway)
 * for getting or deleting Friend related data.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class FriendsDataRepositoryImpl implements FriendsDataRepository{
    private FriendsApiDataSource mFriendsApiDataSource;
    private FriendModelDataMapper mFriendsDataMapper;

    @Inject
    public FriendsDataRepositoryImpl(FriendsApiDataSource friendsApiDataSource,
                                     FriendModelDataMapper friendsDataMapper) {
        this.mFriendsApiDataSource = friendsApiDataSource;
        this.mFriendsDataMapper = friendsDataMapper;
    }

    @Override
    public Observable<List<Friend>> getFriends(long userId, DataPolicy dataPolicy) {
        if(dataPolicy == DataPolicy.API) {
            return mFriendsApiDataSource.getFriends(userId).map(mFriendsDataMapper::convert);
        } else if(dataPolicy == DataPolicy.DB){
            return Observable.error(new UnsupportedOperationException());
        }
        return Observable.error(new UnsupportedOperationException());
    }

    @Override
    public Completable removeFriend(long userId, long friendId, DataPolicy dataPolicy) {
        if(dataPolicy == DataPolicy.API) {
            return mFriendsApiDataSource.removeFriend(userId, friendId);
        } else if(dataPolicy == DataPolicy.DB){
            return Completable.error(new UnsupportedOperationException());
        }
        return Completable.error(new UnsupportedOperationException());
    }

    @Override
    public Completable declineFriend(long userId, long friendId, DataPolicy dataPolicy) {
        if(dataPolicy == DataPolicy.API) {
            return mFriendsApiDataSource.declineFriend(userId, friendId);
        } else if(dataPolicy == DataPolicy.DB){
            return Completable.error(new UnsupportedOperationException());
        }
        return Completable.error(new UnsupportedOperationException());
    }

    @Override
    public Completable acceptFriend(long userId, long friendId, DataPolicy dataPolicy) {
        if(dataPolicy == DataPolicy.API) {
            return mFriendsApiDataSource.acceptFriend(userId, friendId);
        } else if(dataPolicy == DataPolicy.DB){
            return Completable.error(new UnsupportedOperationException());
        }
        return Completable.error(new UnsupportedOperationException());
    }

    @Override
    public Completable addFriend(long userId, long friendId, DataPolicy dataPolicy) {
        if(dataPolicy == DataPolicy.API) {
            return mFriendsApiDataSource.addFriend(userId, friendId);
        } else if(dataPolicy == DataPolicy.DB){
            return Completable.error(new UnsupportedOperationException());
        }
        return Completable.error(new UnsupportedOperationException());
    }
}
