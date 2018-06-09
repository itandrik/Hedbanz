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

import android.annotation.SuppressLint;

import com.transcendensoft.hedbanz.data.models.common.ServerError;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.qualifier.SchedulerIO;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Question;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.entity.Word;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.ErrorUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.MessageUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.connect.OnConnectErrorUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.connect.OnConnectTimeoutUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.connect.OnConnectUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.connect.OnDisconnectUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.connect.OnReconnectErrorUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.connect.OnReconnectUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.connect.OnReconnectingUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.guess.GuessWordUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.guess.QuestionAskingUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.guess.QuestionVotingUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.room.RoomInfoUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.room.RoomRestoreUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.typing.StartTypingUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.typing.StopTypingUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.JoinedUserUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.LeftUserUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.UserAfkUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.UserReturnedUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.word.WordSettedUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.word.WordSettingUseCase;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;
import com.transcendensoft.hedbanz.presentation.game.models.RxRoom;
import com.transcendensoft.hedbanz.presentation.game.models.RxUser;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;

/**
 * This class is a Facade of use cases represents game purposes.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GameInteractorFacade {
    private GameDataRepository mRepository;
    private PreferenceManager mPreferenceManger;

    //Use cases
    @Inject OnConnectUseCase mOnConnectUseCase;
    @Inject OnDisconnectUseCase mOnDisconnectUseCase;
    @Inject OnConnectErrorUseCase mOnConnectErrorUseCase;
    @Inject OnConnectTimeoutUseCase mOnConnectTimeoutUseCase;
    @Inject OnReconnectUseCase mOnReconnectUseCase;
    @Inject OnReconnectErrorUseCase mOnReconnectErrorUseCase;
    @Inject OnReconnectingUseCase mOnReconnectingUseCase;

    @Inject UserAfkUseCase mUserAfkUseCase;
    @Inject UserReturnedUseCase mUserReturnedUseCase;
    @Inject JoinedUserUseCase mJoinedUserUseCase;
    @Inject LeftUserUseCase mLeftUserUseCase;

    @Inject MessageUseCase mMessageUseCase;
    @Inject ErrorUseCase mErrorUseCase;

    @Inject StartTypingUseCase mStartTypingUseCase;
    @Inject StopTypingUseCase mStopTypingUseCase;

    @Inject RoomInfoUseCase mRoomInfoUseCase;
    @Inject RoomRestoreUseCase mRoomRestoreUseCase;

    @Inject WordSettedUseCase mWordSettedUseCase;
    @Inject WordSettingUseCase mWordSettingUseCase;

    @Inject GuessWordUseCase mGuessWordUseCase;
    @Inject QuestionAskingUseCase mQuestionAskingUseCase;
    @Inject QuestionVotingUseCase mQuestionVotingUseCase;

    @Inject @SchedulerIO Scheduler mIoScheduler;
    @Inject RxRoom mCurrentRoom;

    @Inject
    public GameInteractorFacade(PreferenceManager preferenceManager,
                                GameDataRepository mRepository) {
        this.mRepository = mRepository;
        this.mPreferenceManger = preferenceManager;
    }

    public void onConnectListener(Consumer<? super String> onNext,
                                  Consumer<? super Throwable> onError) {
        mOnConnectUseCase.execute(null, onNext, onError);
    }

    public void onDisconnectListener(Consumer<? super String> onNext,
                                     Consumer<? super Throwable> onError) {
        mOnDisconnectUseCase.execute(null, onNext, onError);
    }

    public void onConnectErrorListener(Consumer<? super String> onNext,
                                       Consumer<? super Throwable> onError) {
        mOnConnectErrorUseCase.execute(null, onNext, onError);
    }

    public void onConnectTimeoutListener(Consumer<? super String> onNext,
                                         Consumer<? super Throwable> onError) {
        mOnConnectTimeoutUseCase.execute(null, onNext, onError);
    }

    public void onReconnectListener(Consumer<? super String> onNext,
                                    Consumer<? super Throwable> onError) {
        mOnReconnectUseCase.execute(null, onNext, onError);
    }

    public void onReconnectErrorListener(Consumer<? super String> onNext,
                                         Consumer<? super Throwable> onError) {
        mOnReconnectErrorUseCase.execute(null, onNext, onError);
    }

    public void onReconnectingListener(Consumer<? super String> onNext,
                                       Consumer<? super Throwable> onError) {
        mOnReconnectingUseCase.execute(null, onNext, onError);
    }

    public void onUserAfkListener(Consumer<? super User> onNext,
                                  Consumer<? super Throwable> onError) {
        Consumer<? super User> doOnNext = user -> {
            if (mCurrentRoom != null && mCurrentRoom.getRoom().getPlayers() != null) {
                List<User> users = mCurrentRoom.getRoom().getPlayers();
                if (users.contains(user)) {
                    RxUser rxUser = getRxUser(user);
                    if (rxUser != null) {
                        rxUser.setAFK(true);
                    }
                    user.setAFK(true);
                }
            }
        };
        mUserAfkUseCase.execute(null, onNext, onError, doOnNext);
    }

    public void onUserReturnedListener(Consumer<? super User> onNext,
                                       Consumer<? super Throwable> onError) {
        Consumer<? super User> doOnNext = user -> {
            if (mCurrentRoom != null && mCurrentRoom.getRoom().getPlayers() != null) {
                List<User> users = mCurrentRoom.getRoom().getPlayers();
                if (users.contains(user)) {
                    RxUser rxUser = getRxUser(user);
                    if (rxUser != null) {
                        rxUser.setAFK(false);
                    }
                    user.setAFK(false);
                }
            }
        };
        mUserReturnedUseCase.execute(null, onNext, onError, doOnNext);
    }

    public void onJoinedUserListener(Consumer<? super User> onNext,
                                     Consumer<? super Throwable> onError) {
        Consumer<? super User> doOnNext = user -> {
            if (mCurrentRoom != null && mCurrentRoom.getRoom().getPlayers() != null) {
                List<User> users = mCurrentRoom.getRoom().getPlayers();
                if (!users.contains(user)) {
                    mCurrentRoom.addPlayer(user);
                    mCurrentRoom.setCurrentPlayersNumber(
                            (byte) (mCurrentRoom.getRoom().getCurrentPlayersNumber() + 1));
                }
            }
        };
        mJoinedUserUseCase.execute(null, onNext, onError, doOnNext);
    }

    public void onLeftUserListener(Consumer<? super User> onNext,
                                   Consumer<? super Throwable> onError) {
        Consumer<? super User> doOnNext = user -> {
            if (mCurrentRoom != null && mCurrentRoom.getRoom().getPlayers() != null) {
                mCurrentRoom.removePlayer(user);
                mCurrentRoom.setCurrentPlayersNumber(
                        (byte) (mCurrentRoom.getRoom().getCurrentPlayersNumber() - 1));
            }
        };
        mLeftUserUseCase.execute(null, onNext, onError, doOnNext);
    }

    public void onMessageReceivedListener(Consumer<? super Message> onNext,
                                          Consumer<? super Throwable> onError) {
        mMessageUseCase.execute(mCurrentRoom.getRxPlayers(), onNext, onError);
    }

    public void onStartTypingListener(Consumer<? super User> onNext,
                                      Consumer<? super Throwable> onError) {
        mStartTypingUseCase.execute(mCurrentRoom.getRoom().getPlayers(), onNext, onError);
    }

    public void onStopTypingListener(Consumer<? super User> onNext,
                                     Consumer<? super Throwable> onError) {
        mStopTypingUseCase.execute(mCurrentRoom.getRoom().getPlayers(), onNext, onError);
    }

    public void onRoomInfoListener(Consumer<? super Room> onNext,
                                   Consumer<? super Throwable> onError) {
        Consumer<? super Room> doOnNext = room -> {
            this.mCurrentRoom.setRoom(room);
            mPreferenceManger.setCurrentRoomId(room.getId()); // We set that we play in this room
        };
        mRoomInfoUseCase.execute(null, onNext, onError, doOnNext);
    }

    public void onRoomRestoredListener(Consumer<? super Room> onNext,
                                       Consumer<? super Throwable> onError) {
        Consumer<? super Room> doOnNext = room -> {
            this.mCurrentRoom.setRoom(room);
        };
        mRoomRestoreUseCase.execute(null, onNext, onError, doOnNext);
    }

    public void onErrorListener(Consumer<? super ServerError> onNext,
                                Consumer<? super Throwable> onError) {
        mErrorUseCase.execute(null, onNext, onError);
    }

    public void onWordSettedListener(Consumer<? super Word> onNext,
                                     Consumer<? super Throwable> onError) {
        Consumer<? super Word> doOnNext = word -> {
            if (mCurrentRoom != null && mCurrentRoom.getRoom().getPlayers() != null) {
                RxUser rxUser = getRxUser(word.getWordReceiverUser());
                if (rxUser != null) {
                    rxUser.setWord(word.getWord());
                }
            }
        };
        mWordSettedUseCase.execute(mCurrentRoom.getRoom().getPlayers(), onNext, onError, doOnNext);
    }

    public void onWordSettingListener(Consumer<? super User> onNext,
                                      Consumer<? super Throwable> onError) {
        mWordSettingUseCase.execute(mCurrentRoom.getRoom().getPlayers(), onNext, onError);
    }

    public void onWordGuessingListener(Consumer<? super User> onNext,
                                       Consumer<? super Throwable> onError) {
        mGuessWordUseCase.execute(null, onNext, onError);
    }

    public void onQuestionAskingListener(Consumer<? super Question> onNext,
                                         Consumer<? super Throwable> onError) {
        mQuestionAskingUseCase.execute(null, onNext, onError);
    }

    public void onQuestionVotingListener(Consumer<? super Question> onNext,
                                         Consumer<? super Throwable> onError) {
        mQuestionVotingUseCase.execute(null, onNext, onError);
    }

    public Question guessWord(String word) {
        User currentUser = mPreferenceManger.getUser();

        int clientMessageId = Arrays.hashCode(new long[]{
                System.currentTimeMillis(), currentUser.getId()});

        Question question = new Question();
        question.setMessageType(MessageType.GUESS_WORD_THIS_USER);
        question.setMessage(word);
        question.setUserFrom(currentUser);
        question.setClientMessageId(clientMessageId);

        mRepository.guessWord(question);

        return question;
    }

    public void voteForQuestion(boolean isYes, Long questionId) {
        User currentUser = mPreferenceManger.getUser();

        Question question = new Question();
        question.setMessageType(MessageType.VOTING_FOR_QUESTION);
        question.setQuestionId(questionId);

        if (isYes) {
            question.setVote(Question.Vote.YES);
        } else {
            question.setVote(Question.Vote.NO);
        }

        mRepository.voteForQuestion(question);
    }

    public void setWordToUser(Word word) {
        mRepository.setWord(word);
    }

    public void connectSocketToServer(long roomId) {
        User currentUser = mPreferenceManger.getUser();
        mRepository.connect(currentUser.getId(), roomId);
    }

    public void joinToRoom(String password) {
        mRepository.joinToRoom(password);
    }

    public void restoreRoom() {
        mRepository.sendRoomRestore();
    }

    public void startTyping() {
        mRepository.startTyping();
    }

    public void stopTyping() {
        mRepository.stopTyping();
    }

    public Message sendMessage(String text) {
        User currentUser = mPreferenceManger.getUser();

        int clientMessageId = Arrays.hashCode(new long[]{
                System.currentTimeMillis(), currentUser.getId()});

        Message message = new Message.Builder()
                .setMessageType(MessageType.SIMPLE_MESSAGE_THIS_USER)
                .setMessage(text)
                .setUserFrom(currentUser)
                .setClientMessageId(clientMessageId)

                .build();

        mRepository.sendMessage(message);
        return message;
    }

    @SuppressLint("CheckResult")
    public void destroy() {
        mOnConnectUseCase.dispose();
        mOnDisconnectUseCase.dispose();
        mOnConnectErrorUseCase.dispose();
        mOnConnectTimeoutUseCase.dispose();
        mOnReconnectUseCase.dispose();
        mOnReconnectErrorUseCase.dispose();
        mOnReconnectingUseCase.dispose();

        mUserReturnedUseCase.dispose();
        mUserAfkUseCase.dispose();
        mJoinedUserUseCase.dispose();
        mLeftUserUseCase.dispose();
        mMessageUseCase.dispose();
        mStartTypingUseCase.dispose();
        mStopTypingUseCase.dispose();
        mRoomInfoUseCase.dispose();
        mRoomRestoreUseCase.dispose();
        mErrorUseCase.dispose();
        mWordSettingUseCase.dispose();
        mWordSettedUseCase.dispose();

        mGuessWordUseCase.dispose();
        mQuestionAskingUseCase.dispose();
        mQuestionVotingUseCase.dispose();

        mRepository.disconnectFromRoom();
        mPreferenceManger.setCurrentRoomId(-1); //We leave from current game
        mRepository.disconnect();
    }

    @Nullable
    private RxUser getRxUser(User user) {
        for (RxUser rxUser : mCurrentRoom.getRxPlayers()) {
            if (rxUser.getUser().equals(user)) {
                return rxUser;
            }
        }
        return null;
    }
}
