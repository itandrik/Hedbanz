package com.transcendensoft.hedbanz.presentation.base;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Base adapter delegate that contains unsubscribe from Rx logic.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class RxAdapterDelegate<T> extends AdapterDelegate<T>{
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        mCompositeDisposable = new CompositeDisposable();
    }

    protected void addDisposable(Disposable disposable){
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(mCompositeDisposable != null && !mCompositeDisposable.isDisposed()){
            mCompositeDisposable.dispose();
        }
    }
}
