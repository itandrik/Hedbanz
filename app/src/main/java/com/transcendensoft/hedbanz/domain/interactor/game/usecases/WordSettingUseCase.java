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

import com.transcendensoft.hedbanz.data.models.WordDTO;
import com.transcendensoft.hedbanz.data.repository.GameDataRepositoryImpl;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case listening event when needs to
 * set word to some user
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 **/
public class WordSettingUseCase extends ObservableUseCase<User, List<User>>{
    private PublishSubject<User> mSubject;
    private GameDataRepository mRepository;

    @Inject
    public WordSettingUseCase(ObservableTransformer schedulersTransformer,
                             CompositeDisposable compositeDisposable,
                             GameDataRepositoryImpl gameDataRepository) {
        super(schedulersTransformer, compositeDisposable);
        mRepository = gameDataRepository;
        mSubject = PublishSubject.create();
    }

    @Override
    protected Observable<User> buildUseCaseObservable(List<User> params) {
        Observable<User> observable = mRepository.settingWordObservable()
                .flatMap(jsonObject -> convertJsonToUserObservable(params, jsonObject));
        observable.subscribe(mSubject);
        return mSubject;
    }

    private Observable<User> convertJsonToUserObservable(List<User> users, JSONObject jsonObject){
        try {
            Long wordReceiverId = jsonObject.getLong(WordDTO.WORD_RECEIVER_ID);
            User wordReceiverUser = getUserWithId(users, wordReceiverId);

            return Observable.just(wordReceiverUser);
        } catch (JSONException e) {
            return Observable.error(new IncorrectJsonException(
                    jsonObject.toString(), WordSettedUseCase.class.getName()));
        }
    }

    private User getUserWithId(List<User> users, long id) {
        User user = null;
        for (User innerUser : users) {
            if (innerUser.getId() == id) {
                user = innerUser;
            }
        }
        if(user == null){
            user = new User.Builder().setId(id).build();
        }
        return user;
    }
}
