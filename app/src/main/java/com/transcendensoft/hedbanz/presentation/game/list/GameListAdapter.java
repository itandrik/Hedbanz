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

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.JoinedLeftUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.MessageOtherUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.MessageThisUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.TypingAdapterDelegate;

import java.util.List;

import javax.inject.Inject;

/**
 * Adapter for main list view
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class GameListAdapter extends ListDelegationAdapter<List<Message>> {
    @Inject
    public GameListAdapter() {
        delegatesManager
                .addDelegate(new MessageThisUserAdapterDelegate())
                .addDelegate(new MessageOtherUserAdapterDelegate())
                .addDelegate(new JoinedLeftUserAdapterDelegate())
                .addDelegate(new TypingAdapterDelegate());
    }

    public void clearAndAddAll(List<Message> messages){
        setItems(messages);
        notifyDataSetChanged();
    }

    public void addAll(List<Message> messages){
        int startPosition = getItems().size() - 1;
        getItems().addAll(messages);
        notifyItemRangeChanged(startPosition, startPosition + messages.size());
    }

    public void clear(){
        getItems().clear();
        notifyDataSetChanged();
    }

    public void add(Message message){
        getItems().add(message);
        notifyItemInserted(getItems().size() -1);
    }

    public void add(int position, Message message){
        getItems().add(position, message);
        notifyItemInserted(position);
    }

    public void remove(int position){
        getItems().remove(position);
        notifyItemRemoved(position);
    }

    public void update(int position, Message message){
        getItems().set(position, message);
        notifyItemChanged(position);
    }

    public Message getItem(int position){
        return getItems().get(position);
    }
}
