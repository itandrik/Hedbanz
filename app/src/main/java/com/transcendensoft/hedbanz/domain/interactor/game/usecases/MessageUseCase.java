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
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

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
public class MessageUseCase extends ObservableUseCase<Message, List<User>> {
    private PublishSubject<Message> mSubject;
    private PreferenceManager mPreferenceManager;
    private GameDataRepository mGameDataRepository;

    @Inject
    public MessageUseCase(ObservableTransformer observableTransformer,
                          CompositeDisposable mCompositeDisposable,
                          GameDataRepositoryImpl gameDataRepository,
                          PreferenceManager preferenceManager) {
        super(observableTransformer, mCompositeDisposable);
        this.mPreferenceManager = preferenceManager;
        this.mGameDataRepository = gameDataRepository;
    }

    @Override
    protected Observable<Message> buildUseCaseObservable(List<User> users) {
        initSubject(users);

        return mSubject;
    }

    private void initSubject(List<User> users) {
        Observable<Message> observable = getObservable(users);
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    private Observable<Message> getObservable(List<User> users) {
        return mGameDataRepository.messageObservable()
                .flatMap(jsonObject -> convertJsonToMessage(jsonObject, users));
    }

    private ObservableSource<? extends Message> convertJsonToMessage(
            JSONObject jsonObject, List<User> users) throws JSONException {
        try {
            Message message = getMessage(jsonObject, users);
            return Observable.just(message);
        } catch (JsonSyntaxException e) {
            return Observable.error(new IncorrectJsonException(
                    jsonObject.toString(), RoomInfoUseCase.class.getName()));
        }
    }

    private Message getMessage(JSONObject jsonObject, List<User> users) throws JSONException {
        User user = getUserWithId(users, jsonObject.getLong("senderId"));

        MessageType messageType;
        if (mPreferenceManager.getUser().getId() == user.getId()) {
            messageType = MessageType.SIMPLE_MESSAGE_THIS_USER;
        } else {
            messageType = MessageType.SIMPLE_MESSAGE_OTHER_USER;
        }

        return new Message.Builder()
                .setId(jsonObject.getLong("id"))
                .setMessage(jsonObject.getString("text"))
                .setMessageType(messageType)
                .setUserFrom(user)
                .setCreateDate(new Timestamp(jsonObject.getLong("createDate")))
                .build();
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