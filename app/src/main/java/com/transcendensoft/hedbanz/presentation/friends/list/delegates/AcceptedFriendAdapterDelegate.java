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
import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.presentation.friends.list.holder.AcceptedFriendViewHolder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.subjects.PublishSubject;

/**
 * This delegate is responsible for creating
 * {@link com.transcendensoft.hedbanz.presentation.friends.list.holder.AcceptedFriendViewHolder}
 * and binding ViewHolder widgets according to model.
 * <p>
 * An AdapterDelegate get added to an AdapterDelegatesManager.
 * This manager is the man in the middle between RecyclerView.
 * Adapter and each AdapterDelegate.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class AcceptedFriendAdapterDelegate extends AdapterDelegate<List<Friend>> {
    private PublishSubject<Friend> removeFriendSubject = PublishSubject.create();

    @Inject
    public AcceptedFriendAdapterDelegate() {}

    @Override
    protected boolean isForViewType(@NonNull List<Friend> items, int position) {
        return items.get(position).isAccepted() && !items.get(position).isPending();
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_friend_accepted, parent, false);
        return new AcceptedFriendViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Friend> items, int position,
                                    @NonNull RecyclerView.ViewHolder holder,
                                    @NonNull List<Object> payloads) {
        Friend friend = items.get(position);
        AcceptedFriendViewHolder acceptedFriendViewHolder = (AcceptedFriendViewHolder) holder;

        acceptedFriendViewHolder.bindName(friend.getLogin());
        acceptedFriendViewHolder.bindIcon(friend.getIconId().getResId());
        acceptedFriendViewHolder.removeFriendObservable(friend)
                .subscribe(removeFriendSubject);
    }

    public PublishSubject<Friend> getRemoveFriendSubject() {
        return removeFriendSubject;
    }
}
