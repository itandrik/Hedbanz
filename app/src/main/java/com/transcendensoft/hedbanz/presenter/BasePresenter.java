package com.transcendensoft.hedbanz.presenter;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Andrii Chernysh
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class BasePresenter<M,V> {
    protected M model;
    private WeakReference<V> view;
    private CompositeDisposable mCompositeDisposable;

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

        mCompositeDisposable = new CompositeDisposable();
    }

    public void unbindView() {
        this.view = null;

        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
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
}
