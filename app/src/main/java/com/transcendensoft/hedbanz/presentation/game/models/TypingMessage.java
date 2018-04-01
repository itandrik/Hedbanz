package com.transcendensoft.hedbanz.presentation.game.models;
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
import com.transcendensoft.hedbanz.domain.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Message that represents typing behaviour.
 * Needed to show typing list of users.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class TypingMessage extends Message {
    private List<User> typingUsers;

    public TypingMessage(Message message) {
        super(message.getId(), message.getMessage(), message.getUserFrom(),
                message.getMessageType(), message.getCreateDate());
        typingUsers = new ArrayList<>();
        typingUsers.add(message.getUserFrom());
    }

    public void addUser(User user) {
        typingUsers.add(user);
    }

    public void removeUser(User user) {
        typingUsers.remove(user);
    }

    public List<User> getTypingUsers() {
        return typingUsers;
    }
}
