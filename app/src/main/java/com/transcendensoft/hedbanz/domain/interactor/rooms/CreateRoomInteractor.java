package com.transcendensoft.hedbanz.domain.interactor.rooms;
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

import android.text.TextUtils;

import com.transcendensoft.hedbanz.data.exception.HedbanzApiException;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.UseCase;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.rooms.exception.RoomCreationException;
import com.transcendensoft.hedbanz.domain.repository.RoomDataRepository;
import com.transcendensoft.hedbanz.domain.validation.RoomError;
import com.transcendensoft.hedbanz.domain.validation.RoomValidator;
import com.transcendensoft.hedbanz.utils.SecurityUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for
 * {@link com.transcendensoft.hedbanz.domain.entity.Room} creation.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class CreateRoomInteractor extends UseCase<Room, Room>{
    private RoomDataRepository mRoomRepository;
    private PreferenceManager mPreferenceManager;
    private RoomCreationException mRoomException;

    @Inject
    public CreateRoomInteractor(ObservableTransformer mSchedulersTransformer,
                                CompositeDisposable mCompositeDisposable,
                                RoomDataRepository mRoomRepository,
                                PreferenceManager mPreferenceManager) {
        super(mSchedulersTransformer, mCompositeDisposable);
        this.mRoomRepository = mRoomRepository;
        this.mPreferenceManager = mPreferenceManager;
    }

    @Override
    protected Observable<Room> buildUseCaseObservable(Room params) {
        mRoomException = new RoomCreationException();
        if(isValidRoom(params)){
            User currentUser = mPreferenceManager.getUser();

            if(!TextUtils.isEmpty(params.getPassword()) && params.isWithPassword()) {
                params.setPassword(SecurityUtils.hash(params.getPassword()));
            }

            mRoomRepository.createRoom(params, currentUser.getId())
                    .onErrorResumeNext(this::processCreateRoomOnError);
        }
        return Observable.error(mRoomException);
    }

    private boolean isValidRoom(Room room){
        RoomValidator validator = new RoomValidator(room);
        boolean result = true;
        if (!validator.isNameValid()) {
            mRoomException.addRoomError(validator.getError());
            result = false;
        }
        if (!validator.isPasswordValid()) {
            mRoomException.addRoomError(validator.getError());
            result = false;
        }
        return result;
    }

    private Observable<Room> processCreateRoomOnError(Throwable throwable) {
        if(throwable instanceof HedbanzApiException){
            HedbanzApiException exception = (HedbanzApiException) throwable;
            mRoomException.addRoomError(
                    RoomError.getRoomErrorByCode(exception.getServerErrorCode()));
        } else {
            mRoomException.addRoomError(RoomError.UNDEFINED_ERROR);
        }
        return Observable.error(mRoomException);
    }
}
