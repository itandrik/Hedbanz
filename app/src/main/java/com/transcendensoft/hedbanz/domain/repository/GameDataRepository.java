package com.transcendensoft.hedbanz.domain.repository;
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

import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.Question;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.entity.Word;

import org.json.JSONObject;

import io.reactivex.Observable;

/**
 * Interface that represents a Repository for getting Game related data.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface GameDataRepository {
    Observable<Boolean> connectObservable();
    Observable<Boolean> disconnectObservable();
    Observable<String> connectErrorObservable();
    Observable<Boolean> connectTimeoutObservable();
    Observable<Boolean> reconnectObservable();
    Observable<String> reconnectErrorObservable();
    Observable<Boolean> reconnectingObservable();

    Observable<JSONObject> roomInfoObservable();
    Observable<JSONObject> joinedUserObservable();
    Observable<JSONObject> leftUserObservable();
    Observable<JSONObject> userAfkObservable();
    Observable<JSONObject> userReturnedObservable();
    Observable<Room> restoreRoomObservable();

    Observable<JSONObject> typingObservable();
    Observable<JSONObject> stopTypingObservable();
    Observable<Message> messageObservable();
    Observable<JSONObject> errorObservable();
    Observable<JSONObject> settingWordObservable();
    Observable<JSONObject> wordSettedToUserObservable();

    Observable<User> wordGuessingObservable();
    Observable<Question> questionAskingObservable();
    Observable<Question> questionVotingObservable();

    void startTyping();
    void stopTyping();
    void sendMessage(Message message);
    void joinToRoom(String password);
    void disconnectFromRoom();
    void setWord(Word word);
    void sendRoomRestore();
    void guessWord(Question question);
    void voteForQuestion(Question question);
    void sendConnectInfo();

    void connect(long userId, long roomId);
    void disconnect();
}
