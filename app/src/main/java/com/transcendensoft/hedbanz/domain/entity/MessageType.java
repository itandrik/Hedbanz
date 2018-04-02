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
 * //TODO add class description 
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
    ERROR(8),
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
