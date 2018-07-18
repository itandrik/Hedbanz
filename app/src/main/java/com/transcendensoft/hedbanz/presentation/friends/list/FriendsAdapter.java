package com.transcendensoft.hedbanz.presentation.friends.list;
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

import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.presentation.base.RecyclerDelegationAdapter;
import com.transcendensoft.hedbanz.presentation.friends.list.delegates.AcceptedFriendAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.friends.list.delegates.NotAcceptedFriendAdapterDelegate;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Adapter for friends recycler view.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class FriendsAdapter extends RecyclerDelegationAdapter<Friend> {
    @Inject AcceptedFriendAdapterDelegate mAcceptedFriendAdapterDelegate;
    @Inject NotAcceptedFriendAdapterDelegate mNotAcceptedFriendAdapterDelegate;

    @Inject
    public FriendsAdapter() {
        super();

        delegatesManager
                .addDelegate(mAcceptedFriendAdapterDelegate)
                .addDelegate(mNotAcceptedFriendAdapterDelegate);
    }

    @NonNull
    public Observable<Friend> acceptFriendObservable(){
        return mNotAcceptedFriendAdapterDelegate.getAcceptSubject();
    }

    @NonNull
    public Observable<Friend> declineFriendObservable(){
        return mNotAcceptedFriendAdapterDelegate.getDeclineSubject();
    }

    @NonNull
    public Observable<Friend> deleteFriendObservable(){
        return mAcceptedFriendAdapterDelegate.getRemoveFriendSubject();
    }
}
