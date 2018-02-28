package com.transcendensoft.hedbanz.presenter.impl;
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

import com.crashlytics.android.Crashlytics;
import com.transcendensoft.hedbanz.model.api.manager.RoomsCrudApiManager;
import com.transcendensoft.hedbanz.model.data.PreferenceManager;
import com.transcendensoft.hedbanz.model.entity.Room;
import com.transcendensoft.hedbanz.model.entity.ServerResult;
import com.transcendensoft.hedbanz.model.entity.ServerStatus;
import com.transcendensoft.hedbanz.model.entity.User;
import com.transcendensoft.hedbanz.model.entity.error.ServerError;
import com.transcendensoft.hedbanz.presenter.BasePresenter;
import com.transcendensoft.hedbanz.presenter.CreateRoomPresenter;
import com.transcendensoft.hedbanz.presenter.validation.RoomValidator;
import com.transcendensoft.hedbanz.view.CreateRoomView;

import io.reactivex.disposables.Disposable;

/**
 * Presenter from MVP pattern, that contains
 * methods to create room for players and start a game
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class CreateRoomPresenterImpl extends BasePresenter<Room, CreateRoomView> implements CreateRoomPresenter{
    private static final String TAG = CreateRoomPresenterImpl.class.getName();

    @Override
    protected void updateView() {

    }

    @Override
    public void createRoom(Room room) {
        setModel(room);
        if(isRoomValid(room)){
            User curUser = new PreferenceManager(view().provideContext()).getUser();
            Disposable disposable = RoomsCrudApiManager.getInstance()
                    .createRoom(room.getPassword(), room.getName(),
                            room.getMaxPlayers(), curUser.getId())
                    .subscribe(this::processCreateRoomOnNext,
                            this::processCreateRoomOnError,
                            () -> view().createRoomSuccess(room),
                            this::processOnSubscribe);
            addDisposable(disposable);
        }
    }

    private boolean isRoomValid(Room room){
        RoomValidator validator = new RoomValidator(room);
        boolean result = true;
        if (!validator.isNameValid()) {
            view().showIncorrectRoomName(validator.getErrorMessage());
            result = false;
        }
        if (!validator.isPasswordValid()) {
            view().showIncorrectRoomPassword(validator.getErrorMessage());
            result = false;
        }
        return result;
    }

    private void processCreateRoomOnNext(ServerResult<Room> result) {
        if (result == null) {
            throw new RuntimeException("Server result is null");
        } else if (!result.getStatus().equalsIgnoreCase(ServerStatus.SUCCESS.toString())) {
            ServerError serverError = result.getServerError();
            if (serverError != null) {
                throw new RuntimeException(serverError.getErrorMessage());
            }
        }
    }

    private void processCreateRoomOnError(Throwable err) {
        Log.e(TAG, "Error " + err.getMessage());
        Crashlytics.logException(err);
        view().showServerError();
    }
}
