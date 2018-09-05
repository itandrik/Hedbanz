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

import com.transcendensoft.hedbanz.data.models.WordDTO;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Mapper class used to transform {@link com.transcendensoft.hedbanz.data.models.WordDTO}
 * (in the data layer) to {@link com.transcendensoft.hedbanz.domain.entity.Word} in the
 * domain layer.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class WordModelDataMapper {
    private UserModelDataMapper mUserModelDataMapper;

    @Inject
    public WordModelDataMapper() {
        mUserModelDataMapper = new UserModelDataMapper();
    }

    public Word convert(WordDTO wordDTO){
        Word wordResult = null;
        if(wordDTO != null){
            wordResult = new Word.Builder()
                    .setRoomId(wordDTO.getRoomId())
                    .setWord(wordDTO.getWord())
                    .setMessageType(MessageType.getMessageTypeById(wordDTO.getType() != null ? wordDTO.getType() : 0))
                    .setUserFrom(mUserModelDataMapper.convert(wordDTO.getSenderUser()))
                    .setWordReceiverUser(mUserModelDataMapper.convert(wordDTO.getWordReceiverUser()))
                    .build();
        }
        return wordResult;
    }

    public WordDTO convert(Word word){
        WordDTO wordDTOResult = null;
        if(word != null){
            wordDTOResult = new WordDTO.Builder()
                    .setRoomId(word.getRoomId())
                    .setWord(word.getWord())
                    .setType(word.getMessageType() != null ? word.getMessageType().getId() : MessageType.UNDEFINED.getId())
                    .setSenderUser(mUserModelDataMapper.convert(word.getUserFrom()))
                    .setWordReceiverUser(mUserModelDataMapper.convert(word.getWordReceiverUser()))
                    .build();
        }
        return wordDTOResult;
    }


    public List<Word> convertToWords(Collection<WordDTO> wordDTOCollection) {
        if(wordDTOCollection == null){
            return null;
        }

        final List<Word> wordsResult = new ArrayList<>(20);
        for (WordDTO wordDTO : wordDTOCollection) {
            final Word word = convert(wordDTO);
            if (word != null) {
                wordsResult.add(word);
            }
        }
        return wordsResult;
    }

    public List<WordDTO> convertToDtoWords(Collection<Word> wordCollection) {
        if(wordCollection == null) {
            return null;
        }

        final List<WordDTO> wordsResult = new ArrayList<>(20);
        for (Word word : wordCollection) {
            final WordDTO wordDTO = convert(word);
            if (wordDTO != null) {
                wordsResult.add(wordDTO);
            }
        }
        return wordsResult;
    }
}
