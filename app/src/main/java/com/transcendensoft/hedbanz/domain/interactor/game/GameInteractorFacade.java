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
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.ErrorUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.JoinedUserUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.LeftUserUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.MessageUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.OnConnectErrorUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.OnConnectTimeoutUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.OnConnectUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.OnDisconnectUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.RoomInfoUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.StartTypingUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.StopTypingUseCase;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;
import com.transcendensoft.hedbanz.presentation.game.models.TypingMessage;

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
    private OnConnectUseCase mOnConnectUseCase;
    private OnDisconnectUseCase mOnDisconnectUseCase;
    private OnConnectErrorUseCase mOnConnectErrorUseCase;
    private OnConnectTimeoutUseCase mOnConnectTimeoutUseCase;
    private JoinedUserUseCase mJoinedUserUseCase;
    private LeftUserUseCase mLeftUserUseCase;
    private MessageUseCase mMessageUseCase;
    private StartTypingUseCase mStartTypingUseCase;
    private StopTypingUseCase mStopTypingUseCase;
    private RoomInfoUseCase mRoomInfoUseCase;
    private ErrorUseCase mErrorUseCase;

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
        mOnConnectUseCase = new OnConnectUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mOnDisconnectUseCase = new OnDisconnectUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mOnConnectErrorUseCase = new OnConnectErrorUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mOnConnectTimeoutUseCase = new OnConnectTimeoutUseCase(
                mObservableTransformer, compositeDisposable, mRepository);
        mJoinedUserUseCase = new JoinedUserUseCase(
                mObservableTransformer, compositeDisposable, mRepository, gson);
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

    public void onJoinedUserListener(Consumer<? super User> onNext,
                                     Consumer<? super Throwable> onError) {
        Consumer<? super User> doOnNext = user -> {
            if (mCurrentRoom != null && mCurrentRoom.getUsers() != null) {
                List<User> users = mCurrentRoom.getUsers();
                if(!users.contains(user)) {
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

    public void onStartTypingListener(Consumer<? super TypingMessage> onNext,
                                      Consumer<? super Throwable> onError) {
        mStartTypingUseCase.execute(mCurrentRoom.getUsers(), onNext, onError);
    }

    public void onStopTypingListener(Consumer<? super TypingMessage> onNext,
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

    public void onErrorListener(Consumer<? super ServerError> onNext,
                                   Consumer<? super Throwable> onError) {
        mErrorUseCase.execute(null, onNext, onError);
    }

    public void connectSocketToServer(long roomId) {
        User currentUser = mPreferenceManger.getUser();
        mRepository.connect(currentUser.getId(), roomId);

        mRepository.joinToRoom();
    }

    public void startTyping() {
        mRepository.startTyping();
    }

    public void stopTyping() {
        mRepository.stopTyping();
    }

    public void sendMessage(Message message) {
        mRepository.sendMessage(message);
    }

    public void destroy() {
        mOnConnectUseCase.dispose();
        mOnDisconnectUseCase.dispose();
        mOnConnectErrorUseCase.dispose();
        mOnConnectTimeoutUseCase.dispose();
        mJoinedUserUseCase.dispose();
        mLeftUserUseCase.dispose();
        mMessageUseCase.dispose();
        mStartTypingUseCase.dispose();
        mStopTypingUseCase.dispose();
        mRoomInfoUseCase.dispose();

        mRepository.disconnectFromRoom();
        mRepository.disconnect();
    }
}
