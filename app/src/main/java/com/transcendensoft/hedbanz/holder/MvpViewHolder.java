package com.transcendensoft.hedbanz.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.transcendensoft.hedbanz.presenter.BasePresenter;

/**
 * @author Andrii Chernysh
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class MvpViewHolder<P extends BasePresenter> extends RecyclerView.ViewHolder {
    protected P presenter;

    public MvpViewHolder(View itemView) {
        super(itemView);
    }

    public void bindPresenter(P presenter){
        this.presenter = presenter;
        presenter.bindView(this);
    }

    public void unbindPresenter(){
        presenter = null;
    }
}
