package com.transcendensoft.hedbanz.data.repository;
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
import com.transcendensoft.hedbanz.data.models.MessageDTO;
import com.transcendensoft.hedbanz.data.models.RoomDTO;
import com.transcendensoft.hedbanz.data.models.UserDTO;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.transcendensoft.hedbanz.data.network.source.ApiDataSource.GAME_SOCKET_NSP;
import static com.transcendensoft.hedbanz.data.network.source.ApiDataSource.HOST;
import static com.transcendensoft.hedbanz.data.network.source.ApiDataSource.PORT_SOCKET;

/**
 * Interface that represents a Repository (or Gateway)
 * for getting Game related data.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GameDataRepositoryImpl implements GameDataRepository {
    private static final String JOIN_ROOM_EVENT = "join-room";
    private static final String LEAVE_ROOM_EVENT = "leave-room";
    private static final String ROOM_INFO_EVENT = "joined-room";
    private static final String JOINED_USER_EVENT = "joined-user";
    private static final String LEFT_USER_EVENT = "left-user";
    private static final String CLIENT_TYPING_EVENT = "client-start-typing";
    private static final String CLIENT_STOP_TYPING_EVENT = "client-stop-typing";
    private static final String CLIENT_MESSAGE_EVENT = "client-msg";
    private static final String SERVER_TYPING_EVENT = "server-start-typing";
    private static final String SERVER_STOP_TYPING_EVENT = "server-stop-typing";
    private static final String SERVER_MESSAGE_EVENT = "server-msg";
    private static final String SERVER_ERROR_EVENT = "server-error";

    private Socket mSocket;
    private long mUserId;
    private long mRoomId;

    @Inject
    public GameDataRepositoryImpl() {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = false;
            options.reconnection = true;

            mSocket = IO.socket(HOST + PORT_SOCKET + GAME_SOCKET_NSP, options);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Observable<Boolean> connectObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                emitter.onNext(true);
            };
            mSocket.on(Socket.EVENT_CONNECT, listener);
        });
    }

    @Override
    public Observable<Boolean> disconnectObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                emitter.onNext(true);
            };
            mSocket.on(Socket.EVENT_DISCONNECT, listener);
        });
    }

    @Override
    public Observable<Boolean> connectErrorObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                emitter.onNext(true);
            };
            mSocket.on(Socket.EVENT_CONNECT_ERROR, listener);
        });
    }

    @Override
    public Observable<Boolean> connectTimeoutObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                emitter.onNext(true);
            };
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, listener);
        });
    }

    @Override
    public Observable<JSONObject> roomInfoObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                emitter.onNext(data);
            };
            mSocket.on(ROOM_INFO_EVENT, listener);
        });
    }

    @Override
    public Observable<JSONObject> joinedUserObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                emitter.onNext(data);
            };
            mSocket.on(JOINED_USER_EVENT, listener);
        });
    }

    @Override
    public Observable<JSONObject> leftUserObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                emitter.onNext(data);
            };
            mSocket.on(LEFT_USER_EVENT, listener);
        });
    }

    @Override
    public Observable<JSONObject> typingObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                emitter.onNext(data);
            };
            mSocket.on(SERVER_TYPING_EVENT, listener);
        });
    }

    @Override
    public Observable<JSONObject> stopTypingObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                emitter.onNext(data);
            };
            mSocket.on(SERVER_STOP_TYPING_EVENT, listener);
        });
    }

    @Override
    public Observable<JSONObject> messageObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                emitter.onNext(data);
            };
            mSocket.on(SERVER_MESSAGE_EVENT, listener);
        });
    }

    @Override
    public Observable<JSONObject> errorObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                emitter.onNext(data);
            };
            mSocket.on(SERVER_ERROR_EVENT, listener);
        });
    }

    @Override
    public void startTyping() {
        HashMap<String, Long> joinRoomObject = new HashMap<>();
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        joinRoomObject.put(UserDTO.USER_ID_KEY, mUserId);
        Gson gson = new Gson();
        String json = gson.toJson(joinRoomObject);

        mSocket.emit(CLIENT_TYPING_EVENT, json);
    }

    @Override
    public void stopTyping() {
        HashMap<String, Long> joinRoomObject = new HashMap<>();
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        joinRoomObject.put(UserDTO.USER_ID_KEY, mUserId);
        Gson gson = new Gson();
        String json = gson.toJson(joinRoomObject);

        mSocket.emit(CLIENT_STOP_TYPING_EVENT, json);
    }

    @Override
    public void sendMessage(Message message) {
        MessageDTO messageDTO = new MessageDTO.Builder()
                .setSenderId(mUserId)
                .setRoomId(mRoomId)
                .setText(message.getMessage())
                .setType(MessageType.SIMPLE_MESSAGE.getId())
                .build();

        Gson gson = new Gson();
        String json = gson.toJson(messageDTO);

        mSocket.emit(CLIENT_MESSAGE_EVENT, json);
    }

    @Override
    public void joinToRoom() {
        HashMap<String, Long> joinRoomObject = new HashMap<>();
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        joinRoomObject.put(UserDTO.USER_ID_KEY, mUserId);
        Gson gson = new Gson();
        String json = gson.toJson(joinRoomObject);
        mSocket.emit(JOIN_ROOM_EVENT, json);
    }

    @Override
    public void disconnectFromRoom() {
        HashMap<String, Long> joinRoomObject = new HashMap<>();
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        joinRoomObject.put(UserDTO.USER_ID_KEY, mUserId);
        mSocket.emit(LEAVE_ROOM_EVENT, joinRoomObject);
    }

    @Override
    public void connect(long userId, long roomId) {
        this.mUserId = userId;
        this.mRoomId = roomId;
        mSocket.connect();
    }

    @Override
    public void disconnect() {
        if (mSocket.connected()) {
            mSocket.disconnect();
            mSocket.off(JOIN_ROOM_EVENT);
            mSocket.off(JOINED_USER_EVENT);
            mSocket.off(JOIN_ROOM_EVENT);
            mSocket.off(LEAVE_ROOM_EVENT);
            mSocket.off(ROOM_INFO_EVENT);
            mSocket.off(JOINED_USER_EVENT);
            mSocket.off(LEFT_USER_EVENT);
            mSocket.off(CLIENT_TYPING_EVENT);
            mSocket.off(CLIENT_STOP_TYPING_EVENT);
            mSocket.off(CLIENT_MESSAGE_EVENT);
            mSocket.off(SERVER_TYPING_EVENT);
            mSocket.off(SERVER_STOP_TYPING_EVENT);
            mSocket.off(SERVER_MESSAGE_EVENT);
        }
    }
}
