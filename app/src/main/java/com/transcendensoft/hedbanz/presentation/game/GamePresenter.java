package com.transcendensoft.hedbanz.presentation.game;
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

import android.text.TextUtils;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.game.GameInteractorFacade;
import com.transcendensoft.hedbanz.domain.interactor.game.GetMessagesInteractor;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.base.RecyclerDelegationAdapter;
import com.transcendensoft.hedbanz.presentation.game.models.TypingMessage;
import com.transcendensoft.hedbanz.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Implementation of game mode presenter.
 * Here are work with server by sockets and other like
 * processing game algorithm.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
@ActivityScope
public class GamePresenter extends BasePresenter<Room, GameContract.View>
        implements GameContract.Presenter, RecyclerDelegationAdapter.OnRecyclerBottomReachedListener {
    private GameInteractorFacade mGameInteractor;
    private GetMessagesInteractor mGetMessagesInteractor;
    private List<User> mTypingUsers;

    @Inject
    public GamePresenter(GameInteractorFacade gameInteractor,
                         GetMessagesInteractor getMessagesInteractor) {
        this.mGameInteractor = gameInteractor;
        this.mGetMessagesInteractor = getMessagesInteractor;
        mTypingUsers = new ArrayList<>();
    }

    /*------------------------------------*
     *---------- Base presenter ----------*
     *------------------------------------*/
    @Override
    protected void updateView() {
        if (model.getMessages() == null || model.getMessages().isEmpty()) {
            model.setMessages(new ArrayList<>());
            initSockets();
            refreshMessageHistory();
        } else {
            view().clearMessages();
            view().addMessages(model.getMessages());
        }
    }

    @Override
    public void destroy() {
        mGetMessagesInteractor.dispose();
        mGameInteractor.destroy();
    }

    /*------------------------------------*
     *--------- Messages loading ---------*
     *------------------------------------*/
    @Override
    public void onBottomReached() {
        mGetMessagesInteractor.loadNextPage()
                .execute(new MessageListObserver(view(), model), model.getId());
    }

    private void refreshMessageHistory() {
        mGetMessagesInteractor.refresh(null)
                .execute(new MessageListObserver(view(), model), model.getId());
    }

    /*------------------------------------*
     *----- Recycler click listeners -----*
     *------------------------------------*/
    @Override
    public void processRetryNetworkPagination(Observable<Object> clickObservable) {
        processRetryServerPagination(clickObservable);
    }

    @Override
    public void processRetryServerPagination(Observable<Object> clickObservable) {
        addDisposable(clickObservable.subscribe(
                obj -> onBottomReached(),
                err -> Timber.e("Retry network in pagination error")
        ));
    }

    /*------------------------------------*
     *------ Socket initialization -------*
     *------------------------------------*/
    @Override
    public void initSockets() {
        initSocketSystemListeners();
        initBusinessLogicListeners();

        mGameInteractor.connectSocketToServer(model.getId());
    }

    private void initSocketSystemListeners() {
        mGameInteractor.onConnectListener(
                str -> Timber.i("Socket connected: %s", str),
                this::processEventListenerOnError);
        mGameInteractor.onDisconnectListener(
                str -> Timber.i("Socket disconnected: %s", str),
                this::processEventListenerOnError);
        mGameInteractor.onConnectErrorListener(
                str -> Timber.e("Socket connect ERROR: %s", str),
                this::processEventListenerOnError);
        mGameInteractor.onConnectTimeoutListener(
                str -> Timber.i("Socket connect TIMEOUT: %s", str),
                this::processEventListenerOnError);
    }

    private void initBusinessLogicListeners() {
        initJoinedUserListener();
        initLeftUserListener();
        mGameInteractor.onRoomInfoListener(
                room -> {
                    model = room;
                    model.setMessages(new ArrayList<>());
                    initMessageListeners();
                },
                this::processEventListenerOnError);
        mGameInteractor.onErrorListener(error -> {
            Timber.e("Error from server from socket. Code : %d; Message: %s",
                    error.getErrorCode(), error.getErrorMessage());
        }, this::processEventListenerOnError);
    }

    private void initLeftUserListener() {
        mGameInteractor.onLeftUserListener(
                user -> {
                    List<User> users = model.getUsers();
                    if (!users.contains(user)) {
                        users.remove(user);
                    }
                    Message message = new Message.Builder()
                            .setUserFrom(user)
                            .setMessageType(MessageType.LEFT_USER)
                            .build();
                    model.getMessages().add(message);
                    view().addMessage(message);
                },
                this::processEventListenerOnError);
    }

    private void initJoinedUserListener() {
        mGameInteractor.onJoinedUserListener(
                user -> {
                    List<User> users = model.getUsers();
                    if (!users.contains(user)) {
                        users.add(user);
                    }
                    Message message = new Message.Builder()
                            .setUserFrom(user)
                            .setMessageType(MessageType.JOINED_USER)
                            .build();
                    model.getMessages().add(message);
                    view().addMessage(message);
                },
                this::processEventListenerOnError);
    }

    private void initMessageListeners() {
        mGameInteractor.onStartTypingListener(
                this::processStartTyping,
                this::processEventListenerOnError);
        mGameInteractor.onStopTypingListener(
                this::processStopTyping,
                this::processEventListenerOnError);
        mGameInteractor.onMessageReceivedListener(
                this::processSimpleMessage,
                this::processEventListenerOnError);
    }

    private void processSimpleMessage(Message message) {
        Message lastMessage = null;
        List<Message> messages = model.getMessages();

        if (!messages.isEmpty()) {
            lastMessage = messages.get(messages.size() - 1);
        }
        if (lastMessage instanceof TypingMessage) {
            messages.add(messages.size() - 1, message);
            view().addMessage(messages.size() - 2, message);
        } else {
            messages.add(message);
            view().addMessage(message);
        }
    }

    private void processStartTyping(User user) {
        if (!mTypingUsers.contains(user)) {
            mTypingUsers.add(user);
        }
        view().showFooterTyping(mTypingUsers);
    }

    private void processStopTyping(User user) {
        if (mTypingUsers.contains(user)) {
            mTypingUsers.remove(user);
        }
        view().showFooterTyping(mTypingUsers);
    }

    private void processEventListenerOnError(Throwable err) {
        if (err instanceof IncorrectJsonException) {
            IncorrectJsonException incorrectJsonException = (IncorrectJsonException) err;
            Timber.e("Incorrect JSON from socket listener. JSON: %S; EVENT: %s",
                    incorrectJsonException.getJson(), incorrectJsonException.getMethod());
        }
    }

    /*------------------------------------*
     *---- Send socket event messages ----*
     *------------------------------------*/
    @Override
    public void messageTextChanges(EditText editText) {
        addDisposable(
                RxTextView.textChanges(editText)
                        .skip(1)
                        .filter(text -> !TextUtils.isEmpty(text))
                        .compose(RxUtils.debounceFirst(500, TimeUnit.MILLISECONDS))
                        .doOnNext((str) -> {
                            mGameInteractor.startTyping();
                        })
                        .mergeWith(RxTextView.textChanges(editText)
                                .skip(1)
                                .debounce(500, TimeUnit.MILLISECONDS)
                                .doOnNext(str -> {
                                    mGameInteractor.stopTyping();
                                }))
                        .subscribe());
    }

    @Override
    public void sendMessage(String text) {
        Message message = new Message.Builder()
                .setMessage(text)
                .build();
        mGameInteractor.sendMessage(message);
    }
}
