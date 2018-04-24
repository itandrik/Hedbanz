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
 *         Developed by <u>Transcendensoft</u>
 */
public class UserDTO {
    public static final String USER_ID_KEY = "userId";
    public static final String SENDER_ID_KEY = "senderId";

    @SerializedName("id")
    @Expose
    private long id;
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
    @SerializedName("isAFK")
    @Expose
    private boolean isAfk;

    protected UserDTO(long id, String email, String password, long money, Long registrationDate, String login, boolean isAfk) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = money;
        this.registrationDate = registrationDate;
        this.login = login;
        this.isAfk = isAfk;
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

    public boolean isAfk() {
        return isAfk;
    }

    public void setAfk(boolean afk) {
        isAfk = afk;
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
        private boolean isAfk;

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

        public Builder setIsAfk(boolean isAfk) {
            this.isAfk = isAfk;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(id, email, password, money, registrationDate, login, isAfk);
        }
    }
}
