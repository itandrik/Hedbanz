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
public class WordDTO extends MessageDTO{
    @SerializedName("wordReceiverUser")
    @Expose
    private UserDTO wordReceiverUser;
    @SerializedName("word")
    @Expose
    private String word;


    public WordDTO(long roomId, int type, UserDTO senderUser, UserDTO wordReceiverUser, String word) {
        super(0L, 0L, roomId, "", type, 0L, 0L, senderUser);
        this.wordReceiverUser = wordReceiverUser;
        this.word = word;
    }

    public UserDTO getWordReceiverUser() {
        return wordReceiverUser;
    }

    public void setWordReceiverUser(UserDTO wordReceiverUser) {
        this.wordReceiverUser = wordReceiverUser;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public static class Builder {
        private long roomId;
        private int type;
        private UserDTO senderUser;
        private UserDTO wordReceiverUser;
        private String word;

        public Builder setWordReceiverUser(UserDTO wordReceiverUser) {
            this.wordReceiverUser = wordReceiverUser;
            return this;
        }

        public Builder setWord(String word) {
            this.word = word;
            return this;
        }

        public Builder setRoomId(long roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setSenderUser(UserDTO senderUser) {
            this.senderUser = senderUser;
            return this;
        }

        public WordDTO build() {
            return new WordDTO(roomId, type, senderUser, wordReceiverUser, word);
        }
    }
}
