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

import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.game.GameInteractorFacade;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.game.models.TypingMessage;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Implementation of game mode presenter.
 * Here are work with server by sockets and other like
 * processing game algorithm.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ActivityScope
public class GamePresenter extends BasePresenter<Room, GameContract.View> implements GameContract.Presenter {
   private GameInteractorFacade mGameInteractor;

    @Inject
    public GamePresenter(GameInteractorFacade gameInteractor) {
        this.mGameInteractor = gameInteractor;
    }

    @Override
    protected void updateView() {
        if (model.getName() == null) {
            initSockets();
        }
    }

    @Override
    public void destroy() {
        mGameInteractor.destroy();
    }

    @Override
    public void initSockets() {
        initSocketSystemListeners();
        initBusinessLogicListeners();

        mGameInteractor.connectSocketToServer(model.getId());
    }

    private void initSocketSystemListeners() {
        mGameInteractor.onConnectListener(
                str -> Timber.i("Socket connected. JSON: %s", str),
                this::processEventListenerOnError);
        mGameInteractor.onDisconnectListener(
                str -> Timber.i("Socket disconnected. JSON: %s", str),
                this::processEventListenerOnError);
        mGameInteractor.onConnectErrorListener(
                str -> Timber.e("Socket connect ERROR. JSON: %s", str),
                this::processEventListenerOnError);
        mGameInteractor.onConnectTimeoutListener(
                str -> Timber.i("Socket connect TIMEOUT. JSON: %s", str),
                this::processEventListenerOnError);
    }

    private void initBusinessLogicListeners() {
        mGameInteractor.onJoinedUserListener(
                user -> {
                    model.getUsers().add(user);
                    Message message = new Message.Builder()
                            .setUserFrom(user)
                            .setMessageType(MessageType.JOINED_USER)
                            .build();
                    view().addMessage(message);
                },
                this::processEventListenerOnError);
        mGameInteractor.onLeftUserListener(
                user -> {
                        model.getUsers().remove(user);
                        Message message = new Message.Builder()
                                .setUserFrom(user)
                                .setMessageType(MessageType.LEFT_USER)
                                .build();
                        view().addMessage(message);
                },
                this::processEventListenerOnError);
        mGameInteractor.onRoomInfoListener(
                room -> model = room,
                this::processEventListenerOnError);
        mGameInteractor.onMessageReceivedListener(
                msg -> Timber.i("Message received: %s", msg.toString()),
                this::processEventListenerOnError);
        mGameInteractor.onStartTypingListener(
                userId -> Timber.i("User %s is typing...", user.getLogin()),
                this::processEventListenerOnError);
        mGameInteractor.onStopTypingListener(
                userId -> Timber.i("User %s stopped typing...", user.getLogin()),
                this::processEventListenerOnError);
    }

    private void processTypingMessage(List<Message> messages, Message message) {
        Message lastMessage = null;
        if(!messages.isEmpty()){
            lastMessage = messages.get(messages.size() - 1);
        }
        if (message.getMessageType() == MessageType.START_TYPING) {
            if (lastMessage instanceof TypingMessage) {
                ((TypingMessage) lastMessage).addUser(message.getUserFrom());
            } else {
                messages.add(new TypingMessage(message));
            }
        } else if (message.getMessageType() == MessageType.STOP_TYPING) {
            if (lastMessage instanceof TypingMessage) {
                TypingMessage typingMessage = (TypingMessage) lastMessage;
                typingMessage.removeUser(message.getUserFrom());
                if (typingMessage.getTypingUsers().isEmpty()) {
                    messages.remove(messages.size() - 1);
                }
            }
        }
    }

    private void processSimpleMessage(List<Message> messages, Message message) {
        Message lastMessage = null;
        if(!messages.isEmpty()){
            lastMessage = messages.get(messages.size() - 1);
        }
        if (lastMessage instanceof TypingMessage) {
            messages.add(messages.size() - 1, message);
        } else {
            messages.add(message);
        }
    }

    private void processEventListenerOnError(Throwable err) {
        if(err instanceof IncorrectJsonException){
            IncorrectJsonException incorrectJsonException = (IncorrectJsonException) err;
            Timber.e("Incorrect JSON from socket listener. JSON: %S; EVENT: %s",
                    incorrectJsonException.getJson(), incorrectJsonException.getMethod());
        }
    }
}
