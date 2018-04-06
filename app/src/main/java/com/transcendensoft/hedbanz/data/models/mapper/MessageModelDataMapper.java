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

import com.transcendensoft.hedbanz.data.models.MessageDTO;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Mapper class used to transform {@link com.transcendensoft.hedbanz.data.models.MessageDTO}
 * (in the data layer) to {@link com.transcendensoft.hedbanz.domain.entity.Message} in the
 * domain layer.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class MessageModelDataMapper {
    @Inject
    public MessageModelDataMapper() {
    }

    public Message convert(MessageDTO messageDTO){
        Message messageResult = null;
        if(messageDTO != null){
            messageResult = new Message.Builder()
                    .setId(messageDTO.getId())
                    .setMessage(messageDTO.getText())
                    .setMessageType(MessageType.getMessageTypeById(messageDTO.getType()))
                    .setCreateDate(new Timestamp(messageDTO.getCreateDate()))
                    .setUserFrom(new User.Builder().setId(messageDTO.getSenderId()).build())
                    .build();
        }
        return messageResult;
    }

    public List<Message> convert(Collection<MessageDTO> messageDTOCollection) {
        final List<Message> messagesResult = new ArrayList<>(20);
        for (MessageDTO messageDTO : messageDTOCollection) {
            final Message message = convert(messageDTO);
            if (message != null) {
                messagesResult.add(message);
            }
        }
        return messagesResult;
    }
}
