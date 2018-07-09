package com.transcendensoft.hedbanz.domain.entity;
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

/**
 * Enum with message types. Id of message type must be the same as in server.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public enum MessageType {
    JOINED_USER(1),
    LEFT_USER(2),
    START_TYPING(3),
    STOP_TYPING(4),
    SIMPLE_MESSAGE_OTHER_USER(5),
    SIMPLE_MESSAGE_THIS_USER(6),
    SIMPLE_MESSAGE(7),
    ERROR_SERVER(8),
    ERROR_NETWORK(9),
    LOADING(10),
    WORD_SETTING(11),
    WORD_SETTED(12),
    USER_AFK(13),
    USER_RETURNED(14),
    GUESS_WORD_THIS_USER(15),
    ASKING_QUESTION_THIS_USER(16),
    VOTING_FOR_QUESTION(17),
    GUESS_WORD_OTHER_USER(18),
    ASKING_QUESTION_OTHER_USER(19),
    USER_KICK_WARNING(20),
    USER_KICKED(21),
    UNDEFINED(100500);

    private int id;

    MessageType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MessageType getMessageTypeById(int id){
        for (MessageType messageType: MessageType.values()) {
            if(messageType.id == id){
                return messageType;
            }
        }
        return UNDEFINED;
    }
}
