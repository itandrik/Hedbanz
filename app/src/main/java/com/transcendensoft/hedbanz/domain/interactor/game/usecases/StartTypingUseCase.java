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

import com.transcendensoft.hedbanz.data.repository.GameDataRepositoryImpl;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;
import com.transcendensoft.hedbanz.presentation.game.models.TypingMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case listening event when someone starts startTyping message now
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class StartTypingUseCase extends ObservableUseCase<TypingMessage, List<User>> {
    private PublishSubject<TypingMessage> mSubject;
    private GameDataRepository mRepository;

    public StartTypingUseCase(ObservableTransformer observableTransformer,
                              CompositeDisposable mCompositeDisposable,
                              GameDataRepositoryImpl gameDataRepository) {
        super(observableTransformer, mCompositeDisposable);
        mRepository = gameDataRepository;
        mSubject = PublishSubject.create();
    }

    @Override
    protected Observable<TypingMessage> buildUseCaseObservable(List<User> params) {
        Observable<TypingMessage> observable = mRepository.typingObservable()
                .flatMap(jsonObject -> convertUserIdToMessageObservable(params, jsonObject));
        observable.subscribe(mSubject);
        return mSubject;
    }

    private ObservableSource<? extends TypingMessage> convertUserIdToMessageObservable(
            List<User> params, JSONObject jsonObject) {
        try {
            Long userId = jsonObject.getLong("userId");
            return Observable.just(getTypingMessage(params, userId));
        } catch (JSONException e) {
            return Observable.error(new IncorrectJsonException(
                    jsonObject.toString(), RoomInfoUseCase.class.getName()));
        }
    }

    @NonNull
    private TypingMessage getTypingMessage(List<User> params, Long userId) {
        Message message = new Message.Builder()
                .setMessageType(MessageType.STOP_TYPING)
                .setUserFrom(getUserWithId(params, userId))
                .build();
        return new TypingMessage(message);
    }

    @Nullable
    private User getUserWithId(List<User> users, long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }
}