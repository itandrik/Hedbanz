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

    private Friend(long id, String email, String password, String confirmPassword,
                   long money, Long registrationDate, String login, boolean isAfk,
                   boolean isAccepted, boolean isFriend, String word, int attempts) {
        super(id, email, password, confirmPassword, money, registrationDate,
                login, isAfk, isFriend, word, attempts);
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
        private String confirmPassword;
        private long money;
        private Long registrationDate;
        private String login;
        private boolean isAccepted;
        private boolean isAfk;
        private boolean isFriend;
        private String word;
        private int attempts;
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

        public Builder setIsAfk(boolean isAfk) {
            this.isAfk = isAfk;
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

        public Friend build() {
            return new Friend(id, email, password, confirmPassword, money,
                    registrationDate, login, isAfk, isAccepted, isFriend, word, attempts);
        }
    }
}
