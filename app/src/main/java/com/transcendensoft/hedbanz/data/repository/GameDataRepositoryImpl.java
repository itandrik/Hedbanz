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
import com.google.gson.reflect.TypeToken;
import com.transcendensoft.hedbanz.data.models.MessageDTO;
import com.transcendensoft.hedbanz.data.models.PlayerGuessingDTO;
import com.transcendensoft.hedbanz.data.models.QuestionDTO;
import com.transcendensoft.hedbanz.data.models.RoomDTO;
import com.transcendensoft.hedbanz.data.models.UserDTO;
import com.transcendensoft.hedbanz.data.models.WordDTO;
import com.transcendensoft.hedbanz.data.models.mapper.MessageModelDataMapper;
import com.transcendensoft.hedbanz.data.models.mapper.PlayerGuessingModelDataMapper;
import com.transcendensoft.hedbanz.data.models.mapper.QuestionModelDataMapper;
import com.transcendensoft.hedbanz.data.models.mapper.RoomModelDataMapper;
import com.transcendensoft.hedbanz.data.models.mapper.UserModelDataMapper;
import com.transcendensoft.hedbanz.data.models.mapper.WordModelDataMapper;
import com.transcendensoft.hedbanz.domain.entity.Advertise;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.PlayerGuessing;
import com.transcendensoft.hedbanz.domain.entity.Question;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.entity.Word;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.kick.KickUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.kick.KickWarningUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.JoinedUserUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.LeftUserUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.PlayersInfoUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.UserAfkUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.UserReturnedUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.user.UserWinUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.word.WordSettedUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.word.WordSettingUseCase;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

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
    private static final String SERVER_PLAYERS_INFO = "server-players-status";
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

    private static final String SERVER_USER_GUESSING_EVENT = "server-user-guessing";
    private static final String SERVER_USER_ASKING_EVENT = "server-user-asking";
    private static final String SERVER_USER_ANSWERING_EVENT = "server-user-answering";
    private static final String CLIENT_USER_GUESSING_EVENT = "client-user-guessing";
    private static final String CLIENT_USER_ANSWERING_EVENT = "client-user-answering";
    private static final String SERVER_USER_WIN = "server-user-win";

    private static final String SERVER_PLAYER_AFK_WARNING = "server-player-afk-warning";
    private static final String SERVER_KICKED_USER_EVENT = "server-kicked-user";

    private static final String SERVER_GAME_OVER = "server-game-over";
    private static final String CLIENT_RESTART_GAME = "client-restart-game";

    private static final String SERVER_WAITING_FOR_USERS = "server-waiting-for-users";
    private static final String SERVER_UPDATE_USERS_INFO = "server-update-users-info";
    private static final String SERVER_ADVERTISE = "server_advertise";

    private Socket mSocket;
    private long mUserId;
    private long mRoomId;
    private String mSecurityToken;

    private MessageModelDataMapper mMessageMapper;
    private RoomModelDataMapper mRoomMapper;
    private WordModelDataMapper mWordMapper;
    private QuestionModelDataMapper mQuestionMapper;
    private UserModelDataMapper mUserMapper;
    private PlayerGuessingModelDataMapper mPlayerGuessingModelDataMapper;
    private Gson mGson;
    private String mRoomToUserJson;

    @Inject
    public GameDataRepositoryImpl(MessageModelDataMapper messageModelDataMapper,
                                  WordModelDataMapper wordModelDataMapper,
                                  RoomModelDataMapper roomMapper,
                                  UserModelDataMapper userModelDataMapper,
                                  QuestionModelDataMapper questionModelDataMapper,
                                  PlayerGuessingModelDataMapper playerGuessingModelDataMapper,
                                  Gson gson) {
        this.mMessageMapper = messageModelDataMapper;
        this.mWordMapper = wordModelDataMapper;
        this.mRoomMapper = roomMapper;
        this.mQuestionMapper = questionModelDataMapper;
        this.mUserMapper = userModelDataMapper;
        this.mPlayerGuessingModelDataMapper = playerGuessingModelDataMapper;
        this.mGson = gson;

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
            Emitter.Listener listener = args -> emitter.onNext(true);
            mSocket.once(Socket.EVENT_CONNECT, listener);
        });
    }

    @Override
    public Observable<Boolean> disconnectObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> emitter.onNext(true);
            mSocket.on(Socket.EVENT_DISCONNECT, listener);
        });
    }

    @Override
    public Observable<String> connectErrorObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                if (args == null) {
                    emitter.onNext("null");
                } else {
                    emitter.onNext(Arrays.toString(args));
                }
            };
            mSocket.on(Socket.EVENT_CONNECT_ERROR, listener);
        });
    }

    @Override
    public Observable<Boolean> connectTimeoutObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> emitter.onNext(true);
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
    public Observable<String> reconnectErrorObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                if (args == null) {
                    emitter.onNext("null");
                } else {
                    emitter.onNext(Arrays.toString(args));
                }
            };
            mSocket.on(Socket.EVENT_RECONNECT_ERROR, listener);
        });
    }

    @Override
    public Observable<Boolean> reconnectingObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> emitter.onNext(true);
            mSocket.on(Socket.EVENT_RECONNECTING, listener);
        });
    }

    @Override
    public Observable<Room> roomInfoObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                if (data != null) {
                    RoomDTO roomDTO = mGson.fromJson(data.toString(), RoomDTO.class);

                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            ROOM_INFO_EVENT, data.toString());
                    emitter.onNext(mRoomMapper.convert(roomDTO));
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            ROOM_INFO_EVENT, "null");
                }
            };
            mSocket.on(ROOM_INFO_EVENT, listener);
        });
    }

    @Override
    public Observable<User> joinedUserObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                if (data != null) {
                    try {
                        UserDTO userDTO = mGson.fromJson(data.toString(), UserDTO.class);

                        Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                                JOINED_USER_EVENT, data.toString());
                        emitter.onNext(mUserMapper.convert(userDTO));
                    } catch (JsonSyntaxException e) {
                        emitter.onError(new IncorrectJsonException(
                                data.toString(), JoinedUserUseCase.class.getName()));
                    }
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            JOINED_USER_EVENT, "null");
                    emitter.onError(new NullPointerException("data is null when user joined"));
                }
            };
            mSocket.on(JOINED_USER_EVENT, listener);
        });
    }

    @Override
    public Observable<User> leftUserObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];

                if (data != null) {
                    try {
                        UserDTO userDTO = mGson.fromJson(data.toString(), UserDTO.class);

                        Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                                LEFT_USER_EVENT, data.toString());
                        emitter.onNext(mUserMapper.convert(userDTO));
                    } catch (JsonSyntaxException e) {
                        emitter.onError(new IncorrectJsonException(
                                data.toString(), LeftUserUseCase.class.getName()));
                    }
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            LEFT_USER_EVENT, "null");
                    emitter.onError(new NullPointerException("data is null when user joined"));
                }
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

                Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                        SERVER_RESTORE_ROOM, data.toString());
                emitter.onNext(mRoomMapper.convert(roomDTO));
            };
            mSocket.on(SERVER_RESTORE_ROOM, listener);
        });
    }

    @Override
    public Observable<User> userAfkObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];

                if (data != null) {
                    try {
                        UserDTO userDTO = mGson.fromJson(data.toString(), UserDTO.class);

                        Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                                SERVER_USER_AFK, data.toString());
                        emitter.onNext(mUserMapper.convert(userDTO));
                    } catch (JsonSyntaxException e) {
                        emitter.onError(new IncorrectJsonException(
                                data.toString(), UserAfkUseCase.class.getName()));
                    }
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_USER_AFK, "null");
                    emitter.onError(new NullPointerException("data is null when user joined"));
                }
            };
            mSocket.on(SERVER_USER_AFK, listener);
        });
    }

    @Override
    public Observable<User> userReturnedObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];

                if (data != null) {
                    try {
                        UserDTO userDTO = mGson.fromJson(data.toString(), UserDTO.class);

                        Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                                SERVER_USER_RETURNED, data.toString());
                        emitter.onNext(mUserMapper.convert(userDTO));
                    } catch (JsonSyntaxException e) {
                        emitter.onError(new IncorrectJsonException(
                                data.toString(), UserReturnedUseCase.class.getName()));
                    }
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_USER_RETURNED, "null");
                    //emitter.onError(new Throwable("data is null when user joined"));
                }
            };
            mSocket.on(SERVER_USER_RETURNED, listener);
        });
    }

    @Override
    public Observable<List<User>> playersInfoObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONArray data = (JSONArray) args[0];

                if (data != null) {
                    try {
                        List<UserDTO> players = mGson.fromJson(data.toString(),
                                new TypeToken<List<UserDTO>>() {
                                }.getType());

                        Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                                SERVER_PLAYERS_INFO, data.toString());
                        emitter.onNext(mUserMapper.convertToUsers(players));
                    } catch (JsonSyntaxException e) {
                        emitter.onError(new IncorrectJsonException(
                                data.toString(), PlayersInfoUseCase.class.getName()));
                    }
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_PLAYERS_INFO, "null");
                }
            };
            mSocket.on(SERVER_PLAYERS_INFO, listener);
        });
    }

    @Override
    public Observable<User> userWin() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];

                if (data != null) {
                    try {
                        UserDTO userDTO = mGson.fromJson(data.toString(), UserDTO.class);

                        Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                                SERVER_USER_WIN, data.toString());
                        emitter.onNext(mUserMapper.convert(userDTO));
                    } catch (JsonSyntaxException e) {
                        emitter.onError(new IncorrectJsonException(
                                data.toString(), UserWinUseCase.class.getName()));
                    }
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_USER_WIN, "null");
                }
            };
            mSocket.on(SERVER_USER_WIN, listener);
        });
    }

    @Override
    public Observable<JSONObject> typingObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_TYPING_EVENT,
                        data != null ? data.toString() : "null");
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
                Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_STOP_TYPING_EVENT,
                        data != null ? data.toString() : "null");
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
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_MESSAGE_EVENT, data.toString());
                    emitter.onNext(mMessageMapper.convert(messageDTO));
                } catch (JsonSyntaxException e) {
                    emitter.onError(new JsonSyntaxException(
                            "Error in json: " + (data != null ? data.toString() : "null")));
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
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s", SERVER_ERROR_EVENT,
                            data != null ? data.toString() : "null");
                    emitter.onNext(data);
                } catch (ClassCastException e) {
                    Timber.e(args[0].toString());
                }
            };
            mSocket.on(SERVER_ERROR_EVENT, listener);
        });
    }

    @Override
    public Observable<Word> settingWordObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];

                if (data != null) {
                    try {
                        WordDTO wordDTO = mGson.fromJson(data.toString(), WordDTO.class);

                        Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                                SERVER_SET_PLAYER_WORD_EVENT, data.toString());
                        emitter.onNext(mWordMapper.convert(wordDTO));

                        if (args.length > 1) {
                            try {
                                Ack ack = (Ack) args[args.length - 1];
                                ack.call(true);
                            } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
                                Timber.e(e.getMessage());
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        emitter.onError(new IncorrectJsonException(
                                data.toString(), WordSettingUseCase.class.getName()));
                    }
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_SET_PLAYER_WORD_EVENT, "null");
                }
            };
            mSocket.on(SERVER_SET_PLAYER_WORD_EVENT, listener);
        });
    }

    @Override
    public Observable<Word> wordSettedToUserObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];

                if (data != null) {
                    try {
                        WordDTO wordDTO = mGson.fromJson(data.toString(), WordDTO.class);

                        Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                                SERVER_THOUGHT_PLAYER_WORD_EVENT, data.toString());
                        emitter.onNext(mWordMapper.convert(wordDTO));
                    } catch (JsonSyntaxException e) {
                        emitter.onError(new IncorrectJsonException(
                                data.toString(), WordSettedUseCase.class.getName()));
                    }
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_THOUGHT_PLAYER_WORD_EVENT, "null");
                }
            };
            mSocket.on(SERVER_THOUGHT_PLAYER_WORD_EVENT, listener);
        });
    }

    @Override
    public Observable<PlayerGuessing> wordGuessingObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                try {
                    PlayerGuessingDTO playerGuessingDTO = mGson.fromJson(
                            data.toString(), PlayerGuessingDTO.class);

                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_USER_GUESSING_EVENT, data.toString());
                    emitter.onNext(mPlayerGuessingModelDataMapper.convert(playerGuessingDTO));
                } catch (JsonSyntaxException | NullPointerException e) {
                    emitter.onError(e);
                }
            };
            mSocket.on(SERVER_USER_GUESSING_EVENT, listener);
        });
    }

    @Override
    public Observable<Question> questionAskingObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                try {
                    QuestionDTO questionDTO = mGson.fromJson(data.toString(), QuestionDTO.class);

                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_USER_ASKING_EVENT, data.toString());
                    emitter.onNext(mQuestionMapper.convert(questionDTO));
                } catch (JsonSyntaxException | NullPointerException e) {
                    emitter.onError(e);
                }
            };
            mSocket.on(SERVER_USER_ASKING_EVENT, listener);
        });
    }

    @Override
    public Observable<Question> questionVotingObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                try {
                    QuestionDTO questionDTO = mGson.fromJson(data.toString(), QuestionDTO.class);

                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_USER_ANSWERING_EVENT, data.toString());
                    emitter.onNext(mQuestionMapper.convert(questionDTO));
                } catch (JsonSyntaxException | NullPointerException e) {
                    emitter.onError(e);
                }
            };
            mSocket.on(SERVER_USER_ANSWERING_EVENT, listener);
        });
    }

    @Override
    public Observable<User> userAfkWarningObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];

                if (data != null) {
                    try {
                        UserDTO userDTO = mGson.fromJson(data.toString(), UserDTO.class);

                        Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                                SERVER_PLAYER_AFK_WARNING, data.toString());
                        emitter.onNext(mUserMapper.convert(userDTO));
                    } catch (JsonSyntaxException e) {
                        emitter.onError(new IncorrectJsonException(
                                data.toString(), KickWarningUseCase.class.getName()));
                    }
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_PLAYER_AFK_WARNING, "null");
                }
            };
            mSocket.on(SERVER_PLAYER_AFK_WARNING, listener);
        });
    }

    @Override
    public Observable<User> userKickedObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];

                if (data != null) {
                    try {
                        UserDTO userDTO = mGson.fromJson(data.toString(), UserDTO.class);

                        Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                                SERVER_KICKED_USER_EVENT, data.toString());
                        emitter.onNext(mUserMapper.convert(userDTO));
                    } catch (JsonSyntaxException e) {
                        emitter.onError(new IncorrectJsonException(
                                data.toString(), KickUseCase.class.getName()));
                    }
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_KICKED_USER_EVENT, "null");
                }
            };
            mSocket.on(SERVER_KICKED_USER_EVENT, listener);
        });
    }

    @Override
    public Observable<Boolean> gameOverObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                emitter.onNext(false);
                Timber.i("SOCKET <-- GET(%1$s) : Game over.",
                        SERVER_GAME_OVER);
            };
            mSocket.on(SERVER_GAME_OVER, listener);
        });
    }

    @Override
    public Observable<Boolean> waitingForUsersObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                emitter.onNext(false);
                Timber.i("SOCKET <-- GET(%1$s) : Waiting for users.",
                        SERVER_WAITING_FOR_USERS);
            };
            mSocket.on(SERVER_WAITING_FOR_USERS, listener);
        });
    }

    @Override
    public Observable<Room> updateUsersInfoObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                JSONObject data = (JSONObject) args[0];
                if (data != null) {
                    RoomDTO roomDTO = mGson.fromJson(data.toString(), RoomDTO.class);

                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_UPDATE_USERS_INFO, data.toString());
                    emitter.onNext(mRoomMapper.convert(roomDTO));
                } else {
                    Timber.i("SOCKET <-- GET(%1$s) : %2$s",
                            SERVER_UPDATE_USERS_INFO, "null");
                }
            };
            mSocket.on(SERVER_UPDATE_USERS_INFO, listener);
        });
    }

    @Override
    public Observable<Advertise> advertiseObservable() {
        return Observable.create(emitter -> {
            Emitter.Listener listener = args -> {
                if (args != null && args.length != 0) {
                    emitter.onNext(Advertise.Companion.getTypeById((Integer) args[0]));
                    Timber.i("SOCKET <-- GET(%1$s) : Advertise",
                            SERVER_ADVERTISE);
                } else {
                    Timber.e("SOCKET <-- GET(%1$s) : Advertise parameters is null" +
                            Arrays.toString(args));
                }
            };
            mSocket.on(SERVER_ADVERTISE, listener);
        });
    }

    @Override
    public void restartGame() {
        Timber.i("SOCKET --> SEND(%1$s) : %2$s",
                CLIENT_RESTART_GAME, mRoomToUserJson);
        mSocket.emit(CLIENT_RESTART_GAME, mRoomToUserJson);
    }

    @Override
    public void guessWord(Question question) {
        QuestionDTO questionDTO = mQuestionMapper.convert(question);
        questionDTO.setSenderId(mUserId);
        questionDTO.setRoomId(mRoomId);
        questionDTO.setSecurityToken(mSecurityToken);

        String json = mGson.toJson(questionDTO);

        Timber.i("SOCKET --> SEND(%1$s) : %2$s",
                CLIENT_USER_GUESSING_EVENT, json);
        mSocket.emit(CLIENT_USER_GUESSING_EVENT, json);
    }

    @Override
    public void voteForQuestion(Question question) {
        QuestionDTO questionDTO = mQuestionMapper.convert(question);
        questionDTO.setSenderId(mUserId);
        questionDTO.setRoomId(mRoomId);
        questionDTO.setSecurityToken(mSecurityToken);

        String json = mGson.toJson(questionDTO);

        Timber.i("SOCKET --> SEND(%1$s) : %2$s",
                CLIENT_USER_ANSWERING_EVENT, json);
        mSocket.emit(CLIENT_USER_ANSWERING_EVENT, json);
    }

    @Override
    public void setWord(Word word) {
        LinkedHashMap<String, Object> setWordObject = new LinkedHashMap<>();
        setWordObject.put(UserDTO.SENDER_ID_KEY, mUserId);
        setWordObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        setWordObject.put("word", word.getWord());
        setWordObject.put(RoomDTO.TOKEN_KEY, mSecurityToken);

        String json = mGson.toJson(setWordObject);

        Timber.i("SOCKET --> SEND(%1$s) : %2$s",
                CLIENT_SET_PLAYER_WORD_EVENT, json);
        mSocket.emit(CLIENT_SET_PLAYER_WORD_EVENT, json);
    }

    @Override
    public void startTyping() {
        Timber.i("SOCKET --> SEND(%1$s) : %2$s",
                CLIENT_TYPING_EVENT, mRoomToUserJson);
        mSocket.emit(CLIENT_TYPING_EVENT, mRoomToUserJson);
    }

    @Override
    public void stopTyping() {
        Timber.i("SOCKET --> SEND(%1$s) : %2$s",
                CLIENT_STOP_TYPING_EVENT, mRoomToUserJson);
        mSocket.emit(CLIENT_STOP_TYPING_EVENT, mRoomToUserJson);
    }

    @Override
    public void sendMessage(Message message) {
        MessageDTO messageDTO = new MessageDTO.Builder()
                .setSenderUser(new UserDTO.Builder().setId(mUserId).build())
                .setSenderId(mUserId)
                .setRoomId(mRoomId)
                .setText(message.getMessage())
                .setType(MessageType.SIMPLE_MESSAGE.getId())
                .setClientMessageId(message.getClientMessageId())
                .build();
        messageDTO.setSecurityToken(mSecurityToken);

        String json = mGson.toJson(messageDTO);

        Timber.i("SOCKET --> SEND(%1$s) : %2$s", CLIENT_MESSAGE_EVENT, json);
        mSocket.emit(CLIENT_MESSAGE_EVENT, json);
    }

    @Override
    public void joinToRoom(String password) {
        LinkedHashMap<String, Object> joinRoomObject = new LinkedHashMap<>();
        joinRoomObject.put(UserDTO.USER_ID_KEY, mUserId);
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        joinRoomObject.put(RoomDTO.PASSWORD_KEY, password);
        joinRoomObject.put(RoomDTO.TOKEN_KEY, mSecurityToken);

        String json = mGson.toJson(joinRoomObject);

        Timber.i("SOCKET --> SEND(%1$s) : %2$s", JOIN_ROOM_EVENT, json);
        mSocket.emit(JOIN_ROOM_EVENT, json);
    }

    @Override
    public void connect(long userId, long roomId, String securityToken) {
        this.mUserId = userId;
        this.mRoomId = roomId;
        this.mSecurityToken = securityToken;

        mSocket.connect();

        LinkedHashMap<String, Object> joinRoomObject = new LinkedHashMap<>();
        joinRoomObject.put(UserDTO.USER_ID_KEY, mUserId);
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, mRoomId);
        joinRoomObject.put(RoomDTO.TOKEN_KEY, mSecurityToken);

        mRoomToUserJson = mGson.toJson(joinRoomObject);
    }

    public void sendConnectInfo() {
        Timber.i("SOCKET --> SEND(%1$s). Data : %2$s",
                CLIENT_CONNECT_INFO, mRoomToUserJson);

        mSocket.emit(CLIENT_CONNECT_INFO, mRoomToUserJson);
    }

    @Override
    public void sendRoomRestore() {
        Timber.i("SOCKET --> SEND(%1$s). Data : %2$s",
                CLIENT_RESTORE_ROOM, mRoomToUserJson);
        mSocket.emit(CLIENT_RESTORE_ROOM, mRoomToUserJson);
    }

    @Override
    public void disconnectFromRoom() {
        Timber.i("SOCKET --> SEND(%1$s)", LEAVE_ROOM_EVENT);
        mSocket.emit(LEAVE_ROOM_EVENT, mRoomToUserJson);
    }

    private boolean isAfterStop = false;

    @Override
    public void startSocket() {
        if (mSocket != null && isAfterStop) {
            mSocket.connect();
            sendConnectInfo();
            isAfterStop = false;
        }
    }

    @Override
    public void stopSocket() {
        if (mSocket != null) {
            mSocket.disconnect();
            isAfterStop = true;
        }
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
            mSocket.off(SERVER_PLAYERS_INFO);

            mSocket.off(SERVER_TYPING_EVENT);
            mSocket.off(SERVER_STOP_TYPING_EVENT);
            mSocket.off(SERVER_MESSAGE_EVENT);
            mSocket.off(SERVER_ERROR_EVENT);
            mSocket.off(SERVER_SET_PLAYER_WORD_EVENT);
            mSocket.off(SERVER_THOUGHT_PLAYER_WORD_EVENT);

            mSocket.off(SERVER_USER_GUESSING_EVENT);
            mSocket.off(SERVER_USER_ASKING_EVENT);
            mSocket.off(SERVER_USER_ANSWERING_EVENT);
            mSocket.off(SERVER_USER_WIN);
            mSocket.off(SERVER_GAME_OVER);
            mSocket.off(SERVER_WAITING_FOR_USERS);
            mSocket.off(SERVER_ADVERTISE);
            mSocket.off(SERVER_UPDATE_USERS_INFO);

            mSocket.off(SERVER_PLAYER_AFK_WARNING);
            mSocket.off(SERVER_KICKED_USER_EVENT);
        }
    }
}
