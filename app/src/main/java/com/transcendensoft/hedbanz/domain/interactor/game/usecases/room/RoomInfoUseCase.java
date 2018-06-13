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

import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Room;
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
                           GameDataRepository gameDataRepository) {
        super(observableTransformer, mCompositeDisposable);

        initSubject(gameDataRepository);
    }

    private void initSubject(GameDataRepository gameDataRepository) {
        Observable<Room> observable = gameDataRepository.roomInfoObservable();
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    @Override
    protected Observable<Room> buildUseCaseObservable(Void params) {
        return mSubject;
    }
}
