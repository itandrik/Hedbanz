package com.transcendensoft.hedbanz.domain;
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

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * This use case executes some command with
 * {@link io.reactivex.Completable} boundaries
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class CompletableUseCase<PARAM> extends UseCase {
    private final CompletableTransformer mCompletableTransformer;
    public CompletableUseCase(CompletableTransformer completableTransformer,
                              CompositeDisposable mCompositeDisposable) {
        super(mCompositeDisposable);
        this.mCompletableTransformer = completableTransformer;
    }

    /**
     * Builds an Observable which will be
     * used when executing the current {@link UseCase}.
     */
    protected abstract Completable buildUseCaseCompletable(PARAM params);

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, Action onComplete) {
        final Completable completable = this.buildUseCaseCompletable(params)
                .compose(mCompletableTransformer);

        addDisposable(completable.subscribe(onComplete));
    }

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, Action onComplete, Consumer<? super Throwable> onError) {
        final Completable completable = this.buildUseCaseCompletable(params)
                .compose(mCompletableTransformer);

        addDisposable(completable.subscribe(onComplete, onError));
    }
}
