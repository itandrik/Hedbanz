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

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.transcendensoft.hedbanz.data.models.RoomDTO;
import com.transcendensoft.hedbanz.data.models.UserDTO;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

import javax.inject.Inject;

import static com.transcendensoft.hedbanz.data.network.source.ApiDataSource.GAME_SOCKET_NSP;
import static com.transcendensoft.hedbanz.data.network.source.ApiDataSource.HOST;
import static com.transcendensoft.hedbanz.data.network.source.ApiDataSource.PORT_SOCKET;

/**
 * Implementation of game mode presenter.
 * Here are work with server by sockets and other like
 * processing game algorithm.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ActivityScope
public class GamePresenter extends BasePresenter<RoomDTO, GameContract.View> implements GameContract.Presenter {
    private static final String TAG = GamePresenter.class.getName();

    private static final String JOIN_ROOM_EVENT = "join-room";
    private static final String LEAVE_ROOM_EVENT = "leave-room";
    private static final String ROOM_INFO_EVENT = "joined-room";
    private static final String JOINED_USER_EVENT = "joined-user";

    private Socket mSocket;
    private Emitter.Listener mRoomInfoListener;
    private Emitter.Listener mJoinedUserListener;

    private PreferenceManager mPreferenceManager;

    @Inject
    public GamePresenter(PreferenceManager mPreferenceManager) {
        this.mPreferenceManager = mPreferenceManager;
    }

    @Override
    protected void updateView() {
        if (model.getName() == null) {
            initSockets();
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void initSockets() {
        try {
            mSocket = IO.socket(HOST + PORT_SOCKET + GAME_SOCKET_NSP);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        emitJoinToRoom();
        initSocketListeners();

        mSocket.on(ROOM_INFO_EVENT, mRoomInfoListener);
        mSocket.on(JOINED_USER_EVENT, mJoinedUserListener);
        mSocket.connect();
    }

    private void emitJoinToRoom() {
        User user = mPreferenceManager.getUser();
        HashMap<String, Long> joinRoomObject = new HashMap<>();
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, model.getId());
        joinRoomObject.put(UserDTO.USER_ID_KEY, user.getId());
        Gson gson = new Gson();
        String json = gson.toJson(joinRoomObject);
        mSocket.emit(JOIN_ROOM_EVENT, json);
    }

    private void initSocketListeners() {
        Gson gson = new Gson();
        mRoomInfoListener = args -> {
            try {
                JSONObject data = (JSONObject) args[0];
                RoomDTO room = gson.fromJson(data.toString(), RoomDTO.class);
                if (room != null) {
                    Log.e(TAG, "RoomDTO id:" + room.getId() + "; name: " + room.getName());
                } else {
                    Log.e(TAG, "Received room == null");
                }
            } catch (JsonSyntaxException e) {
                Log.e(TAG, e.getMessage());
            }
        };

        mJoinedUserListener = args -> {
            try {
                JSONObject data = (JSONObject) args[0];
                UserDTO user = gson.fromJson(data.toString(), UserDTO.class);
                if (user != null) {
                    Log.e(TAG, "UserDTO conntected! UserDTO id:" + user.getId() + "; name: " + user.getLogin());
                } else {
                    Log.e(TAG, "Connected user == null");
                }
            } catch (JsonSyntaxException e) {
                Log.e(TAG, e.getMessage());
            }
        };
    }

    @Override
    public void disconnectSockets() {
        emitDisconnectFromRoom();
        mSocket.disconnect();
        mSocket.off(JOIN_ROOM_EVENT, mRoomInfoListener);
        mSocket.off(JOINED_USER_EVENT, mRoomInfoListener);
    }

    private void emitDisconnectFromRoom() {
        User user = mPreferenceManager.getUser();
        HashMap<String, Long> joinRoomObject = new HashMap<>();
        joinRoomObject.put(RoomDTO.ROOM_ID_KEY, model.getId());
        joinRoomObject.put(UserDTO.USER_ID_KEY, user.getId());
        mSocket.emit(LEAVE_ROOM_EVENT, joinRoomObject);
    }
}
