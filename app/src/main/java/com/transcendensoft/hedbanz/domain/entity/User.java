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
    private long id;
    private String email;
    private String password;
    private String confirmPassword;
    private long money;
    private Long registrationDate;
    private String login;
    private boolean isFriend;
    private boolean isAFK;
    private String word;
    private int attempts;

    protected User(long id, String email, String password, String confirmPassword,
                   long money, Long registrationDate, String login, boolean isAFK,
                   boolean isFriend, String word, int attempts) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.money = money;
        this.registrationDate = registrationDate;
        this.login = login;
        this.isAFK = isAFK;
        this.isFriend = isFriend;
        this.attempts = attempts;
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

    public boolean isAFK() {
        return isAFK;
    }

    public void setAFK(boolean AFK) {
        isAFK = AFK;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
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
                ", isAFK=" + isAFK +
                ", word='" + word + '\'' +
                ", attempts=" + attempts +
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
        private boolean isAFK;
        private boolean isFriend;
        private String word;
        private int attempts;

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

        public User.Builder setIsAfk(boolean isAfk) {
            this.isAFK = isAfk;
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

        public User build() {
            return new User(id, email, password, confirmPassword, money,
                    registrationDate, login, isAFK, isFriend, word, attempts);
        }
    }
}
