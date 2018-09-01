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
 * DTO for user entity.
 * All user properties received from server described here
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class UserDTO {
    public static final String USER_ID_KEY = "userId";
    public static final String SENDER_ID_KEY = "senderId";

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("userId")
    @Expose
    private Long userId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("money")
    @Expose
    private long money;
    @SerializedName("registrationDate")
    @Expose
    private Long registrationDate;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("isFriend")
    @Expose
    private boolean isFriend;
    @SerializedName("word")
    @Expose
    private String word;
    @SerializedName("attempt")
    @Expose
    private int attempts;
    @SerializedName("securityToken")
    @Expose
    private String securityToken;
    @SerializedName("isWinner")
    @Expose
    private boolean isWinner;
    @SerializedName("gamesNumber")
    @Expose
    private int gamesNumber;
    @SerializedName("friendsNumber")
    @Expose
    private int friendsNumber;
    @SerializedName("iconId")
    @Expose
    private int iconId;

    protected UserDTO(long id, String email, String password, long money,
                      Long registrationDate, String login, int status,
                      boolean isFriend, String word, Long userId, int attempts,
                      boolean isWinner, int gamesNumber, int friendsNumber, int iconId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = money;
        this.registrationDate = registrationDate;
        this.login = login;
        this.status = status;
        this.isFriend = isFriend;
        this.word = word;
        this.userId = userId;
        this.attempts = attempts;
        this.isWinner = isWinner;
        this.gamesNumber = gamesNumber;
        this.friendsNumber = friendsNumber;
        this.iconId = iconId;
    }

    public UserDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public String getWord() {
        return word;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public int getGamesNumber() {
        return gamesNumber;
    }

    public UserDTO setGamesNumber(int gamesNumber) {
        this.gamesNumber = gamesNumber;
        return this;
    }

    public int getFriendsNumber() {
        return friendsNumber;
    }

    public UserDTO setFriendsNumber(int friendsNumber) {
        this.friendsNumber = friendsNumber;
        return this;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO user = (UserDTO) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public static class Builder {
        private long id;
        private String email;
        private String password;
        private long money;
        private Long registrationDate;
        private String login;
        private int status;
        private boolean isFriend;
        private String word;
        private Long userId;
        private int attempts;
        private boolean isWinner;
        private int gamesNumber;
        private int friendsNumber;
        private int iconId;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setMoney(long money) {
            this.money = money;
            return this;
        }

        public Builder setRegistrationDate(Long registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setIsFriend(boolean isFriend) {
            this.isFriend = isFriend;
            return this;
        }

        public Builder setWord(String word) {
            this.word = word;
            return this;
        }

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setAttempts(int attempts) {
            this.attempts = attempts;
            return this;
        }

        public Builder setIsWinner(boolean isWinner) {
            this.isWinner = isWinner;
            return this;
        }

        public Builder setGamesNumber(int gamesNumber) {
            this.gamesNumber = gamesNumber;
            return this;
        }

        public Builder setFriendsNumber(int friendsNumber) {
            this.friendsNumber = friendsNumber;
            return this;
        }

        public Builder setIconId(int iconId) {
            this.iconId = iconId;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(id, email, password, money, registrationDate, login,
                    status, isFriend, word, userId, attempts, isWinner, gamesNumber,
                    friendsNumber, iconId);
        }
    }
}
