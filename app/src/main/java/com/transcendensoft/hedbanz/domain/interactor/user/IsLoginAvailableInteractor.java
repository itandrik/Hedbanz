package com.transcendensoft.hedbanz.domain.interactor.user;
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

import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;

import org.json.JSONException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for checking login for availability. Data related to a specific
 * {@link com.transcendensoft.hedbanz.domain.entity.User}.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class IsLoginAvailableInteractor extends ObservableUseCase<Boolean, String> {
    private static final String IS_LOGIN_AVAILABLE = "isLoginAvailable";

    private UserDataRepository mUserDataRepository;
    private PublishSubject<Boolean> mSubjectLoginAvailable;

    @Inject
    public IsLoginAvailableInteractor(ObservableTransformer mSchedulersTransformer,
                                      CompositeDisposable mCompositeDisposable,
                                      UserDataRepository userDataRepository) {
        super(mSchedulersTransformer, mCompositeDisposable);
        this.mUserDataRepository = userDataRepository;
        mUserDataRepository.connectIsLoginAvailable();

        Observable<Boolean> isLoginAvailableObservable = mUserDataRepository.isLoginAvailableObservable()
                .flatMap(jsonObject -> {
                    try {
                        return Observable.just(jsonObject.getBoolean(IS_LOGIN_AVAILABLE));
                    } catch (JSONException e) {
                        throw new RuntimeException("Error parsing JSONObject" +
                                "while getting isLoginAvailable from server");
                    }
                });
        mSubjectLoginAvailable = PublishSubject.create();
        isLoginAvailableObservable.subscribe(mSubjectLoginAvailable);
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(String params) {
        mUserDataRepository.checkIsLoginAvailable(params.trim());
        return mSubjectLoginAvailable;
    }

    @Override
    public void dispose() {
        super.dispose();
        mUserDataRepository.disconnectIsLoginAvailable();
    }
}
