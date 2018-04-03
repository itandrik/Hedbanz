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

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 * <p>
 * By convention each UseCase implementation will return the result using a
 * {@link io.reactivex.observers.DisposableObserver} that will execute its
 * job in a background thread and will post the result in the UI thread.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */

public abstract class UseCase {
    protected static final String NULL_DISPOSABLE_OBSERVER_MSG = "Disposable observer is NULL." +
            " Check your Interactor execution within Presenter";
    private static final String NULL_COMPOSITE_DISPOSABLE_MSG = "Composite disposable is null." +
            " Check Dagger 2 dependencies.";

    private final CompositeDisposable mCompositeDisposable;

    public UseCase(CompositeDisposable mCompositeDisposable) {
        this.mCompositeDisposable = mCompositeDisposable;
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    public void dispose() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    protected void addDisposable(Disposable disposable) {
        if (disposable == null) {
            throw new NullPointerException();
        }
        if (mCompositeDisposable == null) {
            throw new NullPointerException(NULL_COMPOSITE_DISPOSABLE_MSG);
        }
        mCompositeDisposable.add(disposable);
    }
}
