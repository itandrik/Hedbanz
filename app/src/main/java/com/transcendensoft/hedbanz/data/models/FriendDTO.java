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

import com.google.gson.annotations.SerializedName;

/**
 * DTO for friend entity.
 * All friend properties received from server described here
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class FriendDTO extends UserDTO {
    @SerializedName("isAccepted")
    private boolean isAccepted;

    private FriendDTO(long id, String email, String password, long money,
                      Long registrationDate, String login, boolean isAfk, boolean isAccepted,
                      boolean isFriend, String word) {
        super(id, email, password, money, registrationDate, login, isAfk, isFriend, word);
        this.isAccepted = isAccepted;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public static class Builder {
        private long id;
        private String email;
        private String password;
        private long money;
        private Long registrationDate;
        private String login;
        private boolean isAccepted;
        private boolean isAfk;
        private boolean isFriend;
        private String word;

        public FriendDTO.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public FriendDTO.Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public FriendDTO.Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public FriendDTO.Builder setMoney(long money) {
            this.money = money;
            return this;
        }

        public FriendDTO.Builder setRegistrationDate(Long registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public FriendDTO.Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        public FriendDTO.Builder setIsAccepted(boolean isAccepted) {
            this.isAccepted = isAccepted;
            return this;
        }

        public FriendDTO.Builder setIsAfk(boolean isAfk) {
            this.isAfk = isAfk;
            return this;
        }

        public FriendDTO.Builder setIsFriend(boolean isFriend) {
            this.isFriend = isFriend;
            return this;
        }

        public FriendDTO.Builder setWord(String word) {
            this.word = word;
            return this;
        }


        public FriendDTO build() {
            return new FriendDTO(id, email, password, money, registrationDate,
                    login, isAfk, isAccepted, isFriend, word);
        }
    }
}
