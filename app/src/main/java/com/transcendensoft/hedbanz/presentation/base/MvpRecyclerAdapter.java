package com.transcendensoft.hedbanz.presentation.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
    @Nullable OnBottomReachedListener mBottomReachedListener;
    @Nullable OnTopReachedListener mTopReachedListener;

    public interface OnBottomReachedListener{
        void onBottomReached(MvpViewHolder holder);
    }

    public interface  OnTopReachedListener{
        void onTopReached(MvpViewHolder holder);
    }

    public MvpRecyclerAdapter() {
        presenters = new HashMap<>();
    }

    @NonNull
    protected P getPresenter(@NonNull M model) {
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

        if(mBottomReachedListener != null && position == presenters.size() - 1){
            mBottomReachedListener.onBottomReached(holder);
        }

        if(mTopReachedListener != null && position <= 0){
            mTopReachedListener.onTopReached(holder);
        }
    }

    @Override
    public boolean onFailedToRecycleView(VH holder) {
        // Sometimes, if animations are running on the itemView's children, the RecyclerView won't
        // be able to recycle the view. We should still unbind the presenter.
        holder.unbindPresenter();

        return super.onFailedToRecycleView(holder);
    }

    public void setBottomReachedListener(@Nullable OnBottomReachedListener mBottomReachedListener) {
        this.mBottomReachedListener = mBottomReachedListener;
    }

    public void setTopReachedListener(@Nullable OnTopReachedListener topReachedListener) {
        this.mTopReachedListener = topReachedListener;
    }
}
