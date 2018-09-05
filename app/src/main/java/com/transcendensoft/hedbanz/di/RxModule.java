package com.transcendensoft.hedbanz.di;
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

import com.transcendensoft.hedbanz.di.qualifier.SchedulerComputation;
import com.transcendensoft.hedbanz.di.qualifier.SchedulerIO;
import com.transcendensoft.hedbanz.di.qualifier.SchedulerUI;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;
import io.reactivex.CompletableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Scheduler provider module
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
@Module
public class RxModule {
    @ApplicationScope
    @SchedulerComputation
    @Provides
    public Scheduler provideComputationScheduler() {
        return Schedulers.computation();
    }

    @ApplicationScope
    @SchedulerIO
    @Provides
    public Scheduler provideIoScheduler() {
        return Schedulers.io();
    }

    @ApplicationScope
    @SchedulerUI
    @Provides
    public Scheduler provideUiScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @ApplicationScope
    @Provides
    public ObservableTransformer provideObservableTransformer(
            @SchedulerIO Scheduler ioScheduler, @SchedulerUI Scheduler uiScheduler) {
        return o -> o
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler);
    }

    @ApplicationScope
    @Provides
    public CompletableTransformer provideCompletableTransformer(
            @SchedulerIO Scheduler ioScheduler, @SchedulerUI Scheduler uiScheduler) {
        return o -> o
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler);
    }

    @ApplicationScope
    @Provides
    public SingleTransformer provideSingleTransformer(
            @SchedulerIO Scheduler ioScheduler, @SchedulerUI Scheduler uiScheduler) {
        return o -> o
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler);
    }

    @ApplicationScope
    @Provides
    public MaybeTransformer provideMaybeTransformer(
            @SchedulerIO Scheduler ioScheduler, @SchedulerUI Scheduler uiScheduler) {
        return o -> o
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler);
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
