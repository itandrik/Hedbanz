package com.transcendensoft.hedbanz.data.entity;
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
 * Entity for room filtering.
 * In order to filter rooms we need to have
 * minPlayers, maxPlayers quantities, isPrivate room and
 * room name.
 *
 * Field room name can be as room name and room id
 * (room id - if it starts from '#')
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class RoomFilter {
    @SerializedName("roomName")
    @Expose
    private String roomName;
    @SerializedName("maxPlayers")
    @Expose
    private Byte maxPlayers;
    @SerializedName("minPlayers")
    @Expose
    private Byte minPlayers;
    @SerializedName("isPrivate")
    @Expose
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

        public Builder setRoomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder setMaxPlayers(Byte maxPlayers) {
            this.maxPlayers = maxPlayers;
            return this;
        }

        public Builder setMinPlayers(Byte minPlayers) {
            this.minPlayers = minPlayers;
            return this;
        }

        public Builder setIsPrivate(Boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public RoomFilter build() {
            return new RoomFilter(roomName, maxPlayers, minPlayers, isPrivate);
        }
    }
}
