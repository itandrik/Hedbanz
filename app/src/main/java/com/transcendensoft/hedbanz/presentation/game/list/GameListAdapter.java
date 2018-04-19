package com.transcendensoft.hedbanz.presentation.game.list;
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

import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.presentation.base.RecyclerDelegationAdapter;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.JoinedLeftUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.LoadingAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.MessageOtherUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.MessageThisUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.NetworkErrorAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.ServerErrorAdapterDelegate;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Adapter for game mode recycler.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GameListAdapter extends RecyclerDelegationAdapter<Message> {
    private LoadingAdapterDelegate mLoadingAdapterDelegate;
    private ServerErrorAdapterDelegate mServerErrorAdapterDelegate;
    private NetworkErrorAdapterDelegate mNetworkErrorAdapterDelegate;
    private MessageThisUserAdapterDelegate mMessageThisUserAdapterDelegate;
    private MessageOtherUserAdapterDelegate mMessageOtherUserAdapterDelegate;
    private JoinedLeftUserAdapterDelegate mJoinedLeftUserAdapterDelegate;

    @Inject
    public GameListAdapter(LoadingAdapterDelegate loadingAdapterDelegate,
                           ServerErrorAdapterDelegate serverErrorAdapterDelegate,
                           NetworkErrorAdapterDelegate networkErrorAdapterDelegate,
                           MessageThisUserAdapterDelegate messageThisUserAdapterDelegate,
                           MessageOtherUserAdapterDelegate messageOtherUserAdapterDelegate,
                           JoinedLeftUserAdapterDelegate joinedLeftUserAdapterDelegate) {
        super();

        this.mLoadingAdapterDelegate = loadingAdapterDelegate;
        this.mServerErrorAdapterDelegate = serverErrorAdapterDelegate;
        this.mNetworkErrorAdapterDelegate = networkErrorAdapterDelegate;
        this.mMessageThisUserAdapterDelegate = messageThisUserAdapterDelegate;
        this.mMessageOtherUserAdapterDelegate = messageOtherUserAdapterDelegate;
        this.mJoinedLeftUserAdapterDelegate = joinedLeftUserAdapterDelegate;

        delegatesManager.addDelegate(loadingAdapterDelegate)
                .addDelegate(serverErrorAdapterDelegate)
                .addDelegate(networkErrorAdapterDelegate)
                .addDelegate(messageThisUserAdapterDelegate)
                .addDelegate(messageOtherUserAdapterDelegate)
                .addDelegate(joinedLeftUserAdapterDelegate);
    }

    @Nullable
    public Observable<Object> retryServerClickObservable() {
        return mServerErrorAdapterDelegate.getRetryServerClickObservable();
    }

    @Nullable
    public Observable<Object> retryNetworkClickObservable() {
        return mNetworkErrorAdapterDelegate.getRetryNetworkClickObservable();
    }
}
