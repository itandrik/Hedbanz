package com.transcendensoft.hedbanz.domain.interactor.game.usecases.connect;
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
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case listening socket reconnection error.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class OnReconnectErrorUseCase extends ObservableUseCase<String, Void> {
    private PublishSubject<String> mSubject;

    @Inject
    public OnReconnectErrorUseCase(ObservableTransformer observableTransformer,
                              CompositeDisposable mCompositeDisposable,
                              GameDataRepository gameDataRepository) {
        super(observableTransformer, mCompositeDisposable);

        Observable<String> observable = gameDataRepository.reconnectErrorObservable();
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    @Override
    protected Observable<String> buildUseCaseObservable(Void params) {
        return mSubject;
    }
}

