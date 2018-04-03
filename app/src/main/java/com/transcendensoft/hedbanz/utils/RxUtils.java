package com.transcendensoft.hedbanz.utils;
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

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * Utility class that contains Reactive methods
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RxUtils {
    public static <T> ObservableTransformer<T, T> debounceFirst(long timeout, TimeUnit unit) {
        return f ->
                f.publish(g -> g.take(1)
                        .concatWith(
                                g.switchMap(u -> Observable.timer(timeout, unit).map(w -> u))
                                        .take(1)
                                        .ignoreElements()
                                        .toObservable()
                        )
                        .repeatWhen(h -> h.takeUntil(g.ignoreElements().toObservable()))
                );
    }
}
