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
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Question;
import com.transcendensoft.hedbanz.domain.repository.GameDataRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case listening {@link com.transcendensoft.hedbanz.domain.entity.Question}
 * that represents voting for question of some user
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class QuestionVotingUseCase extends ObservableUseCase<Question, Void> {
    private PublishSubject<Question> mSubject;

    @Inject
    public QuestionVotingUseCase(ObservableTransformer schedulersTransformer,
                                 CompositeDisposable compositeDisposable,
                                 GameDataRepository gameDataRepository) {
        super(schedulersTransformer, compositeDisposable);

        initSubject(gameDataRepository);
    }

    private void initSubject(GameDataRepository gameDataRepository) {
        Observable<Question> observable = gameDataRepository.questionVotingObservable()
                .map(question -> {
                    question.setMessageType(MessageType.VOTING_FOR_QUESTION);
                    return question;
                });
        mSubject = PublishSubject.create();
        observable.subscribe(mSubject);
    }

    @Override
    protected Observable<Question> buildUseCaseObservable(Void params) {
        return mSubject;
    }
}
