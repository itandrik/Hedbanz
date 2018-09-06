package com.transcendensoft.hedbanz.domain.interactor.game;
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

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.PaginationState;
import com.transcendensoft.hedbanz.domain.PaginationUseCase;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.PlayerGuessing;
import com.transcendensoft.hedbanz.domain.entity.Question;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.entity.Word;
import com.transcendensoft.hedbanz.domain.repository.MessagesDataRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for retrieving data related to a specific list of
 * {@link com.transcendensoft.hedbanz.domain.entity.Message}.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GetMessagesInteractor extends PaginationUseCase<Message, GetMessagesInteractor.Param, Void> {
    private MessagesDataRepository mDataRepository;
    private PreferenceManager mPreferenceManager;

    @Inject
    public GetMessagesInteractor(ObservableTransformer mSchedulersTransformer,
                                 CompositeDisposable mCompositeDisposable,
                                 MessagesDataRepository mDataRepository,
                                 PreferenceManager mPreferenceManager) {
        super(mSchedulersTransformer, mCompositeDisposable);
        this.mDataRepository = mDataRepository;
        this.mPreferenceManager = mPreferenceManager;
    }

    @Override
    protected Observable<PaginationState<Message>> buildUseCaseObservable(GetMessagesInteractor.Param param) {
        return mDataRepository.getMessages(param.roomId, mCurrentPage, DataPolicy.API)
                .map(messages -> mapSetMessageUserAndType(messages, param.playersCount))
                .flatMap(this::convertEntitiesToPagingResult)
                .onErrorReturn(this::mapPaginationStateBasedOnError);
    }

    @NonNull
    private List<Message> mapSetMessageUserAndType(List<Message> messages, int playersCount) {
        User currentUser = mPreferenceManager.getUser();
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            if (message.getMessageType() == MessageType.SIMPLE_MESSAGE) {
                if (currentUser.equals(message.getUserFrom())) {
                    message.setMessageType(MessageType.SIMPLE_MESSAGE_THIS_USER);
                } else {
                    message.setMessageType(MessageType.SIMPLE_MESSAGE_OTHER_USER);
                }
                message.setFinished(true);
                message.setLoading(false);
            } else if (message.getMessageType() == MessageType.GUESS_WORD_THIS_USER && message instanceof Question) {
                Question question = (Question) message;

                // Question with guessing
                PlayerGuessing playerGuessing = new PlayerGuessing.Builder()
                        .player(question.getUserFrom())
                        .attempts(question.getAttempt() != null ? question.getAttempt() : -1)
                        .questionId(question.getQuestionId())
                        .build();
                playerGuessing.setUserFrom(question.getUserFrom());
                playerGuessing.setMessage(question.getMessage());
                if (currentUser.equals(message.getUserFrom())) {
                    playerGuessing.setMessageType(MessageType.GUESS_WORD_THIS_USER);
                } else {
                    playerGuessing.setMessageType(MessageType.GUESS_WORD_OTHER_USER);
                }
                if (!TextUtils.isEmpty(question.getMessage())) {
                    playerGuessing.setFinished(true);
                    playerGuessing.setLoading(false);
                } else {
                    playerGuessing.setFinished(false);
                    playerGuessing.setLoading(false);
                }

                //Question with asking\voting
                Question askingQuestion = null;
                if (!TextUtils.isEmpty(question.getMessage())) {
                    askingQuestion = question.clone();
                    if (currentUser.equals(message.getUserFrom())) {
                        askingQuestion.setMessageType(MessageType.ASKING_QUESTION_THIS_USER);
                    } else {
                        askingQuestion.setMessageType(MessageType.ASKING_QUESTION_OTHER_USER);
                    }
                    askingQuestion.setAllUsersCount(playersCount - 1);
                    askingQuestion.setFinished(true);
                    askingQuestion.setLoading(false);
                }

                messages.remove(i);
                if(askingQuestion != null) {
                    messages.add(i, askingQuestion);
                    messages.add(i, playerGuessing);
                    i++;
                } else {
                    messages.add(i, playerGuessing);
                }
            } else if(message.getMessageType() == MessageType.WORD_SETTING){
                Word word = (Word) message;

                messages.remove(i);
                if (currentUser.equals(word.getUserFrom())) {
                    word.setMessageType(MessageType.WORD_SETTING);

                    if(TextUtils.isEmpty(word.getWord())){
                        word.setLoading(false);
                        word.setFinished(false);
                        messages.add(i, word);
                    } else {
                        Word wordSetted = new Word.Builder()
                                .setWord(word.getWord())
                                .setWordReceiverUser(word.getWordReceiverUser())
                                .setMessageType(MessageType.WORD_SETTED)
                                .setUserFrom(word.getUserFrom())
                                .build();
                        word.setLoading(false);
                        word.setFinished(true);

                        messages.add(i, wordSetted);
                        messages.add(i, word);
                        i++;
                    }
                } else {
                    if(!TextUtils.isEmpty(word.getWord())){
                        word.setMessageType(MessageType.WORD_SETTED);
                        messages.add(i, word);
                    } else {
                        i--;
                    }
                }
            } else if(message.getMessageType() == MessageType.USER_WINS_THIS){
                if(!mPreferenceManager.getUser().equals(message.getUserFrom())){
                    message.setMessageType(MessageType.USER_WINS_OTHER);
                }
            }
        }
        return messages;
    }

    public static class Param{
        private Long roomId;
        private int playersCount;

        public Param(Long roomId, int playersCount) {
            this.roomId = roomId;
            this.playersCount = playersCount;
        }

        public Long getRoomId() {
            return roomId;
        }

        public void setRoomId(Long roomId) {
            this.roomId = roomId;
        }

        public int getPlayersCount() {
            return playersCount;
        }

        public void setPlayersCount(int playersCount) {
            this.playersCount = playersCount;
        }
    }
}
