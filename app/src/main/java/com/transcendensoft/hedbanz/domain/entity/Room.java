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

import java.util.List;

/**
 * Room entity that represents room business logic
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class Room {
    private long id;
    private String password;
    private String name;
    private byte maxPlayers;
    private List<User> users;
    private byte currentPlayersNumber;
    private long startDate;
    private long endDate;
    private boolean isWithPassword;

    private Room(long id, String password, byte maxPlayers, List<User> users,
                    String name, byte currentPlayersNumber, long startDate, long endDate,
                 boolean isWithPassword) {
        this.id = id;
        this.password = password;
        this.maxPlayers = maxPlayers;
        this.users = users;
        this.currentPlayersNumber = currentPlayersNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.isWithPassword = isWithPassword;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(byte maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public byte getCurrentPlayersNumber() {
        return currentPlayersNumber;
    }

    public void setCurrentPlayersNumber(byte currentPlayersNumber) {
        this.currentPlayersNumber = currentPlayersNumber;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWithPassword() {
        return isWithPassword;
    }

    public void setWithPassword(boolean withPassword) {
        isWithPassword = withPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        return id == room.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", maxPlayers=" + maxPlayers +
                ", users=" + users +
                ", currentPlayersNumber=" + currentPlayersNumber +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isWithPassword=" + isWithPassword +
                '}';
    }

    public static class Builder {
        private long id;
        private String password;
        private String name;
        private byte maxPlayers;
        private List<User> users;
        private byte currentPlayersNumber;
        private long startDate;
        private long endDate;
        private boolean isWithPassword;

        public Room.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Room.Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Room.Builder setMaxPlayers(byte maxPlayers) {
            this.maxPlayers = maxPlayers;
            return this;
        }

        public Room.Builder setUsers(List<User> users) {
            this.users = users;
            return this;
        }

        public Room.Builder setCurrentPlayersNumber(byte currentPlayersNumber) {
            this.currentPlayersNumber = currentPlayersNumber;
            return this;
        }

        public Room.Builder setStartDate(long startDate) {
            this.startDate = startDate;
            return this;
        }

        public Room.Builder setEndDate(long endDate) {
            this.endDate = endDate;
            return this;
        }

        public Room.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Room.Builder setWithPassword(boolean isWithPassword) {
            this.isWithPassword = isWithPassword;
            return this;
        }

        public Room build() {
            return new Room(id, password, maxPlayers, users, name, currentPlayersNumber, startDate, endDate, isWithPassword);
        }
    }
}
