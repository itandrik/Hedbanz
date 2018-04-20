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

import android.widget.EditText;

import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * View and Presenter interfaces contract for game mode presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public interface GameContract {

    interface View extends BaseView {
        void addMessage(Message message);
        void addMessage(int position, Message message);
        void addMessages(List<Message> messages);
        void addMessages(int position, List<Message> messages);
        void clearMessages();
        void removeMessage(int position);
        void setMessage(int position, Message message);

        void showFooterTyping(List<User>users);
        void showFooterServerError();
        void showFooterNetworkError();
        void showEmptyList();
        void removeLastMessage();
    }

    interface Presenter {
        void initSockets();
        void messageTextChanges(EditText editText);
        void sendMessage(String message);
        void setWordToUser(String word, User user);

        void processRetryNetworkPagination(Observable<Object> clickObservable);
        void processRetryServerPagination(Observable<Object> clickObservable);
    }
}
