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
import com.google.gson.JsonSyntaxException;
import com.transcendensoft.hedbanz.data.models.MessageDTO;
import com.transcendensoft.hedbanz.data.models.RoomDTO;
import com.transcendensoft.hedbanz.data.models.UserDTO;
import com.transcendensoft.hedbanz.data.models.WordDTO;
import com.transcendensoft.hedbanz.data.models.mapper.MessageModelDataMapper;
import com.transcendensoft.hedbanz.data.models.mapper.RoomModelDataMapper;
import com.transcendensoft.hedbanz.data.models.mapper.WordModelDataMapper;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.Word;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import timber.log.Timber;

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
    private static final String CLIENT_CONNECT_INFO = "client-connect-info";
    private static final String CLIENT_RESTORE_ROOM = "client-restore-room";
    private static final String SERVER_RESTORE_ROOM = "server-restore-room";
    private static final String SERVER_USER_AFK = "server-user-afk";
    private static final String SERVER_USER_RETURNED = "server-user-returned";

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

    private static final String SERVER_SET_PLAYER_WORD_EVENT = "server-set-word";
    private static final String CLIENT_SET_PLAYER_WORD_EVENT = "client-set-word";
    private static final String SERVER_THOUGHT_PLAYER_WORD_EVENT = "server-thought-player-word";

    private Socket mSocket;
    private long mUserId;
    private long mRoomId;

    private MessageModelDataMapper mMessageMapper;
    private RoomModelDataMapper mRoomMapper;
    private WordModelDataMapper mWordMapper;
    private Gson mGson;

    @Inject
    public GameDataRepositoryImpl(MessageModelDataMapper messageModelDataMapper,
                                  WordModelDataMapper wordModelDataMapper,
                                  RoomModelDataMapper roomMapper, Gson gson) {
        this.mMessageMapper = messageModelDataMapper;
        this.mWordMapper = wordModelDataMapper;
        this.mRoomMapper = roomMapper;
        this.mGson = gson;

        try {
            IO.Options options = new IO.Options();
            options.forceNew = true;
            options.reconnection = true;
            options.reconnectionDelay = 1000;
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
            mSocket.once(Socket.EVENT_CONNECT, listener);
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
    public Observable<Boolean> reconnectObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                emitter.onNext(true);
                sendConnectInfo();
            };
            mSocket.on(Socket.EVENT_RECONNECT, listener);
        });
    }

    @Override
    public Observable<Boolean> reconnectErrorObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                emitter.onNext(true);
            };
            mSocket.on(Socket.EVENT_RECONNECT_ERROR, listener);
        });
    }

    @Override
    public Observable<Boolean> reconnectingObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                emitter.onNext(true);
            };
            mSocket.on(Socket.EVENT_RECONNECTING, listener);
        });
    }

    @Override
    public Observable<JSONObject> roomInfoObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                Timber.i("SOCKET <-- GET(%1$s) : %2$s", ROOM_INFO_EVENT, data.toString());
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
                Timber.i("SOCKET <-- GET(%1$s) : %2$s", JOINED_USER_EVENT, data.toString());
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
                Timber.i("SOCKET <-- GET(%1$s) : %2$s", LEFT_USER_EVENT, data.toString());
                emitter.onNext(data);
            };
            mSocket.on(LEFT_USER_EVENT, listener);
        });
    }

    @Override
    public Observable<Room> restoreRoomObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                RoomDTO roomDTO = mGson.fromJson(data.toString(), RoomDTO.class);

                Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_RESTORE_ROOM, data.toString());
                emitter.onNext(mRoomMapper.convert(roomDTO));
            };
            mSocket.on(SERVER_RESTORE_ROOM, listener);
        });
    }

    @Override
    public Observable<JSONObject> userAfkObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];

                Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_USER_AFK, data.toString());
                emitter.onNext(data);
            };
            mSocket.on(SERVER_USER_AFK, listener);
        });
    }

    @Override
    public Observable<JSONObject> userReturnedObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];

                Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_USER_RETURNED, data.toString());
                emitter.onNext(data);
            };
            mSocket.on(SERVER_USER_RETURNED, listener);
        });
    }

    @Override
    public Observable<JSONObject> typingObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_TYPING_EVENT, data.toString());
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
                Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_STOP_TYPING_EVENT, data.toString());
                emitter.onNext(data);
            };
            mSocket.on(SERVER_STOP_TYPING_EVENT, listener);
        });
    }

    @Override
    public Observable<Message> messageObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                try {
                    MessageDTO messageDTO = mGson.fromJson(data.toString(), MessageDTO.class);
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_MESSAGE_EVENT, data.toString());
                    emitter.onNext(mMessageMapper.convert(messageDTO));
                } catch (JsonSyntaxException e) {
                    emitter.onError(new JsonSyntaxException("Error in json: " + data.toString()));
                }
            };
            mSocket.on(SERVER_MESSAGE_EVENT, listener);
        });
    }

    @Override
    public Observable<JSONObject> errorObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                try {
                    JSONObject data = (JSONObject) args[0];
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_ERROR_EVENT, data.toString());
                    emitter.onNext(data);
                } catch (ClassCastException e){
                    Timber.e(args[0].toString());
                }
            };
            mSocket.on(SERVER_ERROR_EVENT, listener);
        });
    }

    @Override
    public Observable<JSONObject> settingWordObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_SET_PLAYER_WORD_EVENT, data.toString());
                emitter.onNext(data);

                if (args.length > 1) {
                    try {
                        Ack ack = (Ack) args[args.length - 1];
                        ack.call(true);
                    } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
                        Timber.e(e.getMessage());
                    }
                }
            };
            mSocket.on(SERVER_SET_PLAYER_WORD_EVENT, listener);
        });
    }

    @Override
    public Observable<JSONObject> wordSettedToUserObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_THOUGHT_PLAYER_WORD_EVENT, data.toString());
                emitter.onNext(data);
            };
            mSocket.on(SERVER_THOUGHT_PLAYER_WORD_EVENT, listener);
        });
    }

    @Override
    public void setWord(Word word) {
        word.setSenderId(mUserId);
        word.setRoomId(mRoomId);

        WordDTO wordDTO = mWordMapper.convert(word);
        String json = mGson.toJson(wordDTO, WordDTO.class);

        Timber.i("SOCKET --> SEND(%1$s) : %2$s", CLIENT_SET_PLAYER_WORD_EVENT, json);
        mSocket.emit(CLIENT_SET_PLAYER_WORD_EVENT, json);
    }

    @Override
    public void startTyping() {
        HashMap<String, Long> joinRoomObject = new HashMap<>();
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        joinRoomObject.put(UserDTO.USER_ID_KEY, mUserId);
        Gson gson = new Gson();
        String json = gson.toJson(joinRoomObject);

        Timber.i("SOCKET --> SEND(%1$s) : %2$s", CLIENT_TYPING_EVENT, json);
        mSocket.emit(CLIENT_TYPING_EVENT, json);
    }

    @Override
    public void stopTyping() {
        HashMap<String, Long> joinRoomObject = new HashMap<>();
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        joinRoomObject.put(UserDTO.USER_ID_KEY, mUserId);
        Gson gson = new Gson();
        String json = gson.toJson(joinRoomObject);

        Timber.i("SOCKET --> SEND(%1$s) : %2$s", CLIENT_STOP_TYPING_EVENT, json);
        mSocket.emit(CLIENT_STOP_TYPING_EVENT, json);
    }

    @Override
    public void sendMessage(Message message) {
        MessageDTO messageDTO = new MessageDTO.Builder()
                .setSenderUser(new UserDTO.Builder().setId(mUserId).build())
                .setRoomId(mRoomId)
                .setText(message.getMessage())
                .setType(MessageType.SIMPLE_MESSAGE.getId())
                .setClientMessageId(message.getClientMessageId())
                .build();

        Gson gson = new Gson();
        String json = gson.toJson(messageDTO);

        Timber.i("SOCKET --> SEND(%1$s) : %2$s", CLIENT_MESSAGE_EVENT, json);
        mSocket.emit(CLIENT_MESSAGE_EVENT, json);
    }

    @Override
    public void joinToRoom() {
        HashMap<String, Long> joinRoomObject = new HashMap<>();
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        joinRoomObject.put(UserDTO.USER_ID_KEY, mUserId);
        Gson gson = new Gson();
        String json = gson.toJson(joinRoomObject);

        Timber.i("SOCKET --> SEND(%1$s) : %2$s", JOIN_ROOM_EVENT, json);
        mSocket.emit(JOIN_ROOM_EVENT, json);
    }

    @Override
    public void disconnectFromRoom() {
        HashMap<String, Long> joinRoomObject = new HashMap<>();
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        joinRoomObject.put(UserDTO.USER_ID_KEY, mUserId);

        Timber.i("SOCKET --> SEND(%1$s)", LEAVE_ROOM_EVENT);
        mSocket.emit(LEAVE_ROOM_EVENT, joinRoomObject);
    }

    @Override
    public void connect(long userId, long roomId) {
        this.mUserId = userId;
        this.mRoomId = roomId;
        mSocket.connect();
    }

    private void sendConnectInfo() {
        String json = getUserRoomInfoObject();

        HashMap<String, Long> connectInfoObject = new HashMap<>();
        connectInfoObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        connectInfoObject.put(UserDTO.USER_ID_KEY, mUserId);

        Timber.i("SOCKET --> SEND(%1$s). Data : %2$s",
                CLIENT_CONNECT_INFO, json);
        mSocket.emit(CLIENT_CONNECT_INFO, connectInfoObject);
    }

    private String getUserRoomInfoObject() {
        HashMap<String, Long> connectInfoObject = new HashMap<>();
        connectInfoObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        connectInfoObject.put(UserDTO.USER_ID_KEY, mUserId);

        Gson gson = new Gson();
        return gson.toJson(connectInfoObject, HashMap.class);
    }

    @Override
    public void sendRoomRestore() {
        String json = getUserRoomInfoObject();

        Timber.i("SOCKET --> SEND(%1$s). Data : %2$s",
                CLIENT_RESTORE_ROOM, json);
        mSocket.emit(CLIENT_RESTORE_ROOM, json);
    }

    @Override
    public void disconnect() {
        if (mSocket.connected()) {
            mSocket.disconnect();

            mSocket.off(Socket.EVENT_CONNECT);
            mSocket.off(Socket.EVENT_DISCONNECT);
            mSocket.off(Socket.EVENT_ERROR);
            mSocket.off(Socket.EVENT_CONNECT_ERROR);
            mSocket.off(Socket.EVENT_RECONNECT);
            mSocket.off(Socket.EVENT_RECONNECT_ERROR);
            mSocket.off(Socket.EVENT_RECONNECTING);

            mSocket.off(JOIN_ROOM_EVENT);
            mSocket.off(LEAVE_ROOM_EVENT);
            mSocket.off(JOINED_USER_EVENT);
            mSocket.off(LEFT_USER_EVENT);
            mSocket.off(ROOM_INFO_EVENT);
            mSocket.off(SERVER_RESTORE_ROOM);
            mSocket.off(SERVER_USER_AFK);
            mSocket.off(SERVER_USER_RETURNED);

            mSocket.off(SERVER_TYPING_EVENT);
            mSocket.off(SERVER_STOP_TYPING_EVENT);
            mSocket.off(SERVER_MESSAGE_EVENT);
            mSocket.off(SERVER_ERROR_EVENT);
            mSocket.off(SERVER_SET_PLAYER_WORD_EVENT);
            mSocket.off(SERVER_THOUGHT_PLAYER_WORD_EVENT);
        }
    }
}
