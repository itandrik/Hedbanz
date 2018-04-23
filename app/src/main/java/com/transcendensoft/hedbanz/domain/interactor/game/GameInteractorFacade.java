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

import com.google.gson.Gson;
import com.transcendensoft.hedbanz.data.models.common.ServerError;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.data.repository.GameDataRepositoryImpl;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.entity.Word;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.ErrorUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.RoomRestoreUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.socket.OnReconnectErrorUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.JoinedUserUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.LeftUserUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.MessageUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.socket.OnConnectErrorUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.socket.OnConnectTimeoutUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.socket.OnConnectUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.socket.OnDisconnectUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.socket.OnReconnectUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.RoomInfoUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.typing.StartTypingUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.typing.StopTypingUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.UserAfkUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.UserReturnedUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.word.WordSettedUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.word.WordSettingUseCase;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
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

    @Inject UserAfkUseCase mUserAfkUseCase;
    @Inject UserReturnedUseCase mUserReturnedUseCase;
    @Inject JoinedUserUseCase mJoinedUserUseCase;
    @Inject LeftUserUseCase mLeftUserUseCase;
    @Inject MessageUseCase mMessageUseCase;
    @Inject StartTypingUseCase mStartTypingUseCase;
    @Inject StopTypingUseCase mStopTypingUseCase;
    @Inject RoomInfoUseCase mRoomInfoUseCase;
    @Inject RoomRestoreUseCase mRoomRestoreUseCase;
    @Inject ErrorUseCase mErrorUseCase;
    @Inject WordSettedUseCase mWordSettedUseCase;
    @Inject WordSettingUseCase mWordSettingUseCase;

    private Room mCurrentRoom;

    @Inject
    public GameInteractorFacade(PreferenceManager preferenceManager,
                                GameDataRepositoryImpl mRepository,
                                CompositeDisposable compositeDisposable,
                                ObservableTransformer mObservableTransformer,
                                Gson gson) {
        this.mRepository = mRepository;
        this.mPreferenceManger = preferenceManager;

        initUseCases(mRepository, compositeDisposable, mObservableTransformer, gson);
    }

    private void initUseCases(GameDataRepositoryImpl mRepository,
                              CompositeDisposable compositeDisposable,
                              ObservableTransformer mObservableTransformer,
                              Gson gson) {
       /* mOnConnectUseCase = new OnConnectUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mOnDisconnectUseCase = new OnDisconnectUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mOnConnectErrorUseCase = new OnConnectErrorUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mOnConnectTimeoutUseCase = new OnConnectTimeoutUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mOnReconnectUseCase = new OnReconnectUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mJoinedUserUseCase = new JoinedUserUseCase(
                mObservableTransformer, compositeDisposable, mRepository, gson, mPreferenceManger);
        mLeftUserUseCase = new LeftUserUseCase(
                mObservableTransformer, compositeDisposable, mRepository, gson);
        mMessageUseCase = new MessageUseCase(
                mObservableTransformer, compositeDisposable,
                mRepository, mPreferenceManger);
        mStartTypingUseCase = new StartTypingUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mStopTypingUseCase = new StopTypingUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mRoomInfoUseCase = new RoomInfoUseCase(
                mObservableTransformer, compositeDisposable, mRepository, gson);
        mErrorUseCase = new ErrorUseCase(
                mObservableTransformer, compositeDisposable, mRepository, gson);
        mWordSettedUseCase = new WordSettedUseCase(mObservableTransformer,
                compositeDisposable, mRepository);
        mWordSettingUseCase = new WordSettingUseCase(mObservableTransformer,
                compositeDisposable, mRepository);
        mUserAfkUseCase = new UserAfkUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mUserReturnedUseCase = new UserReturnedUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mRoomRestoreUseCase = new RoomRestoreUseCase(
                mObservableTransformer, compositeDisposable, mRepository);*/
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

    public void onUserAfkListener(Consumer<? super User> onNext,
                                    Consumer<? super Throwable> onError) {
        mUserAfkUseCase.execute(null, onNext, onError);
    }

    public void onUserReturnedListener(Consumer<? super User> onNext,
                                  Consumer<? super Throwable> onError) {
        mUserReturnedUseCase.execute(null, onNext, onError);
    }

    public void onJoinedUserListener(Consumer<? super User> onNext,
                                     Consumer<? super Throwable> onError) {
        Consumer<? super User> doOnNext = user -> {
            if (mCurrentRoom != null && mCurrentRoom.getUsers() != null) {
                List<User> users = mCurrentRoom.getUsers();
                if (!users.contains(user)) {
                    users.add(user);
                }
            }
        };
        mJoinedUserUseCase.execute(null, onNext, onError, doOnNext);
    }

    public void onLeftUserListener(Consumer<? super User> onNext,
                                   Consumer<? super Throwable> onError) {
        Consumer<? super User> doOnNext = user -> {
            if (mCurrentRoom != null && mCurrentRoom.getUsers() != null) {
                mCurrentRoom.getUsers().remove(user);
            }
        };
        mLeftUserUseCase.execute(null, onNext, onError, doOnNext);
    }

    public void onMessageReceivedListener(Consumer<? super Message> onNext,
                                          Consumer<? super Throwable> onError) {
        mMessageUseCase.execute(null, onNext, onError);
    }

    public void onStartTypingListener(Consumer<? super User> onNext,
                                      Consumer<? super Throwable> onError) {
        mStartTypingUseCase.execute(mCurrentRoom.getUsers(), onNext, onError);
    }

    public void onStopTypingListener(Consumer<? super User> onNext,
                                     Consumer<? super Throwable> onError) {
        mStopTypingUseCase.execute(mCurrentRoom.getUsers(), onNext, onError);
    }

    public void onRoomInfoListener(Consumer<? super Room> onNext,
                                   Consumer<? super Throwable> onError) {
        Consumer<? super Room> doOnNext = room -> {
            this.mCurrentRoom = room;
        };
        mRoomInfoUseCase.execute(null, onNext, onError, doOnNext);
    }

    public void onRoomRestoredListener(Consumer<? super Room> onNext,
                                   Consumer<? super Throwable> onError) {
        Consumer<? super Room> doOnNext = room -> {
            this.mCurrentRoom = room;
        };
        mRoomRestoreUseCase.execute(null, onNext, onError, doOnNext);
    }

    public void onErrorListener(Consumer<? super ServerError> onNext,
                                Consumer<? super Throwable> onError) {
        mErrorUseCase.execute(null, onNext, onError);
    }

    public void onWordSettedListener(Consumer<? super Word> onNext,
                                     Consumer<? super Throwable> onError) {
        mWordSettedUseCase.execute(mCurrentRoom.getUsers(), onNext, onError);
    }

    public void onWordSettingListener(Consumer<? super User> onNext,
                                      Consumer<? super Throwable> onError) {
        mWordSettingUseCase.execute(mCurrentRoom.getUsers(), onNext, onError);
    }

    public void setWordToUser(Word word) {
        mRepository.setWord(word);
    }

    public void connectSocketToServer(long roomId) {
        User currentUser = mPreferenceManger.getUser();
        mRepository.connect(currentUser.getId(), roomId);

        joinToRoom();
    }

    public void joinToRoom() {
        mRepository.joinToRoom();
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

    public void destroy() {
        mOnConnectUseCase.dispose();
        mOnDisconnectUseCase.dispose();
        mOnConnectErrorUseCase.dispose();
        mOnConnectTimeoutUseCase.dispose();
        mOnReconnectUseCase.dispose();
        mOnReconnectErrorUseCase.dispose();

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

        mRepository.disconnectFromRoom();
        mRepository.disconnect();
    }
}
