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
 * Room filter entity that represents room filtering logic
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class RoomFilter {
    private String roomName;
    private Byte maxPlayers;
    private Byte minPlayers;
    private Boolean isPrivate;

    private RoomFilter(String roomName, Byte maxPlayers, Byte minPlayers, Boolean isPrivate) {
        this.roomName = roomName;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.isPrivate = isPrivate;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Byte getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Byte maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Byte getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(Byte minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public static class Builder {
        private String roomName;
        private Byte maxPlayers;
        private Byte minPlayers;
        private Boolean isPrivate;

        public RoomFilter.Builder setRoomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public RoomFilter.Builder setMaxPlayers(Byte maxPlayers) {
            this.maxPlayers = maxPlayers;
            return this;
        }

        public RoomFilter.Builder setMinPlayers(Byte minPlayers) {
            this.minPlayers = minPlayers;
            return this;
        }

        public RoomFilter.Builder setIsPrivate(Boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public RoomFilter build() {
            return new RoomFilter(roomName, maxPlayers, minPlayers, isPrivate);
        }
    }
}
