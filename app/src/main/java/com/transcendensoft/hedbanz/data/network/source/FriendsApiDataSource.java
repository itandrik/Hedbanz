package com.transcendensoft.hedbanz.data.network.source;
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

import com.transcendensoft.hedbanz.data.models.FriendDTO;
import com.transcendensoft.hedbanz.data.source.FriendDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Get, delete and accept friends iin server
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class FriendsApiDataSource extends ApiDataSource implements FriendDataSource {
    @Inject
    public FriendsApiDataSource() {
        super();
    }

    @Override
    public Observable<List<FriendDTO>> getFriends(long userId) {
        return mService.getFriends(userId);
    }

    @Override
    public Completable removeFriend(long userId, long friendId) {
        return null;
    }

    @Override
    public Completable addFriend(long userId, long friendId) {
        return mService.addFriend(userId, friendId);
    }

    @Override
    public Completable acceptFriend(long userId, long friendId) {
        return mService.acceptFriend(userId, friendId);
    }
}
