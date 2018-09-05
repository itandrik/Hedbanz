package com.transcendensoft.hedbanz.domain;

import io.reactivex.Maybe;
import io.reactivex.MaybeTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 * <p>
 * This use case executes some command with
 * {@link io.reactivex.Maybe} boundaries
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public abstract class MaybeUseCase<T, PARAM> extends UseCase {
    private final MaybeTransformer mMaybeTransformer;

    public MaybeUseCase(MaybeTransformer maybeTransformer,
                        CompositeDisposable mCompositeDisposable) {
        super(mCompositeDisposable);
        this.mMaybeTransformer = maybeTransformer;
    }

    /**
     * Builds an Observable which will be
     * used when executing the current {@link UseCase}.
     */
    protected abstract Maybe<T> buildUseCaseMaybe(PARAM params);

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, Consumer<T> onSuccess) {
        final Maybe<T> maybe = this.buildUseCaseMaybe(params)
                .compose(applySchedulers());

        addDisposable(maybe.subscribe(onSuccess));
    }

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, Consumer<T> onSuccess, Consumer<? super Throwable> onError) {
        final Maybe<T> maybe = this.buildUseCaseMaybe(params)
                .compose(applySchedulers());

        addDisposable(maybe.subscribe(onSuccess, onError));
    }

    /**
     * Executes the current use case.
     */
    public void execute(PARAM params, Consumer<T> onSuccess, Consumer<? super Throwable> onError, Action onComplete) {
        final Maybe<T> maybe = this.buildUseCaseMaybe(params)
                .compose(applySchedulers());

        addDisposable(maybe.subscribe(onSuccess, onError, onComplete));
    }

    @SuppressWarnings("unchecked")
    private <S> MaybeTransformer<S, S> applySchedulers() {
        return (MaybeTransformer<S, S>) mMaybeTransformer;
    }
}
