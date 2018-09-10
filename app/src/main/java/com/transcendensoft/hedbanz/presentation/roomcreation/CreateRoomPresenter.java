package com.transcendensoft.hedbanz.presentation.roomcreation;
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

import android.content.Context;

import com.transcendensoft.hedbanz.data.network.retrofit.NoConnectivityException;
import com.transcendensoft.hedbanz.di.qualifier.ActivityContext;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.interactor.rooms.CreateRoomInteractor;
import com.transcendensoft.hedbanz.domain.interactor.rooms.exception.RoomCreationException;
import com.transcendensoft.hedbanz.domain.validation.RoomError;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.roomcreation.models.RoomIcons;
import com.transcendensoft.hedbanz.presentation.roomcreation.models.StickerIcons;
import com.transcendensoft.hedbanz.presentation.rooms.models.RoomList;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Presenter from MVP pattern, that contains
 * methods to create room for players and start a game
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class CreateRoomPresenter extends BasePresenter<RoomList, CreateRoomContract.View>
        implements CreateRoomContract.Presenter {
    private CreateRoomInteractor mCreateRoomInteractor;
    private Context mContext;  //Only for logging purposes

    @Inject
    public CreateRoomPresenter(CreateRoomInteractor createRoomInteractor, @ActivityContext Context context) {
        this.mCreateRoomInteractor = createRoomInteractor;
        mContext = context;
    }

    @Override
    protected void updateView() {
        // Stub
    }

    @Override
    public void destroy() {
        mCreateRoomInteractor.dispose();
    }

    @Override
    public void createRoom(Room room) {
        room.setStickerId(StickerIcons.Companion.getRandomId());
        room.setIconId(RoomIcons.Companion.getRandomId());

        mCreateRoomInteractor.execute(room,
                this::processCreateRoomOnNext,
                this::processCreateRoomOnError,
                () -> view().hideLoadingDialog(),
                d -> view().showLoadingDialog());
    }

    private void processCreateRoomOnNext(Room result) {
        model.addRoom(result);
        view().createRoomSuccess(result);
    }

    private void processCreateRoomOnError(Throwable err) {
        if (err instanceof RoomCreationException) {
            RoomCreationException exception = (RoomCreationException) err;
            for (RoomError roomError : exception.getErrors()) {
                processRoomError(roomError);
            }
        } else if (err instanceof NoConnectivityException) {
            view().showNetworkError();
        } else {
            Timber.e(err);
            view().showServerError();
        }
    }

    private void processRoomError(RoomError roomError) {
        switch (roomError) {
            case NO_SUCH_USER:
            case UNDEFINED_ERROR:
                Timber.e(mContext.getString(roomError.getErrorMessage()));
                view().createRoomError();
                break;
            case ROOM_ALREADY_EXIST:
            case EMPTY_NAME:
                view().showIncorrectRoomName(roomError.getErrorMessage());
                break;
            case EMPTY_PASSWORD:
            case INVALID_PASSWORD:
                view().showIncorrectRoomPassword(roomError.getErrorMessage());
                break;
            case USER_HAS_MAX_ACTIVE_ROOMS_NUMBER:
                view().showMaxActiveRoomsError();
                break;
            default:
                Timber.e(mContext.getString(roomError.getErrorMessage()));
                view().showServerError();

        }
    }
}
