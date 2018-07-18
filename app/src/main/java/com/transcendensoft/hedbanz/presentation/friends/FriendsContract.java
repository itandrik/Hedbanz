package com.transcendensoft.hedbanz.presentation.friends;
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

import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.presentation.base.BaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * View and Presenter interfaces contract for friends list presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class FriendsContract {
    interface View extends BaseView {
        void addFriendsToRecycler(List<Friend> friends);

        void deleteFriend(Friend friend);

        void clearFriends();

        void showEmptyList();

        void sureToDeleteFriend(Friend friend);

        void sureToDeclineFriend(Friend friend);

        void successAcceptFriend(Friend friend);

        void successDeclineFriend(Friend friend);

        void successDeleteFriend(Friend friend);

        void errorFriend(Friend friend);
    }

    interface Presenter {
        void processDeleteFriendClick(Observable<Friend> clickObservable);

        void processAcceptFriendClick(Observable<Friend> clickObservable);

        void processDeclineFriendClick(Observable<Friend> clickObservable);

        void deleteFriend(Friend friend);

        void acceptFriend(Friend friend);

        void declineFriend(Friend friend);

        void getFriends();
    }
}
