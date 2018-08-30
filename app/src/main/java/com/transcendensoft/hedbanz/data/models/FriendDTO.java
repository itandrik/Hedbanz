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
 * DTO for friend entity.
 * All friend properties received from server described here
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class FriendDTO extends UserDTO {
    @SerializedName("isAccepted")
    @Expose
    private boolean isAccepted;
    @SerializedName("isPending")
    @Expose
    private boolean isPending;
    @SerializedName("isInRoom")
    @Expose
    private boolean isInGame;
    @SerializedName("isInvited")
    @Expose
    private boolean isInvited;

    private FriendDTO(long id, String email, String password, long money,
                      Long registrationDate, String login, int status,
                      boolean isAccepted, boolean isPending, boolean isFriend,
                      String word, Long userId, int attempts, boolean isWinner,
                      boolean isInGame, boolean isInvited,int gamesNumber, int friendsNumber) {
        super(id, email, password, money, registrationDate, login,
                status, isFriend, word, userId, attempts, isWinner, gamesNumber, friendsNumber);
        this.isAccepted = isAccepted;
        this.isPending = isPending;
        this.isInGame = isInGame;
        this.isInvited = isInvited;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    public boolean isInvited() {
        return isInvited;
    }

    public void setInvited(boolean invited) {
        isInvited = invited;
    }

    public static class Builder {
        private long id;
        private String email;
        private String password;
        private long money;
        private Long registrationDate;
        private String login;
        private boolean isAccepted;
        private boolean isPending;
        private int status;
        private boolean isFriend;
        private String word;
        private Long userId;
        private int attempts;
        private boolean isWinner;
        private boolean isInGame;
        private boolean isInvited;
        private int gamesNumber;
        private int friendsNumber;

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

        public FriendDTO.Builder setIsPending(boolean isPending) {
            this.isPending = isPending;
            return this;
        }

        public FriendDTO.Builder setStatus(int status) {
            this.status = status;
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

        public FriendDTO.Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public FriendDTO.Builder setAttempts(int attempts) {
            this.attempts = attempts;
            return this;
        }

        public FriendDTO.Builder setIsWinner(boolean isWinner) {
            this.isWinner = isWinner;
            return this;
        }

        public FriendDTO.Builder setIsInGame(boolean isInGame) {
            this.isInGame = isInGame;
            return this;
        }

        public FriendDTO.Builder setIsInvited(boolean isInvited) {
            this.isInvited = isInvited;
            return this;
        }

        public FriendDTO.Builder setGamesNumber(int gamesNumber) {
            this.gamesNumber = gamesNumber;
            return this;
        }

        public FriendDTO.Builder setFriendsNumber(int friendsNumber) {
            this.friendsNumber = friendsNumber;
            return this;
        }

        public FriendDTO build() {
            return new FriendDTO(id, email, password, money, registrationDate,
                    login, status, isAccepted, isPending, isFriend, word, userId, attempts,
                    isWinner, isInGame, isInvited, gamesNumber, friendsNumber);
        }
    }
}
