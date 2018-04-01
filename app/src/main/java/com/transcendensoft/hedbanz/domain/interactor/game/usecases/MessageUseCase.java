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

import com.google.gson.JsonSyntaxException;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.data.repository.GameDataRepositoryImpl;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case listening event when message has received
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class MessageUseCase extends ObservableUseCase<Message, Void> {
    private PublishSubject<Message> mSubject;
    private PreferenceManager mPreferenceManager;

    public MessageUseCase(ObservableTransformer observableTransformer,
                          CompositeDisposable mCompositeDisposable,
                          GameDataRepositoryImpl gameDataRepository,
                          PreferenceManager preferenceManager) {
        super(observableTransformer, mCompositeDisposable);
        this.mPreferenceManager = preferenceManager;

        initSubject(gameDataRepository);
    }

    private void initSubject(GameDataRepositoryImpl gameDataRepository) {
        Observable<Message> observable = getObservable(gameDataRepository);
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    private Observable<Message> getObservable(GameDataRepositoryImpl gameDataRepository) {
        return gameDataRepository.messageObservable()
                .flatMap(this::convertJsonToMessage);
    }

    private ObservableSource<? extends Message> convertJsonToMessage(JSONObject jsonObject) throws JSONException {
        try {
            Message message = getMessage(jsonObject);
            return Observable.just(message);
        } catch (JsonSyntaxException e) {
            return Observable.error(new IncorrectJsonException(
                    jsonObject.toString(), RoomInfoUseCase.class.getName()));
        }
    }

    private Message getMessage(JSONObject jsonObject) throws JSONException {
        User user = new User.Builder()
                .setId(jsonObject.getLong("senderId"))
                .build();
        MessageType messageType = MessageType
                .getMessageTypeById(jsonObject.getInt("type"));

        return new Message.Builder()
                .setId(jsonObject.getLong("id"))
                .setMessage(jsonObject.getString("text"))
                .setMessageType(messageType)
                .setUserFrom(user)
                .setCreateDate(new Timestamp(jsonObject.getLong("createDate")))
                .build();
    }

    @Override
    protected Observable<Message> buildUseCaseObservable(Void params) {
        return mSubject;
    }
}