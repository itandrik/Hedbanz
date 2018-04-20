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

import java.sql.Timestamp;

/**
 * Entity that describes message in game mode from other users.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class Message{
    private long id;
    private long clientMessageId;
    private String message;
    private User userFrom;
    private MessageType messageType;
    private Timestamp createDate;

    protected Message() {
    }

    protected Message(long id, String message, User userFrom, MessageType messageType, Timestamp timestamp, long clientMessageId) {
        this.id = id;
        this.message = message;
        this.userFrom = userFrom;
        this.messageType = messageType;
        this.createDate = timestamp;
        this.clientMessageId = clientMessageId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public long getClientMessageId() {
        return clientMessageId;
    }

    public void setClientMessageId(long clientMessageId) {
        this.clientMessageId = clientMessageId;
    }

    public static class Builder {
        private long id;
        private String message;
        private User userFrom;
        private MessageType messageType;
        private Timestamp createDate;
        private long clientMessageId;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setUserFrom(User userFrom) {
            this.userFrom = userFrom;
            return this;
        }

        public Builder setMessageType(MessageType messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder setCreateDate(Timestamp createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setClientMessageId(long clientMessageId) {
            this.clientMessageId = clientMessageId;
            return this;
        }

        public Message build() {
            return new Message(id, message, userFrom, messageType, createDate, clientMessageId);
        }
    }
}
