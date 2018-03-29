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

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * This use case executes some command with
 * {@link io.reactivex.Observable} boundaries
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class ObservableUseCase<T, PARAM> extends UseCase{
    private final ObservableTransformer mSchedulersTransformer;

    public ObservableUseCase(ObservableTransformer schedulersTransformer,
                             CompositeDisposable compositeDisposable) {
        super(compositeDisposable);
        this.mSchedulersTransformer = schedulersTransformer;
    }

    /**
     * Builds an Observable which will be
     * used when executing the current {@link UseCase}.
     */
    protected abstract Observable<T> buildUseCaseObservable(PARAM params);

    /**
     * Executes the current use case.
     */
    public void execute(DisposableObserver<T> disposableObserver, PARAM params) {
        if (disposableObserver == null) {
            throw new NullPointerException(NULL_DISPOSABLE_OBSERVER_MSG);
        }
        final Observable<T> observable = this.buildUseCaseObservable(params)
                .compose(applySchedulers());

        addDisposable(observable.subscribeWith(disposableObserver));
    }

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, @NonNull Consumer<? super T> onNext,
                        @NonNull Consumer<? super Throwable> onError,
                        @NonNull Action onComplete) {
        final Observable<T> observable = this.buildUseCaseObservable(params)
                .compose(applySchedulers());

        addDisposable(observable.subscribe(onNext, onError, onComplete));
    }

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, @NonNull Consumer<? super T> onNext,
                        @NonNull Consumer<? super Throwable> onError,
                        @NonNull Action onComplete,
                        @NonNull Consumer<? super Disposable> onSubscribe) {
        final Observable<T> observable = this.buildUseCaseObservable(params)
                .compose(applySchedulers());

        addDisposable(observable.subscribe(onNext, onError, onComplete, onSubscribe));
    }

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, @NonNull Consumer<? super T> onNext,
                        @NonNull Consumer<? super Throwable> onError) {
        final Observable<T> observable = this.buildUseCaseObservable(params)
                .compose(applySchedulers());

        addDisposable(observable.subscribe(onNext, onError));
    }

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, @NonNull Consumer<? super T> onNext) {
        final Observable<T> observable = this.buildUseCaseObservable(params)
                .compose(applySchedulers());

        addDisposable(observable.subscribe(onNext));
    }

    /**
     * Executes the current use case without result to Presenter
     */
    public void execute(PARAM params) {
        final Observable<T> observable = this.buildUseCaseObservable(params)
                .compose(applySchedulers());

        addDisposable(observable.subscribe());
    }

    @SuppressWarnings("unchecked")
    private <S> ObservableTransformer<S, S> applySchedulers() {
        return (ObservableTransformer<S, S>) mSchedulersTransformer;
    }
}
