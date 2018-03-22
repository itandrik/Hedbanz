package com.transcendensoft.hedbanz.presentation.mainscreen.roomcreation;
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
import com.transcendensoft.hedbanz.data.models.UserDTO;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.repository.RoomDataRepository;
import com.transcendensoft.hedbanz.domain.validation.RoomValidator;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Presenter from MVP pattern, that contains
 * methods to create room for players and start a game
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class CreateRoomPresenter extends BasePresenter<Room, CreateRoomContract.View>
        implements CreateRoomContract.Presenter{
    private static final String TAG = CreateRoomPresenter.class.getName();
    private RoomDataRepository mRoomRepository;
    private PreferenceManager mPreferenceManager;

    @Inject
    public CreateRoomPresenter(RoomDataRepository roomRepository, PreferenceManager preferenceManager) {
        this.mRoomRepository = roomRepository;
        this.mPreferenceManager = preferenceManager;
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void createRoom(Room room) {
        setModel(room);
        if(isRoomValid(room)){
            UserDTO curUser = mPreferenceManager.getUser();
            Disposable disposable = mRoomRepository
                    .createRoom(room, curUser.getId())
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

    private void processCreateRoomOnNext(Room result) {
       // if (result == null) {
       ///     throw new RuntimeException("Server result is null");
       // } else if (!result.getStatus().equalsIgnoreCase(ServerStatus.SUCCESS.toString())) {
        //    ServerError serverError = result.getServerError();
        //    if (serverError != null) {
        //        throw new RuntimeException(serverError.getErrorMessage());
       //     }
     //   }
    }

    private void processCreateRoomOnError(Throwable err) {
        Log.e(TAG, "Error " + err.getMessage());
        Crashlytics.logException(err);
        view().showServerError();
    }
}