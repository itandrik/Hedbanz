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

import android.support.annotation.DrawableRes;

/**
 * User entity that represents user business logic
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class User {
    private long id;
    private String email;
    private String password;
    private String confirmPassword;
    private long money;
    private Long registrationDate;
    private String login;
    private boolean isFriend;
    private PlayerStatus playerStatus;
    private String word;
    private int attempts;
    private boolean isWinner;
    private int gamesNumber;
    private int friendsNumber;
    private @DrawableRes int iconId;

    protected User(long id, String email, String password, String confirmPassword,
                   long money, Long registrationDate, String login, PlayerStatus playerStatus,
                   boolean isFriend, String word, int attempts, boolean isWinner,
                   int gamesNumber, int friendsNumber, int iconId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.money = money;
        this.registrationDate = registrationDate;
        this.login = login;
        this.playerStatus = playerStatus;
        this.isFriend = isFriend;
        this.attempts = attempts;
        this.word = word;
        this.isWinner = isWinner;
        this.gamesNumber = gamesNumber;
        this.friendsNumber = friendsNumber;
        this.iconId = iconId;
    }

    public User() {
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public Long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
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

        User user = (User) o;

        return id == user.id;
    }

    public int getGamesNumber() {
        return gamesNumber;
    }

    public void setGamesNumber(int gamesNumber) {
        this.gamesNumber = gamesNumber;
    }

    public int getFriendsNumber() {
        return friendsNumber;
    }

    public void setFriendsNumber(int friendsNumber) {
        this.friendsNumber = friendsNumber;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", money=" + money +
                ", registrationDate=" + registrationDate +
                ", login='" + login + '\'' +
                ", isFriend=" + isFriend +
                ", playerStatus=" + playerStatus +
                ", word='" + word + '\'' +
                ", attempts=" + attempts +
                ", isWinner=" + isWinner +
                ", gamesNumber=" + gamesNumber +
                ", friendsNumber=" + friendsNumber +
                ", iconId=" + iconId +
                '}';
    }

    public static class Builder {
        private long id;
        private String email;
        private String password;
        private String confirmPassword;
        private long money;
        private Long registrationDate;
        private String login;
        private PlayerStatus playerStatus;
        private boolean isFriend;
        private String word;
        private int attempts;
        private boolean isWinner;
        private int gamesNumber;
        private int friendsNumber;
        private int iconId;

        public User.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public User.Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public User.Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public User.Builder setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public User.Builder setMoney(long money) {
            this.money = money;
            return this;
        }

        public User.Builder setRegistrationDate(Long registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public User.Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        public User.Builder setPlayerStatus(PlayerStatus playerStatus) {
            this.playerStatus = playerStatus;
            return this;
        }

        public User.Builder setIsFriend(boolean isFriend){
            this.isFriend = isFriend;
            return this;
        }

        public User.Builder setWord(String word){
            this.word = word;
            return this;
        }

        public User.Builder setAttempts(int attempts){
            this.attempts = attempts;
            return this;
        }

        public User.Builder setIsWinner(boolean isWinner){
            this.isWinner = isWinner;
            return this;
        }

        public User.Builder setGamesNumber(int gamesNumber) {
            this.gamesNumber = gamesNumber;
            return this;
        }

        public User.Builder setFriendsNumber(int friendsNumber) {
            this.friendsNumber = friendsNumber;
            return this;
        }

        public User.Builder setIconId(int iconId) {
            this.iconId = iconId;
            return this;
        }

        public User build() {
            return new User(id, email, password, confirmPassword, money,
                    registrationDate, login, playerStatus, isFriend,
                    word, attempts, isWinner, gamesNumber, friendsNumber, iconId);
        }
    }
}
