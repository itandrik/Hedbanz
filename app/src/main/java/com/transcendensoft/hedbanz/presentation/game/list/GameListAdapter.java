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
import com.transcendensoft.hedbanz.presentation.game.list.delegates.MessageOtherUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.MessageThisUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.TypingAdapterDelegate;

import javax.inject.Inject;

/**
 * Adapter for game mode recycler.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class GameListAdapter extends RecyclerDelegationAdapter<Message> {
    @Inject
    public GameListAdapter() {
        super();
        delegatesManager
                .addDelegate(new MessageThisUserAdapterDelegate())
                .addDelegate(new MessageOtherUserAdapterDelegate())
                .addDelegate(new JoinedLeftUserAdapterDelegate())
                .addDelegate(new TypingAdapterDelegate());
        }
}
