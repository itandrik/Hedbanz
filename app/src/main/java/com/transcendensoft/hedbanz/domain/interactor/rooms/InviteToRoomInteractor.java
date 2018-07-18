package com.transcendensoft.hedbanz.domain.interactor.rooms;
/**
 * Copyright 2018. Andrii Chernysh
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

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.CompletableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Invite;
import com.transcendensoft.hedbanz.domain.repository.RoomDataRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for inviting friend to some room
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class InviteToRoomInteractor extends CompletableUseCase<Invite> {
    private RoomDataRepository mRoomRepository;
    private PreferenceManager mPreferenceManager;

    @Inject
    public InviteToRoomInteractor(CompletableTransformer mSchedulersTransformer,
                                  CompositeDisposable mCompositeDisposable,
                                  RoomDataRepository mRoomRepository,
                                  PreferenceManager preferenceManager) {
        super(mSchedulersTransformer, mCompositeDisposable);
        this.mRoomRepository = mRoomRepository;
        this.mPreferenceManager = preferenceManager;
    }

    @Override
    protected Completable buildUseCaseCompletable(Invite params) {
        params.setSenderId(mPreferenceManager.getUser().getId());

        return mRoomRepository.inviteFriend(params);
    }
}
