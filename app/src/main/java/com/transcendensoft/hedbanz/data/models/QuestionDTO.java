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

import java.util.List;

/**
 * DTO for question entity.
 * All question properties received from server described here
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class QuestionDTO extends MessageDTO{
    @SerializedName("questionId")
    @Expose
    private long questionId;
    @SerializedName("yesVoters")
    @Expose
    private List<UserDTO> yesVoters;
    @SerializedName("noVoters")
    @Expose
    private List<UserDTO> noVoters;
    @SerializedName("vote")
    @Expose
    private int voteId;

    public QuestionDTO(long id, long senderId, long roomId, String text, int type,
                       Long createDate, long clientMessageId, UserDTO senderUser,
                       long questionId, List<UserDTO> yesVoters, List<UserDTO> noVoters, int voteId) {
        super(id, senderId, roomId, text, type, createDate, clientMessageId, senderUser);
        this.questionId = questionId;
        this.yesVoters = yesVoters;
        this.noVoters = noVoters;
        this.voteId = voteId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public List<UserDTO> getYesVoters() {
        return yesVoters;
    }

    public void setYesVoters(List<UserDTO> yesVoters) {
        this.yesVoters = yesVoters;
    }

    public List<UserDTO> getNoVoters() {
        return noVoters;
    }

    public void setNoVoters(List<UserDTO> noVoters) {
        this.noVoters = noVoters;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public static class Builder {
        private long id;
        private long senderId;
        private long roomId;
        private String text;
        private int type;
        private Long createDate;
        private long clientMessageId;
        private UserDTO senderUser;
        private long questionId;
        private List<UserDTO> yesVoters;
        private List<UserDTO> noVoters;
        private int voteId;

        public QuestionDTO.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public QuestionDTO.Builder setSenderId(long senderId) {
            this.senderId = senderId;
            return this;
        }

        public QuestionDTO.Builder setRoomId(long roomId) {
            this.roomId = roomId;
            return this;
        }

        public QuestionDTO.Builder setText(String text) {
            this.text = text;
            return this;
        }

        public QuestionDTO.Builder setType(int type) {
            this.type = type;
            return this;
        }

        public QuestionDTO.Builder setCreateDate(Long createDate) {
            this.createDate = createDate;
            return this;
        }

        public QuestionDTO.Builder setClientMessageId(long clientMessageId) {
            this.clientMessageId = clientMessageId;
            return this;
        }

        public QuestionDTO.Builder setSenderUser(UserDTO senderUser) {
            this.senderUser = senderUser;
            return this;
        }

        public QuestionDTO.Builder setQuestionId(long questionId) {
            this.questionId = questionId;
            return this;
        }

        public QuestionDTO.Builder setYesVoters(List<UserDTO> yesVoters) {
            this.yesVoters = yesVoters;
            return this;
        }

        public QuestionDTO.Builder setNoVoters(List<UserDTO> noVoters) {
            this.noVoters = noVoters;
            return this;
        }

        public QuestionDTO.Builder setVoteId(int vote) {
            this.voteId = vote;
            return this;
        }

        public QuestionDTO build() {
            return new QuestionDTO(id, senderId, roomId, text, type, createDate,
                    clientMessageId, senderUser, questionId, yesVoters, noVoters, voteId);
        }
    }
}
