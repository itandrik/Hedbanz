package com.transcendensoft.hedbanz.domain.interactor.game.usecases.typing;
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

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.ObservableUseCase;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;
import com.transcendensoft.hedbanz.domain.interactor.game.usecases.room.RoomInfoUseCase;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import org.json.JSONException;
import org.json.JSONObject;

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
 * that represents a use case listening event when someone starts startTyping message_received now
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class StartTypingUseCase extends ObservableUseCase<User, Room> {
    private PublishSubject<User> mSubject;
    private GameDataRepository mRepository;
    private PreferenceManager mPreferenceManager;

    @Inject
    public StartTypingUseCase(ObservableTransformer observableTransformer,
                              CompositeDisposable mCompositeDisposable,
                              GameDataRepository gameDataRepository,
                              PreferenceManager preferenceManager) {
        super(observableTransformer, mCompositeDisposable);
        mRepository = gameDataRepository;
        mSubject = PublishSubject.create();
        mPreferenceManager = preferenceManager;
    }

    @Override
    protected Observable<User> buildUseCaseObservable(Room params) {
        Observable<User> observable = mRepository.typingObservable()
                .flatMap(jsonObject -> convertUserIdToUserObservable(params.getPlayers(), jsonObject));
        observable.subscribe(mSubject);
        return mSubject;
    }

    private ObservableSource<? extends User> convertUserIdToUserObservable(
            List<User> params, JSONObject jsonObject) {
        try {
            Long userId = jsonObject.getLong("userId");
            User user = getUserWithId(params, userId);

            if(user == null || mPreferenceManager.getUser().equals(user)){
                return Observable.empty();
            } else {
                return Observable.just(user);
            }
        } catch (JSONException e) {
            return Observable.error(new IncorrectJsonException(
                    jsonObject.toString(), RoomInfoUseCase.class.getName()));
        }
    }

    @Nullable
    private User getUserWithId(List<User> users, long id) {
        User result = null;
        if(users != null){
            for (User user : users) {
                if (user.getId() == id) {
                    result = user;
                    break;
                }
            }
        }
        if(result == null){
            result = new User.Builder().setId(id).build();
        }
        return result;
    }
}