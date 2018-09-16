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
 * DTO for message_received entity.
 * All message_received properties received from server described here
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class MessageDTO {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("clientMessageId")
    @Expose
    private Long clientMessageId;
    @SerializedName("senderId")
    @Expose
    private Long senderId;
    @SerializedName("roomId")
    @Expose
    private Long roomId;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("createDate")
    @Expose
    private Long createDate;
    @SerializedName("senderUser")
    @Expose
    private UserDTO senderUser;
    private String securityToken;

    MessageDTO(Long id, Long senderId, Long roomId, String text, Integer type, Long createDate, Long clientMessageId, UserDTO senderUser) {
        this.id = id;
        this.senderId = senderId;
        this.roomId = roomId;
        this.text = text;
        this.type = type;
        this.createDate = createDate;
        this.clientMessageId = clientMessageId;
        this.senderUser = senderUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getClientMessageId() {
        return clientMessageId;
    }

    public void setClientMessageId(Long clientMessageId) {
        this.clientMessageId = clientMessageId;
    }

    public UserDTO getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(UserDTO senderUser) {
        this.senderUser = senderUser;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDTO that = (MessageDTO) o;

        return clientMessageId != null ? clientMessageId.equals(that.clientMessageId) : that.clientMessageId == null;
    }

    @Override
    public int hashCode() {
        return (int) (clientMessageId ^ (clientMessageId >>> 32));
    }

    public static class Builder {
        private Long id;
        private Long senderId;
        private Long roomId;
        private String text;
        private Integer type;
        private Long createDate;
        private Long clientMessageId;
        private UserDTO senderUser;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setSenderId(Long senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder setRoomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setType(Integer type) {
            this.type = type;
            return this;
        }

        public Builder setCreateDate(Long createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setClientMessageId(Long clientMessageId) {
            this.clientMessageId = clientMessageId;
            return this;
        }

        public Builder setSenderUser(UserDTO senderUser) {
            this.senderUser = senderUser;
            return this;
        }

        public MessageDTO build() {
            return new MessageDTO(id, senderId, roomId, text, type, createDate, clientMessageId, senderUser);
        }
    }
}
