package com.transcendensoft.hedbanz.domain.interactor.game.usecases.room;
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
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case listening {@link Room}
 * information after connecting to socket.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class RoomInfoUseCase extends ObservableUseCase<Room, Void> {
    private PublishSubject<Room> mSubject;

    @Inject
    public RoomInfoUseCase(ObservableTransformer observableTransformer,
                           CompositeDisposable mCompositeDisposable,
                           GameDataRepository gameDataRepository,
                           Gson gson) {
        super(observableTransformer, mCompositeDisposable);

        initSubject(gameDataRepository, gson);
    }

    private void initSubject(GameDataRepository gameDataRepository, Gson gson) {
        Observable<Room> observable = getObservable(gameDataRepository, gson);
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    private Observable<Room> getObservable(GameDataRepository gameDataRepository, Gson gson) {
        return gameDataRepository.roomInfoObservable()
                .flatMap(jsonObject -> {
                    try {
                        Room room = gson.fromJson(jsonObject.toString(), Room.class);
                        return Observable.just(room);
                    } catch (JsonSyntaxException e) {
                        return Observable.error(new IncorrectJsonException(
                                jsonObject.toString(), RoomInfoUseCase.class.getName()));
                    }
                });
    }

    @Override
    protected Observable<Room> buildUseCaseObservable(Void params) {
        return mSubject;
    }
}
