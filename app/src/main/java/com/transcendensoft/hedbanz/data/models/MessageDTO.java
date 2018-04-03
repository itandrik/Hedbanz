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

import java.sql.Timestamp;

/**
 * DTO for message entity.
 * All message properties received from server described here
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class MessageDTO {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("senderId")
    @Expose
    private long senderId;
    @SerializedName("roomId")
    @Expose
    private long roomId;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("createDate")
    @Expose
    private Timestamp createDate;

    MessageDTO(long id, long senderId, long roomId, String text, int type, Timestamp createDate) {
        this.id = id;
        this.senderId = senderId;
        this.roomId = roomId;
        this.text = text;
        this.type = type;
        this.createDate = createDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDTO that = (MessageDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public static class Builder {
        private long id;
        private long senderId;
        private long roomId;
        private String text;
        private int type;
        private Timestamp createDate;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setSenderId(long senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder setRoomId(long roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setCreateDate(Timestamp createDate) {
            this.createDate = createDate;
            return this;
        }

        public MessageDTO build() {
            return new MessageDTO(id, senderId, roomId, text, type, createDate);
        }
    }
}
