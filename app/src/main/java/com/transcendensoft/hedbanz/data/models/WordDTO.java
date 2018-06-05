package com.transcendensoft.hedbanz.data.models;
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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * DTO for word entity.
 * All word properties received from server described here
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class WordDTO {
    public static final String SENDER_ID = "senderId";
    public static final String ROOM_ID = "roomId";
    public static final String WORD_RECEIVER_ID = "wordReceiverId";
    public static final String WORD = "word";

    @SerializedName(SENDER_ID)
    @Expose
    private long senderId;
    @SerializedName(ROOM_ID)
    @Expose
    private long roomId;
    @SerializedName(WORD_RECEIVER_ID)
    @Expose
    private long wordReceiverId;
    @SerializedName(WORD)
    @Expose
    private String word;

    private WordDTO(long senderId, long roomId, long wordReceiverId, String word) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordDTO wordDTO = (WordDTO) o;

        if (senderId != wordDTO.senderId) return false;
        if (roomId != wordDTO.roomId) return false;
        if (wordReceiverId != wordDTO.wordReceiverId) return false;
        return word != null ? word.equals(wordDTO.word) : wordDTO.word == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (senderId ^ (senderId >>> 32));
        result = 31 * result + (int) (roomId ^ (roomId >>> 32));
        result = 31 * result + (int) (wordReceiverId ^ (wordReceiverId >>> 32));
        result = 31 * result + (word != null ? word.hashCode() : 0);
        return result;
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

        public WordDTO build() {
            return new WordDTO(senderId, roomId, wordReceiverId, word);
        }
    }
}
