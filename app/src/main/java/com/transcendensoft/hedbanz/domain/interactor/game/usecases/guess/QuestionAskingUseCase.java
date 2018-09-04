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
 * that represents a use case listening {@link com.transcendensoft.hedbanz.domain.entity.Question}
 * that represents guess question of some user
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class QuestionAskingUseCase extends ObservableUseCase<Question, List<RxUser>> {
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
    protected Observable<Question> buildUseCaseObservable(List<RxUser> rxUsers) {
        initSubject(rxUsers);
        return mSubject;
    }

    private void initSubject(List<RxUser> users) {
        Observable<Question> observable = getObservable(users);
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    private Observable<Question> getObservable(List<RxUser> users) {
        return mGameDataRepository.questionAskingObservable()
                .map(question -> {
                    RxUser userWithWord = getRxUser(users, question.getUserFrom().getId());
                    if(userWithWord != null && userWithWord.getUser() != null) {
                        question.setUserFrom(userWithWord.getUser());
                    }
                    question.setLoading(false);
                    question.setFinished(true);
                    return question;
                });
    }

    @Nullable
    private RxUser getRxUser(List<RxUser> rxUsers, Long userId) {
        for (RxUser rxUser : rxUsers) {
            if (rxUser.getUser().getId().equals(userId)) {
                return rxUser;
            }
        }
        return null;
    }
}
