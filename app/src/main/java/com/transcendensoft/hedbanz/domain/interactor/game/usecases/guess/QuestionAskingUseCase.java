package com.transcendensoft.hedbanz.domain.interactor.game.usecases.guess;
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
import com.transcendensoft.hedbanz.domain.entity.Question;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case listening {@link com.transcendensoft.hedbanz.domain.entity.Question}
 * that represents guess question of some user
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class QuestionAskingUseCase extends ObservableUseCase<Question, List<User>> {
    private PublishSubject<Question> mSubject;
    private GameDataRepository mGameDataRepository;
    @Inject
    public QuestionAskingUseCase(ObservableTransformer schedulersTransformer,
                                 CompositeDisposable compositeDisposable,
                                 GameDataRepository gameDataRepository) {
        super(schedulersTransformer, compositeDisposable);
        mGameDataRepository = gameDataRepository;
        mSubject = PublishSubject.create();
    }

    @Override
    protected Observable<Question> buildUseCaseObservable(List<User> users) {
        initSubject(users);
        return mSubject;
    }

    private void initSubject(List<User> users) {
        Observable<Question> observable = getObservable(users);
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    private Observable<Question> getObservable(List<User> users) {
        return mGameDataRepository.questionAskingObservable()
                .map(question -> {
                    User userWithWord = getUserWithId(users, question.getUserFrom().getId());
                    question.setUserFrom(userWithWord);
                    question.setLoading(false);
                    question.setFinished(true);
                    return question;
                });
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
