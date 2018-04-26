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
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.entity.Word;
import com.transcendensoft.hedbanz.domain.interactor.game.GameInteractorFacade;
import com.transcendensoft.hedbanz.domain.interactor.game.GetMessagesInteractor;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.base.RecyclerDelegationAdapter;
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
        implements GameContract.Presenter, RecyclerDelegationAdapter.OnRecyclerBorderListener {
    private GameInteractorFacade mGameInteractor;
    private GetMessagesInteractor mGetMessagesInteractor;
    private PreferenceManager mPreferenceManger;
    private List<User> mTypingUsers;

    @Inject
    public GamePresenter(GameInteractorFacade gameInteractor,
                         GetMessagesInteractor getMessagesInteractor,
                         PreferenceManager preferenceManager) {
        this.mGameInteractor = gameInteractor;
        this.mGetMessagesInteractor = getMessagesInteractor;
        this.mPreferenceManger = preferenceManager;
        mTypingUsers = new ArrayList<>();
    }

    /*------------------------------------*
     *---------- Base presenter ----------*
     *------------------------------------*/
    @Override
    protected void updateView() {
        if (model.getMessages() == null || model.getMessages().isEmpty()) {
            model.setMessages(new ArrayList<>());
            view().showLoading();
            initSockets();
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
        Timber.i("BOTTOM reached");
    }

    @Override
    public void onTopReached() {
        Timber.i("TOP reached");
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
        if (clickObservable != null) {
            addDisposable(clickObservable.subscribe(
                    obj -> onTopReached(),
                    err -> Timber.e("Retry network in pagination error")
            ));
        }
    }

    @Override
    public void processSetWordToUserObservable(Observable<Word> sendWordObservable) {
        if (sendWordObservable != null) {
            addDisposable(sendWordObservable.subscribe(
                    word -> {
                        word.setSenderUser(mPreferenceManger.getUser());
                        updateSettingWordViewParameters(word.getSenderUser(), false, true);
                        mGameInteractor.setWordToUser(word);
                    },
                    err -> Timber.e("Error while send word to user. MEssage : " + err.getMessage())
            ));
        }
    }

    private void updateSettingWordViewParameters(User senderUser, boolean isFinished, boolean isLoading) {
        for (int i = model.getMessages().size() - 1; i >= 0; i--) {
            Message message = model.getMessages().get(i);
            if (message instanceof Word && message.getMessageType() == MessageType.WORD_SETTING &&
                    mPreferenceManger.getUser().equals(senderUser)) {
                Word settingWord = (Word) message;
                settingWord.setFinished(isFinished);
                settingWord.setLoading(isLoading);
                view().setMessage(i, settingWord);
                break;
            }
        }
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
                str -> {
                    Timber.i("Socket connected: %s", str);
                    if (mPreferenceManger.getCurrentRoomId() == -1) {
                        mGameInteractor.joinToRoom(model.getPassword());
                    } else {
                        view().showRestoreRoom();
                    }
                },
                this::processEventListenerOnError);
        mGameInteractor.onDisconnectListener(
                str -> {
                    Timber.i("Socket disconnected!");
                    view().showFooterDisconnected();
                },
                this::processEventListenerOnError);
        mGameInteractor.onConnectErrorListener(
                str -> {
                    Timber.e("Socket connect ERROR: %s", str);
                    view().showFooterDisconnected();
                },
                this::processEventListenerOnError);
        mGameInteractor.onConnectTimeoutListener(
                str -> {
                    Timber.e("Socket connect TIMEOUT: %s", str);
                    view().showFooterDisconnected();
                },
                this::processEventListenerOnError);
        mGameInteractor.onReconnectListener(
                str -> {
                    Timber.i("Socket reconnected!");
                    view().showFooterReconnected();
                },
                this::processEventListenerOnError);
        mGameInteractor.onReconnectErrorListener(str -> {
                    Timber.e("Socket reconnect error %s", str);
                    view().showFooterDisconnected();
                },
                this::processEventListenerOnError);
        mGameInteractor.onReconnectingListener(str -> {
                    Timber.i("Socket reconnecting... %s", str);
                    view().showFooterReconnecting();
                },
                this::processEventListenerOnError);
    }

    private void initBusinessLogicListeners() {
        mGameInteractor.onRoomInfoListener(
                this::initRoom,
                this::processEventListenerOnError);
        mGameInteractor.onRoomRestoredListener(
                this::initRoom,
                this::processEventListenerOnError);
        mGameInteractor.onErrorListener(error -> {
            Timber.e("Error from server from socket. Code : %d; Message: %s",
                    error.getErrorCode(), error.getErrorMessage());
        }, this::processEventListenerOnError);
    }

    private void initLeftUserListener() {
        mGameInteractor.onLeftUserListener(
                user -> {
                    List<User> users = model.getPlayers();
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
                    List<User> users = model.getPlayers();
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

    private void initUserAfkListener() {
        mGameInteractor.onUserAfkListener(user -> {
            Message message = new Message.Builder()
                    .setUserFrom(user)
                    .setMessageType(MessageType.USER_AFK)
                    .build();

            model.getMessages().add(message);
            view().addMessage(message);
        }, this::processEventListenerOnError);
    }

    private void initUserReturnedListener() {
        mGameInteractor.onUserReturnedListener(user -> {
            if(!mPreferenceManger.getUser().equals(user)) {
                Message message = new Message.Builder()
                        .setUserFrom(user)
                        .setMessageType(MessageType.USER_RETURNED)
                        .build();

                model.getMessages().add(message);
                view().addMessage(message);
            }
        }, this::processEventListenerOnError);
    }

    private void initRoom(Room room) {
        model = room;
        model.setMessages(new ArrayList<>());

        initJoinedUserListener();
        initLeftUserListener();
        initUserAfkListener();
        initUserReturnedListener();
        initMessageListeners();
        initWordSettingListeners();

        refreshMessageHistory();
    }

    private void initWordSettingListeners() {

        mGameInteractor.onWordSettedListener(word -> {
            word.setMessageType(MessageType.WORD_SETTED);
            model.getMessages().add(word);
            view().addMessage(word);

            updateSettingWordViewParameters(word.getSenderUser(), true, false);
        }, this::processEventListenerOnError);

        mGameInteractor.onWordSettingListener(wordReceiverUser -> {
            Word word = new Word(null, wordReceiverUser);
            word.setWordReceiverId(wordReceiverUser.getId());

            word.setMessageType(MessageType.WORD_SETTING);
            model.getMessages().add(word);
            view().addMessage(word);
        }, this::processEventListenerOnError);
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
        List<Message> messages = model.getMessages();

        if (message.getMessageType() == MessageType.SIMPLE_MESSAGE_THIS_USER &&
                !message.isLoading() && message.isFinished()) {
            for (int i = messages.size() - 1; i >= 0; i--) {
                Message modelMessage = messages.get(i);
                modelMessage.setLoading(false);
                modelMessage.setFinished(true);
                modelMessage.setCreateDate(message.getCreateDate());
                if (modelMessage.getClientMessageId() == message.getClientMessageId()) {
                    view().setMessage(i, modelMessage);
                    break;
                }
            }
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
        Message message = mGameInteractor.sendMessage(text);

        model.getMessages().add(message);
        view().addMessage(message);
    }

    @Override
    public void restoreRoom() {
        mGameInteractor.restoreRoom();
    }
}
