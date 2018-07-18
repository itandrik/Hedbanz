package com.transcendensoft.hedbanz.domain.interactor.friends;
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
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.domain.repository.FriendsDataRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for retrieving data related to a specific list of invited
 * {@link com.transcendensoft.hedbanz.domain.entity.Friend}.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class GetInviteFriends extends ObservableUseCase<List<Friend>, DataPolicy> {
    private FriendsDataRepository mRepository;
    private PreferenceManager mPreferenceManger;

    @Inject
    public GetInviteFriends(ObservableTransformer schedulersTransformer,
                      CompositeDisposable compositeDisposable,
                      FriendsDataRepository mRepository,
                      PreferenceManager mPreferenceManger) {
        super(schedulersTransformer, compositeDisposable);
        this.mRepository = mRepository;
        this.mPreferenceManger = mPreferenceManger;
    }

    @Override
    protected Observable<List<Friend>> buildUseCaseObservable(DataPolicy params) {
        return mRepository.getInviteFriends(
                mPreferenceManger.getUser().getId(),
                mPreferenceManger.getCurrentRoomId(),
                params);
    }
}
