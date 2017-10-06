package com.transcendensoft.hedbanz.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.transcendensoft.hedbanz.holder.MvpViewHolder;
import com.transcendensoft.hedbanz.presenter.BasePresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrii Chernysh
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class MvpRecyclerAdapter<M, P extends BasePresenter, VH extends MvpViewHolder>
        extends RecyclerView.Adapter<VH> {
    private static final String TAG = MvpRecyclerAdapter.class.getName();
    protected final Map<Object, P> presenters;

    public MvpRecyclerAdapter() {
        presenters = new HashMap<>();
    }

    @NonNull
    protected P getPresenter(@NonNull M model) {
        Log.d(TAG, "Getting presenter for item " + getModelId(model));
        return presenters.get(getModelId(model));
    }

    @NonNull
    protected abstract P createPresenter(@NonNull M model);

    @NonNull
    protected abstract Object getModelId(@NonNull M model);

    protected abstract M getItem(int position);

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);

        holder.unbindPresenter();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindPresenter(getPresenter(getItem(position)));
    }

    @Override
    public boolean onFailedToRecycleView(VH holder) {
        // Sometimes, if animations are running on the itemView's children, the RecyclerView won't
        // be able to recycle the view. We should still unbind the presenter.
        holder.unbindPresenter();

        return super.onFailedToRecycleView(holder);
    }
}
