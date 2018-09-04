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
 * Developed by <u>Transcendensoft</u>
 */
public class QuestionDTO extends MessageDTO {
    @SerializedName("questionId")
    @Expose
    private Long questionId;
    @SerializedName("yesVoters")
    @Expose
    private List<UserDTO> yesVoters;
    @SerializedName("noVoters")
    @Expose
    private List<UserDTO> noVoters;
    @SerializedName("winVoters")
    @Expose
    private List<UserDTO> winVoters;
    @SerializedName("vote")
    @Expose
    private Integer voteId;
    @SerializedName("attempt")
    @Expose
    private Integer attempt;

    public QuestionDTO(Long id, Long senderId, Long roomId, String text, Integer type,
                       Long createDate, Long clientMessageId, UserDTO senderUser,
                       Long questionId, List<UserDTO> yesVoters, List<UserDTO> noVoters,
                       List<UserDTO> winVoters, Integer voteId, Integer attempt) {
        super(id, senderId, roomId, text, type, createDate, clientMessageId, senderUser);
        this.questionId = questionId;
        this.yesVoters = yesVoters;
        this.noVoters = noVoters;
        this.winVoters = winVoters;
        this.voteId = voteId;
        this.attempt = attempt;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
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

    public List<UserDTO> getWinVoters() {
        return winVoters;
    }

    public void setWinVoters(List<UserDTO> winVoters) {
        this.winVoters = winVoters;
    }

    public Integer getVoteId() {
        return voteId;
    }

    public void setVoteId(Integer voteId) {
        this.voteId = voteId;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
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
        private Long questionId;
        private List<UserDTO> yesVoters;
        private List<UserDTO> noVoters;
        private List<UserDTO> winVoters;
        private Integer attempt;
        private Integer voteId;

        public QuestionDTO.Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public QuestionDTO.Builder setSenderId(Long senderId) {
            this.senderId = senderId;
            return this;
        }

        public QuestionDTO.Builder setRoomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public QuestionDTO.Builder setText(String text) {
            this.text = text;
            return this;
        }

        public QuestionDTO.Builder setType(Integer type) {
            this.type = type;
            return this;
        }

        public QuestionDTO.Builder setCreateDate(Long createDate) {
            this.createDate = createDate;
            return this;
        }

        public QuestionDTO.Builder setClientMessageId(Long clientMessageId) {
            this.clientMessageId = clientMessageId;
            return this;
        }

        public QuestionDTO.Builder setSenderUser(UserDTO senderUser) {
            this.senderUser = senderUser;
            return this;
        }

        public QuestionDTO.Builder setQuestionId(Long questionId) {
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

        public QuestionDTO.Builder setWinVoters(List<UserDTO> winVoters) {
            this.winVoters = winVoters;
            return this;
        }

        public QuestionDTO.Builder setVoteId(Integer vote) {
            this.voteId = vote;
            return this;
        }

        public QuestionDTO.Builder setAttempt(Integer attempt) {
            this.attempt = attempt;
            return this;
        }

        public QuestionDTO build() {
            return new QuestionDTO(id, senderId, roomId, text, type, createDate,
                    clientMessageId, senderUser, questionId, yesVoters, noVoters,
                    winVoters, voteId, attempt);
        }
    }
}
