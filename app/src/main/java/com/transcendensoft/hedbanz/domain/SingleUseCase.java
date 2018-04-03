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

import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 * <p>
 * This use case executes some command with
 * {@link io.reactivex.Single} boundaries
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public abstract class SingleUseCase<T, PARAM> extends UseCase {
    private final SingleTransformer mSingleTransformer;

    public SingleUseCase(SingleTransformer singleTransformer,
                         CompositeDisposable mCompositeDisposable) {
        super(mCompositeDisposable);
        this.mSingleTransformer = singleTransformer;
    }

    /**
     * Builds an Observable which will be
     * used when executing the current {@link UseCase}.
     */
    protected abstract Single<T> buildUseCaseSingle(PARAM params);

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, Consumer<T> onSuccess) {
        final Single<T> single = this.buildUseCaseSingle(params)
                .compose(applySchedulers());

        addDisposable(single.subscribe(onSuccess));
    }

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, Consumer<T> onSuccess, Consumer<? super Throwable> onError) {
        final Single<T> single = this.buildUseCaseSingle(params)
                .compose(applySchedulers());

        addDisposable(single.subscribe(onSuccess, onError));
    }

    @SuppressWarnings("unchecked")
    private <S> SingleTransformer<S, S> applySchedulers() {
        return (SingleTransformer<S, S>) mSingleTransformer;
    }
}
