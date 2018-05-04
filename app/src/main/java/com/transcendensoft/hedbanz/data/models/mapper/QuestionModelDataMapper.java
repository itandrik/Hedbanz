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
            questionDTO = new QuestionDTO.Builder()
                    .setClientMessageId(question.getClientMessageId())
                    .setCreateDate(question.getCreateDate() != null ? question.getCreateDate().getTime() : 0L)
                    .setId(question.getId())
                    .setSenderId(question.getUserFrom().getId())
                    .setSenderUser(mUserModelDataMapper.convert(question.getUserFrom()))
                    .setText(question.getMessage())
                    .setType(question.getMessageType().getId())
                    .setNoNumber(question.getNoNumber())
                    .setYesNumber(question.getYesNumber())
                    .setQuestionId(question.getQuestionId())
                    .build();
        }
        return questionDTO;
    }

    public Question convert(QuestionDTO questionDTO) {
        Question question = null;
        if (questionDTO != null) {
            question = new Question.Builder()
                    .setClientMessageId(questionDTO.getClientMessageId())
                    .setCreateDate(new Timestamp(questionDTO.getCreateDate()))
                    .setId(questionDTO.getId())
                    .setMessage(questionDTO.getText())
                    .setMessageType(MessageType.getMessageTypeById(questionDTO.getType()))
                    .setNoNumber(questionDTO.getNoNumber())
                    .setQuestionId(questionDTO.getQuestionId())
                    .setUserFrom(mUserModelDataMapper.convert(questionDTO.getSenderUser()))
                    .setYesNumber(questionDTO.getYesNumber())
                    .build();
        }
        return question;
    }
}
