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

import android.support.annotation.NonNull;

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;
import com.transcendensoft.hedbanz.presentation.game.models.RxUser;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case listening event when message_received has received
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class MessageUseCase extends ObservableUseCase<Message, List<RxUser>> {
    private PublishSubject<Message> mSubject;
    private PreferenceManager mPreferenceManager;
    private GameDataRepository mGameDataRepository;

    @Inject
    public MessageUseCase(ObservableTransformer observableTransformer,
                          CompositeDisposable mCompositeDisposable,
                          GameDataRepository gameDataRepository,
                          PreferenceManager preferenceManager) {
        super(observableTransformer, mCompositeDisposable);
        this.mPreferenceManager = preferenceManager;
        this.mGameDataRepository = gameDataRepository;
    }

    @Override
    protected Observable<Message> buildUseCaseObservable(List<RxUser> rxPlayers) {
        initSubject(rxPlayers);

        return mSubject;
    }

    private void initSubject(List<RxUser> rxPlayers) {
        Observable<Message> observable = getObservable(rxPlayers);
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    private Observable<Message> getObservable(List<RxUser> rxPlayers) {
        return mGameDataRepository.messageObservable()
                .map(message -> mapMessageFullData(message, rxPlayers));
    }

    @NonNull
    private Message mapMessageFullData(Message message, List<RxUser> rxPlayers) {
        if (mPreferenceManager.getUser().equals(message.getUserFrom())) {
            message.setMessageType(MessageType.SIMPLE_MESSAGE_THIS_USER);
            message.setFinished(true);
            message.setLoading(false);
        } else {
            message.setMessageType(MessageType.SIMPLE_MESSAGE_OTHER_USER);

            if (message.getUserFrom() != null) {
                RxUser rxUser = getRxUser(rxPlayers, message.getUserFrom().getId());
                if (rxUser != null) {
                    message.getUserFrom().setWord(rxUser.getUser().getWord());
                    message.getUserFrom().setWinner(rxUser.getUser().isWinner());
                }
            }
        }

        return message;
    }

    @Nullable
    private RxUser getRxUser(List<RxUser> rxPlayers, long userId) {
        for (RxUser rxUser : rxPlayers) {
            if (rxUser.getUser().getId() == userId) {
                return rxUser;
            }
        }
        return null;
    }
}