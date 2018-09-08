package com.transcendensoft.hedbanz.domain.interactor.game.usecases;
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

import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Advertise;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case listening information about advertise
 * type when server send advertise socket event
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class AdvertiseUseCase extends ObservableUseCase<Message, Void> {
    private PublishSubject<Message> mSubject;

    @Inject
    public AdvertiseUseCase(ObservableTransformer observableTransformer,
                            CompositeDisposable mCompositeDisposable,
                            GameDataRepository gameDataRepository) {
        super(observableTransformer, mCompositeDisposable);

        initSubject(gameDataRepository);
    }

    private void initSubject(GameDataRepository gameDataRepository) {
        Observable<Message> observable = gameDataRepository
                .advertiseObservable()
                .map(this::mapAdvertise);
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    private Message mapAdvertise(Advertise advertise) {
        MessageType messageType;
        switch (advertise) {
            case BANNER:
                messageType = MessageType.ADVERTISE_BANNER;
                break;
            case VIDEO:
                messageType = MessageType.ADVERTISE_VIDEO;
                break;
            case DEVELOPERS:
                messageType = MessageType.ADVERTISE_DEVELOPERS;
                break;
            default:
                messageType = MessageType.UNDEFINED;
        }
        return new Message.Builder()
                .setMessageType(messageType)
                .build();
    }

    @Override
    protected Observable<Message> buildUseCaseObservable(Void params) {
        return mSubject;
    }
}
