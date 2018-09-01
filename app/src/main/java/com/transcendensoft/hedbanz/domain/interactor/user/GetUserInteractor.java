package com.transcendensoft.hedbanz.domain.interactor.user;
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

import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for getting user info. Data related to a specific
 * {@link com.transcendensoft.hedbanz.domain.entity.User}.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GetUserInteractor extends ObservableUseCase<User, Long> {
    private UserDataRepository mUserDataRepository;

    @Inject
    public GetUserInteractor(ObservableTransformer mSchedulersTransformer,
                             CompositeDisposable mCompositeDisposable,
                             UserDataRepository userDataRepository) {
        super(mSchedulersTransformer, mCompositeDisposable);
        this.mUserDataRepository = userDataRepository;
    }

    @Override
    protected Observable<User> buildUseCaseObservable(Long param) {
        return mUserDataRepository.getUser(param, DataPolicy.API);
    }
}
