package com.transcendensoft.hedbanz.presentation.game.list.delegates;
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
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.game.list.holder.SomeUserMessageViewHolder;

import java.util.List;

import javax.inject.Inject;

import static com.transcendensoft.hedbanz.domain.entity.MessageType.SIMPLE_MESSAGE_OTHER_USER;

/**
 * This delegate is responsible for creating
 * {@link com.transcendensoft.hedbanz.presentation.game.list.holder.SomeUserMessageViewHolder}
 * and binding ViewHolder widgets according to model.
 * <p>
 * An AdapterDelegate get added to an AdapterDelegatesManager.
 * This manager is the man in the middle between RecyclerView.
 * Adapter and each AdapterDelegate.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class MessageOtherUserAdapterDelegate extends AdapterDelegate<List<Message>> {
    @Inject
    public MessageOtherUserAdapterDelegate() {
    }

    @Override
    protected boolean isForViewType(@NonNull List<Message> items, int position) {
        MessageType currentMessageType = items.get(position).getMessageType();
        return currentMessageType == SIMPLE_MESSAGE_OTHER_USER;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_message_some_user, parent, false);
        return new SomeUserMessageViewHolder(context, itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Message> items, int position,
                                    @NonNull RecyclerView.ViewHolder holder,
                                    @NonNull List<Object> payloads) {
        SomeUserMessageViewHolder viewHolder = (SomeUserMessageViewHolder) holder;
        Message message = items.get(position);

        if (message != null) {
            User userFrom = message.getUserFrom();

            String login = null;
            String word = null;
            boolean isWinner = false;

            if (userFrom != null) {
                login = userFrom.getLogin();
                word = userFrom.getWord();
                isWinner = userFrom.isWinner();
            }

            boolean isHideLoginAndImage = false;
            if ((items.size() > 1) && position != 0 &&
                    (items.get(position - 1).getMessageType() == SIMPLE_MESSAGE_OTHER_USER) &&
                    (items.get(position - 1).getUserFrom().equals(message.getUserFrom()))) {
                isHideLoginAndImage = true;
            }

            viewHolder.bindUserWord(word);
            viewHolder.bindShowHideLoginAndImage(isHideLoginAndImage);
            viewHolder.bindUserLogin(login);
            viewHolder.bindMessage(message.getMessage());
            viewHolder.bindUserImage(R.drawable.logo); //TODO change this shit
            viewHolder.bindTime(message.getCreateDate().getTime());
            viewHolder.bindIsWinner(isWinner);
        }
    }
}