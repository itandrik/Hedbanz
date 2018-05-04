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

import java.sql.Timestamp;

/**
 * Entity for submitting and showing questions during game
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class Question extends Message{
    private long questionId;
    private int yesNumber;
    private int noNumber;

    private Question(long questionId, int yesNumber, int noNumber) {
        this.questionId = questionId;
        this.yesNumber = yesNumber;
        this.noNumber = noNumber;
    }

    public Question(long id, String message, User userFrom, MessageType messageType,
                    Timestamp timestamp, long clientMessageId, long questionId,
                    int yesNumber, int noNumber) {
        super(id, message, userFrom, messageType, timestamp, clientMessageId);
        this.questionId = questionId;
        this.yesNumber = yesNumber;
        this.noNumber = noNumber;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public int getYesNumber() {
        return yesNumber;
    }

    public void setYesNumber(int yesNumber) {
        this.yesNumber = yesNumber;
    }

    public int getNoNumber() {
        return noNumber;
    }

    public void setNoNumber(int noNumber) {
        this.noNumber = noNumber;
    }

    public static class Builder {
        private long id;
        private String message;
        private User userFrom;
        private MessageType messageType;
        private Timestamp createDate;
        private long clientMessageId;
        private long questionId;
        private int yesNumber;
        private int noNumber;

        public Question.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Question.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Question.Builder setUserFrom(User userFrom) {
            this.userFrom = userFrom;
            return this;
        }

        public Question.Builder setMessageType(MessageType messageType) {
            this.messageType = messageType;
            return this;
        }

        public Question.Builder setCreateDate(Timestamp createDate) {
            this.createDate = createDate;
            return this;
        }

        public Question.Builder setClientMessageId(long clientMessageId) {
            this.clientMessageId = clientMessageId;
            return this;
        }

        public Question.Builder setQuestionId(long questionId) {
            this.questionId = questionId;
            return this;
        }

        public Question.Builder setYesNumber(int yesNumber) {
            this.yesNumber = yesNumber;
            return this;
        }

        public Question.Builder setNoNumber(int noNumber) {
            this.noNumber = noNumber;
            return this;
        }

        public Question build() {
            return new Question(id, message, userFrom, messageType, createDate,
                    clientMessageId, questionId, yesNumber, noNumber);
        }
    }
}
