package com.transcendensoft.hedbanz.presentation.base;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Andrii Chernysh
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class BasePresenter<M,V> {
    protected M model;
    private WeakReference<V> view;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public void setModel(M model) {
        resetState();
        this.model = model;
        if (setupDone()) {
            updateView();
        }
    }

    protected void resetState() {
    }

    public void bindView(@NonNull V view) {
        this.view = new WeakReference<>(view);
        if (setupDone()) {
            updateView();
        }
    }

    public void unbindView() {
        this.view = null;

        disposeAll();
    }

    public void disposeAll(){

        if(mCompositeDisposable != null && !mCompositeDisposable.isDisposed()){
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
            mCompositeDisposable = new CompositeDisposable();
        }
    }

    protected void addDisposable(Disposable disposable){
        if(mCompositeDisposable != null && !mCompositeDisposable.isDisposed()){
            mCompositeDisposable.add(disposable);
        }
    }

    protected V view() {
        if (view == null) {
            return null;
        } else {
            return view.get();
        }
    }

    protected abstract void updateView();

    protected boolean setupDone() {
        return view() != null && model != null;
    }

    public abstract void destroy();
}
