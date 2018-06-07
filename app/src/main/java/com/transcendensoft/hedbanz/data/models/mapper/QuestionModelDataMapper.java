package com.transcendensoft.hedbanz.data.models.mapper;
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

import com.transcendensoft.hedbanz.data.models.QuestionDTO;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Question;

import java.sql.Timestamp;

import javax.inject.Inject;

/**
 * Mapper class used to transform {@link com.transcendensoft.hedbanz.data.models.QuestionDTO}
 * (in the data layer) to {@link com.transcendensoft.hedbanz.domain.entity.Question} in the
 * domain layer.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class QuestionModelDataMapper {
    private UserModelDataMapper mUserModelDataMapper;

    @Inject
    public QuestionModelDataMapper() {
        mUserModelDataMapper = new UserModelDataMapper();
    }

    public QuestionDTO convert(Question question) {
        QuestionDTO questionDTO = null;
        if (question != null) {
            int voteId = Question.Vote.UNDEFINED.getId();
            if (question.getVote() != null) {
                voteId = question.getVote().getId();
            }
            questionDTO = new QuestionDTO.Builder()
                    .setClientMessageId(question.getClientMessageId())
                    .setCreateDate(question.getCreateDate() != null ? question.getCreateDate().getTime() : 0L)
                    .setId(question.getId())
                    .setSenderId(question.getUserFrom() != null ? question.getUserFrom().getId() : 0L)
                    .setSenderUser(question.getUserFrom() != null ?
                            mUserModelDataMapper.convert(question.getUserFrom()) : null)
                    .setText(question.getMessage())
                    .setType(question.getMessageType() != null ?
                            question.getMessageType().getId() : MessageType.UNDEFINED.getId())
                    .setNoVoters(mUserModelDataMapper.convertToDtoUsers(question.getNoVoters()))
                    .setYesVoters(mUserModelDataMapper.convertToDtoUsers(question.getYesVoters()))
                    .setVoteId(voteId)
                    .setQuestionId(question.getQuestionId())
                    .build();
        }
        return questionDTO;
    }

    public Question convert(QuestionDTO questionDTO) {
        Question question = null;
        if (questionDTO != null) {
            Message message = new Message.Builder()
                    .setClientMessageId(questionDTO.getClientMessageId())
                    .setCreateDate(new Timestamp(questionDTO.getCreateDate()))
                    .setId(questionDTO.getId())
                    .setMessage(questionDTO.getText())
                    .setMessageType(MessageType.getMessageTypeById(questionDTO.getType()))
                    .setUserFrom(questionDTO.getSenderUser() != null ?
                            mUserModelDataMapper.convert(questionDTO.getSenderUser()) : null)
                    .build();

            question = new Question.Builder()
                    .questionId(questionDTO.getQuestionId())
                    .yesVoters(mUserModelDataMapper.convertToUsers(questionDTO.getYesVoters()))
                    .noVoters(mUserModelDataMapper.convertToUsers(questionDTO.getNoVoters()))
                    .vote(Question.Vote.Companion.getVoteById(questionDTO.getVoteId()))
                    .message(message)
                    .build();
        }
        return question;
    }
}
