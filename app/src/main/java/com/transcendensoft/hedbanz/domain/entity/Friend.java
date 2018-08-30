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
 * Entity that describes Friend of some User
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class Friend extends User {
    private boolean isAccepted;
    private boolean isPending;
    private boolean isInvited;
    private boolean isInGame;
    private boolean isSelected;

    private Friend(long id, String email, String password, String confirmPassword,
                   long money, Long registrationDate, String login, PlayerStatus playerStatus,
                   boolean isAccepted, boolean isPending, boolean isFriend,
                   String word, int attempts, boolean isWinner, boolean isInvited, boolean isInGame,
                   int gamesNumber, int friendsNumber) {
        super(id, email, password, confirmPassword, money, registrationDate,
                login, playerStatus, isFriend, word, attempts, isWinner, gamesNumber, friendsNumber);
        this.isAccepted = isAccepted;
        this.isPending = isPending;
        this.isInvited = isInvited;
        this.isInGame = isInGame;
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

    public boolean isInvited() {
        return isInvited;
    }

    public void setInvited(boolean invited) {
        isInvited = invited;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static class Builder {
        private long id;
        private String email;
        private String password;
        private String confirmPassword;
        private long money;
        private Long registrationDate;
        private String login;
        private boolean isAccepted;
        private PlayerStatus playerStatus;
        private boolean isFriend;
        private String word;
        private int attempts;
        private boolean isWinner;
        private boolean isPending;
        private boolean isInvited;
        private boolean isInGame;
        private int gamesNumber;
        private int friendsNumber;

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

        public Builder setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
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

        public Builder setIsAccepted(boolean isAccepted) {
            this.isAccepted = isAccepted;
            return this;
        }

        public Builder setIsPending(boolean isPending) {
            this.isPending = isPending;
            return this;
        }

        public Builder setPlayerStatus(PlayerStatus playerStatus) {
            this.playerStatus = playerStatus;
            return this;
        }

        public Builder setIsFriend(boolean isFriend){
            this.isFriend = isFriend;
            return this;
        }

        public Builder setWord(String word){
            this.word = word;
            return this;
        }

        public Builder setAttempts(int attempts){
            this.attempts = attempts;
            return this;
        }

        public Builder setIsWinner(boolean isWinner){
            this.isWinner = isWinner;
            return this;
        }

        public Builder setIsInvited(boolean isInvited){
            this.isInvited = isInvited;
            return this;
        }

        public Builder setIsInGame(boolean isInGame){
            this.isInGame = isInGame;
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

        public Friend build() {
            return new Friend(id, email, password, confirmPassword, money,
                    registrationDate, login, playerStatus, isAccepted, isPending,
                    isFriend, word, attempts, isWinner, isInvited, isInGame, gamesNumber, friendsNumber);
        }
    }
}
