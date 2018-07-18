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
    private String word;
    private User wordReceiverUser;
    private Long roomId;

    public Word(User userFrom, MessageType messageType, String word, User wordReceiverUser, Long roomId) {
        super(0L, null, userFrom, messageType, null,0L);
        this.word = word;
        this.wordReceiverUser = wordReceiverUser;
        this.roomId = roomId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public User getWordReceiverUser() {
        return wordReceiverUser;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setWordReceiverUser(User wordReceiverUser) {
        this.wordReceiverUser = wordReceiverUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        if (word != null ? !word.equals(word1.word) : word1.word != null) return false;
        return wordReceiverUser != null ? wordReceiverUser.equals(word1.wordReceiverUser) : word1.wordReceiverUser == null;
    }

    @Override
    public int hashCode() {
        int result = word != null ? word.hashCode() : 0;
        result = 31 * result + (wordReceiverUser != null ? wordReceiverUser.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private User userFrom;
        private MessageType messageType;
        private Long roomId;
        private User wordReceiverUser;
        private String word;

        public Builder setUserFrom(User userFrom) {
            this.userFrom = userFrom;
            return this;
        }

        public Builder setRoomId(long roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder setWordReceiverUser(User wordReceiverUser) {
            this.wordReceiverUser = wordReceiverUser;
            return this;
        }

        public Builder setWord(String word) {
            this.word = word;
            return this;
        }

        public Builder setMessageType(MessageType messageType) {
            this.messageType = messageType;
            return this;
        }

        public Word build() {
            return new Word(userFrom, messageType, word, wordReceiverUser, roomId);
        }
    }
}
