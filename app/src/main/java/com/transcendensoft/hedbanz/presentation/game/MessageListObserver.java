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

import com.transcendensoft.hedbanz.domain.PaginationState;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Room;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Observer that handles message list from Repository
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class MessageListObserver extends DisposableObserver<PaginationState<Message>> {
    private GameContract.View mView;
    private Room mModel;

    public MessageListObserver(GameContract.View view, Room model) {
        this.mModel = model;
        this.mView = view;
    }

    @Override
    public void onNext(PaginationState<Message> messagePaginationState) {
        if (messagePaginationState != null) {
            if (processErrors(messagePaginationState)) {
                return;
            }

            List<Message> messages = messagePaginationState.getData();
            if (messages == null || messages.isEmpty()) {
                processEmptyMessageList(messagePaginationState);
            } else {
                processNotEmptyMessageList(messagePaginationState, messages);
            }
        }
    }

    private boolean processErrors(PaginationState<Message> messagePaginationState) {
        boolean hasErrors = false;
        if (messagePaginationState.isHasInternetError()) {
            processNetworkError(messagePaginationState);
            hasErrors = true;
        }
        if (messagePaginationState.isHasServerError()) {
            processServerError(messagePaginationState);
            hasErrors = true;
        }
        return hasErrors;
    }

    private void processNetworkError(PaginationState<Message> messagePaginationState) {
        if (messagePaginationState.isRefreshed()) {
            mView.showNetworkError();
        } else {
            Message lastMessage = mModel.getMessages().get(0);
            if (lastMessage.getMessageType() != MessageType.ERROR_NETWORK) {
                lastMessage.setMessageType(MessageType.ERROR_NETWORK);
                mView.setMessage(0, lastMessage);
            }
        }
    }

    private void processServerError(PaginationState<Message> messagePaginationState) {
        if (messagePaginationState.isRefreshed()) {
            mView.showServerError();
        } else {
            Message lastMessage = mModel.getMessages().get(0);
            if (lastMessage.getMessageType() != MessageType.ERROR_SERVER) {
                lastMessage.setMessageType(MessageType.ERROR_SERVER);
                mView.setMessage(0, lastMessage);
            }
        }
    }

    private void processEmptyMessageList(PaginationState<Message> messagePaginationState) {
        if (messagePaginationState.isRefreshed()) {
            mView.showEmptyList();
        } else {
            Message lastMessage = mModel.getMessages().get(0);
            if (lastMessage.getMessageType() == MessageType.LOADING) {
                mModel.getMessages().remove(0);
                mView.removeLastMessage();
            }
        }
    }

    private void processNotEmptyMessageList(PaginationState<Message> messagePaginationState, List<Message> messages) {
        if (!messagePaginationState.isRefreshed()) {
            mModel.getMessages().remove(0);
            mView.removeLastMessage();
        }

        messages.add(0, new Message.Builder().setMessageType(MessageType.LOADING).build());

        mView.addMessages(0, messages);
        mView.showContent();
        mModel.getMessages().addAll(0, messages);
    }

    @Override
    public void onError(Throwable e) {
        if (mView != null) {
            mView.showServerError();
        }
    }

    @Override
    public void onComplete() {
        // Stub
    }
}
