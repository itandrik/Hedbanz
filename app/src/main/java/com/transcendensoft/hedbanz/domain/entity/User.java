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
 * User entity that represents user business logic
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class User {
    private Long id;
    private String email;
    private String password;
    private String confirmPassword;
    private Long money;
    private Long registrationDate;
    private String login;
    private boolean isFriend;
    private PlayerStatus playerStatus;
    private String word;
    private Integer attempts;
    private boolean isWinner;
    private Integer gamesNumber;
    private Integer friendsNumber;
    private UserIcon iconId;
    private boolean isWordVisible;

    protected User(Long id, String email, String password, String confirmPassword,
                   Long money, Long registrationDate, String login, PlayerStatus playerStatus,
                   boolean isFriend, String word, Integer attempts, boolean isWinner,
                   Integer gamesNumber, Integer friendsNumber, UserIcon iconId) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
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

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public UserIcon getIconId() {
        return iconId;
    }

    public void setIconId(UserIcon iconId) {
        this.iconId = iconId;
    }

    public boolean isWordVisible() {
        return isWordVisible;
    }

    public void setWordVisible(boolean wordVisible) {
        isWordVisible = wordVisible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;

    }

    public Integer getGamesNumber() {
        return gamesNumber;
    }

    public void setGamesNumber(Integer gamesNumber) {
        this.gamesNumber = gamesNumber;
    }

    public Integer getFriendsNumber() {
        return friendsNumber;
    }

    public void setFriendsNumber(Integer friendsNumber) {
        this.friendsNumber = friendsNumber;
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
        private Long id;
        private String email;
        private String password;
        private String confirmPassword;
        private Long money;
        private Long registrationDate;
        private String login;
        private PlayerStatus playerStatus;
        private boolean isFriend;
        private String word;
        private Integer attempts;
        private boolean isWinner;
        private Integer gamesNumber;
        private Integer friendsNumber;
        private UserIcon iconId;

        public User.Builder setId(Long id) {
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

        public User.Builder setMoney(Long money) {
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

        public User.Builder setAttempts(Integer attempts){
            this.attempts = attempts;
            return this;
        }

        public User.Builder setIsWinner(boolean isWinner){
            this.isWinner = isWinner;
            return this;
        }

        public User.Builder setGamesNumber(Integer gamesNumber) {
            this.gamesNumber = gamesNumber;
            return this;
        }

        public User.Builder setFriendsNumber(Integer friendsNumber) {
            this.friendsNumber = friendsNumber;
            return this;
        }

        public User.Builder setIconId(UserIcon iconId) {
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
