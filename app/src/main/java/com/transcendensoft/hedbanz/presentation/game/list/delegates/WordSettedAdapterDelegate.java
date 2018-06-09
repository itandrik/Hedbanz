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

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.entity.Word;
import com.transcendensoft.hedbanz.presentation.base.RxAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.holder.WordSettedViewHolder;

import java.util.List;

import javax.inject.Inject;

/**
 * This delegate is responsible for creating
 * {@link com.transcendensoft.hedbanz.presentation.game.list.holder.WordSettedViewHolder}
 * and binding ViewHolder widgets according to model.
 * <p>
 * An AdapterDelegate get added to an AdapterDelegatesManager.
 * This manager is the man in the middle between RecyclerView.
 * Adapter and each AdapterDelegate.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class WordSettedAdapterDelegate extends RxAdapterDelegate<List<Message>> {
    private PreferenceManager mPreferenceManger;

    @Inject
    public WordSettedAdapterDelegate(PreferenceManager preferenceManager) {
        this.mPreferenceManger = preferenceManager;
    }

    @Override
    protected boolean isForViewType(@NonNull List<Message> items, int position) {
        Message message = items.get(position);

        return (message instanceof Word) &&
                (message.getMessageType() == MessageType.WORD_SETTED);
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_word_setted, parent, false);
        return new WordSettedViewHolder(context, itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Message> items, int position,
                                    @NonNull RecyclerView.ViewHolder holder,
                                    @NonNull List<Object> payloads) {
        WordSettedViewHolder viewHolder = (WordSettedViewHolder) holder;
        Word word = (Word) items.get(position);

        User currentUser = mPreferenceManger.getUser();
        if (currentUser.equals(word.getSenderUser())) {
            if (word.getWordReceiverUser() != null && word.getWordReceiverUser().getLogin() != null) {
                viewHolder.bindCurrentUserSettedText(word.getWordReceiverUser().getLogin().trim());
            }
        } else if (currentUser.equals(word.getWordReceiverUser())) {
            viewHolder.bindTextSettedToCurrentUser(word.getSenderUser().getLogin().trim());
        } else {
            viewHolder.bindText(word.getSenderUser().getLogin(),
                    word.getWordReceiverUser().getLogin());
        }
    }
}