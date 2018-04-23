package com.transcendensoft.hedbanz.domain.interactor.game.usecases;
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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.transcendensoft.hedbanz.data.models.common.ServerError;
import com.transcendensoft.hedbanz.data.repository.GameDataRepositoryImpl;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case listening {@link com.transcendensoft.hedbanz.data.models.common.ServerError}
 * information when some user connects to the room
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class ErrorUseCase extends ObservableUseCase<ServerError, Void> {
    private PublishSubject<ServerError> mSubject;

    @Inject
    public ErrorUseCase(ObservableTransformer observableTransformer,
                             CompositeDisposable mCompositeDisposable,
                             GameDataRepositoryImpl gameDataRepository,
                             Gson gson) {
        super(observableTransformer, mCompositeDisposable);

        initSubject(gameDataRepository, gson);
    }

    private void initSubject(GameDataRepositoryImpl gameDataRepository, Gson gson) {
        Observable<ServerError> observable = getObservable(gameDataRepository, gson);
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    private Observable<ServerError> getObservable(GameDataRepositoryImpl gameDataRepository, Gson gson) {
        return gameDataRepository.errorObservable()
                .flatMap(jsonObject -> {
                    try {
                        ServerError error = gson.fromJson(jsonObject.toString(), ServerError.class);
                        return Observable.just(error);
                    } catch (JsonSyntaxException e) {
                        return Observable.error(new IncorrectJsonException(
                                jsonObject.toString(), RoomInfoUseCase.class.getName()));
                    }
                });
    }

    @Override
    protected Observable<ServerError> buildUseCaseObservable(Void params) {
        return mSubject;
    }
}
