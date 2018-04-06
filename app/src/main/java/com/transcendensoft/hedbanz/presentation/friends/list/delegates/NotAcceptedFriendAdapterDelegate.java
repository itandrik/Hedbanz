package com.transcendensoft.hedbanz.presentation.friends.list.delegates;
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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.domain.interactor.friends.AcceptFriend;
import com.transcendensoft.hedbanz.presentation.friends.list.FriendsAdapter;
import com.transcendensoft.hedbanz.presentation.friends.list.holder.NotAcceptedFriendViewHolder;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * This delegate is responsible for creating
 * {@link com.transcendensoft.hedbanz.presentation.friends.list.holder.NotAcceptedFriendViewHolder}
 * and binding ViewHolder widgets according to model.
 * <p>
 * An AdapterDelegate get added to an AdapterDelegatesManager.
 * This manager is the man in the middle between RecyclerView.
 * Adapter and each AdapterDelegate.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class NotAcceptedFriendAdapterDelegate extends AdapterDelegate<List<Friend>> {
    private AcceptFriend mAcceptFriendUseCase;
    private FriendsAdapter mAdapter;
    private CompositeDisposable mButtonsCompositeDisposable;

    public NotAcceptedFriendAdapterDelegate(AcceptFriend acceptFriendUseCase, FriendsAdapter adapter) {
        mAcceptFriendUseCase = acceptFriendUseCase;
        mAdapter = adapter;
        mButtonsCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected boolean isForViewType(@NonNull List<Friend> items, int position) {
        return !items.get(position).isAccepted();
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_friend_not_accepted, parent, false);
        return new NotAcceptedFriendViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Friend> items, int position,
                                    @NonNull RecyclerView.ViewHolder holder,
                                    @NonNull List<Object> payloads) {
        Friend friend = items.get(position);
        NotAcceptedFriendViewHolder notAcceptedFriendViewHolder = (NotAcceptedFriendViewHolder) holder;

        notAcceptedFriendViewHolder.bindName(friend.getLogin());
        notAcceptedFriendViewHolder.bindIcon(R.drawable.logo); //TODO change this shit

        mButtonsCompositeDisposable.add(notAcceptedFriendViewHolder.acceptObservable()
                .subscribe(obj -> {
                    AcceptFriend.Param param = new AcceptFriend.Param(DataPolicy.API, friend.getId());
                    //TODO showLoading
                    mAcceptFriendUseCase.execute(param,
                            () -> acceptFriendOnNext(friend),
                            err -> acceptFriendOnError(err, friend));
                }));
        mButtonsCompositeDisposable.add(notAcceptedFriendViewHolder.dismissObservable()
                .subscribe(obj -> {
                    //TODO implement dismiss click
                }));
    }

    private void acceptFriendOnNext(Friend friend) {
        mAdapter.remove(friend);
        List<Friend> adapterFriends = mAdapter.getItems();
        int i = 0;
        while (adapterFriends.get(i).isAccepted()) {
            i++;
        }
        friend.setAccepted(true);
        mAdapter.add(friend);
    }

    private void acceptFriendOnError(Throwable error, Friend friend) {
        //TODO hide loading and show error message
    }

    @Override
    protected void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (mButtonsCompositeDisposable != null) {
            mButtonsCompositeDisposable.dispose();
        }
    }
}
