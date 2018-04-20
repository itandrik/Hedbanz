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
 * Common entity for {@link com.transcendensoft.hedbanz.presentation.game.GameActivity} and
 * in order to set and get user guess word
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class Word extends Message{
    private long senderId;
    private long roomId;
    private long wordReceiverId;
    private String word;
    private User senderUser;
    private User wordReceiverUser;

    public Word(User senderUser, User wordReceiverUser) {
        this.senderUser = senderUser;
        this.wordReceiverUser = wordReceiverUser;
    }

    private Word(long senderId, long roomId, long wordReceiverId, String word) {
        this.senderId = senderId;
        this.roomId = roomId;
        this.wordReceiverId = wordReceiverId;
        this.word = word;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getWordReceiverId() {
        return wordReceiverId;
    }

    public void setWordReceiverId(long wordReceiverId) {
        this.wordReceiverId = wordReceiverId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public User getWordReceiverUser() {
        return wordReceiverUser;
    }

    public void setWordReceiverUser(User wordReceiverUser) {
        this.wordReceiverUser = wordReceiverUser;
    }

    public static class Builder {
        private long senderId;
        private long roomId;
        private long wordReceiverId;
        private String word;

        public Builder setSenderId(long senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder setRoomId(long roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder setWordReceiverId(long wordReceiverId) {
            this.wordReceiverId = wordReceiverId;
            return this;
        }

        public Builder setWord(String word) {
            this.word = word;
            return this;
        }

        public Word build() {
            return new Word(senderId, roomId, wordReceiverId, word);
        }
    }
}
