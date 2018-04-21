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
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.CompletableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.interactor.rooms.exception.RoomCreationException;
import com.transcendensoft.hedbanz.domain.repository.RoomDataRepository;
import com.transcendensoft.hedbanz.domain.validation.RoomError;
import com.transcendensoft.hedbanz.domain.validation.RoomValidator;
import com.transcendensoft.hedbanz.utils.SecurityUtils;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for retrieving data related to a specific list of
 * {@link com.transcendensoft.hedbanz.domain.entity.Room}.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class IsRoomPasswordCorrectInteractor extends CompletableUseCase<Room>{
    private RoomDataRepository mRoomRepository;
    private PreferenceManager mPreferenceManager;
    private RoomCreationException mRoomException;

    @Inject
    public IsRoomPasswordCorrectInteractor(CompletableTransformer mSchedulersTransformer,
                                CompositeDisposable mCompositeDisposable,
                                RoomDataRepository mRoomRepository,
                                PreferenceManager mPreferenceManager) {
        super(mSchedulersTransformer, mCompositeDisposable);
        this.mRoomRepository = mRoomRepository;
        this.mPreferenceManager = mPreferenceManager;
    }

    @Override
    protected Completable buildUseCaseCompletable(Room params) {
        mRoomException = new RoomCreationException();
        if(isValidRoom(params)){
            if(!TextUtils.isEmpty(params.getPassword())) {
                params.setPassword(SecurityUtils.hash(params.getPassword()));
            }

            return mRoomRepository.isPasswordCorrect(params.getId(), params.getPassword(), DataPolicy.API)
                    .onErrorResumeNext(this::processCreateRoomOnError);
        }
        return Completable.error(mRoomException);
    }

    private boolean isValidRoom(Room room){
        RoomValidator validator = new RoomValidator(room);
        boolean result = true;
        if (!validator.isPasswordValid()) {
            mRoomException.addRoomError(validator.getError());
            result = false;
        }
        return result;
    }

    private Completable processCreateRoomOnError(Throwable throwable) {
        if(throwable instanceof HedbanzApiException){
            HedbanzApiException exception = (HedbanzApiException) throwable;
            mRoomException.addRoomError(
                    RoomError.getRoomErrorByCode(exception.getServerErrorCode()));
        } else {
            mRoomException.addRoomError(RoomError.UNDEFINED_ERROR);
        }
        return Completable.error(mRoomException);
    }
}
