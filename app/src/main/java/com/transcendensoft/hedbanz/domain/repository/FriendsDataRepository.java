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
import com.transcendensoft.hedbanz.domain.entity.Friend;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Interface that represents a Repository for getting
 * {@link com.transcendensoft.hedbanz.domain.entity.User} related data.
 * This users are friends.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface FriendsDataRepository {
    Observable<List<Friend>> getFriends(long userId, DataPolicy dataPolicy);

    Completable removeFriend(long userId, long friendId, DataPolicy dataPolicy);

    Completable acceptFriend(long userId, long friendId, DataPolicy dataPolicy);

    Completable declineFriend(long userId, long friendId, DataPolicy dataPolicy);

    Completable addFriend(long userId, long friendId, DataPolicy dataPolicy);


}
