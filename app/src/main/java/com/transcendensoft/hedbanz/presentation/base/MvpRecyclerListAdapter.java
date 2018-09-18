package com.transcendensoft.hedbanz.presentation.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Andrii Chernysh
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class MvpRecyclerListAdapter<M, P extends BasePresenter, VH extends MvpViewHolder>
        extends MvpRecyclerAdapter<M, P, VH> {
    private static final String TAG = MvpRecyclerListAdapter.class.getName();
    private final List<M> models;

    public MvpRecyclerListAdapter() {
        models = new ArrayList<>();
    }

    public void clearAndAddAll(Collection<M> data) {
        models.clear();
        presenters.clear();

        for (M item : data) {
            addInternal(item);
        }

        notifyDataSetChanged();
    }

    public void clearAll() {
        models.clear();
        presenters.clear();
        notifyDataSetChanged();
    }

    public void addAll(Collection<M> data) {
        for (M item : data) {
            addInternal(item);
        }

        int addedSize = data.size();
        int oldSize = models.size() - addedSize;
        notifyItemRangeInserted(oldSize, addedSize);
    }

    public void addItem(M item) {
        addInternal(item);
        notifyItemInserted(models.size());
    }

    public void sort(Comparator<M> comparator){
        Collections.sort(models, comparator);
        notifyDataSetChanged();
    }

    public void updateItem(M item) {
        Object modelId = getModelId(item);

        //Swap the model
        int position = getItemPosition(item);
        if (position >= 0) {
            models.remove(position);
            models.add(position, item);
        }

        // Swap the presenter
        P existingPresenter = presenters.get(modelId);
        if (existingPresenter != null) {
            existingPresenter.setModel(item);
        }

        if (position >= 0) {
            notifyItemChanged(position);
        }
    }

    public void removeLastItem() {
        int position = models.size()-1;
        if (position >= 0) {
            M model = models.get(position);
            models.remove(position);
            presenters.remove(model);
            notifyItemRemoved(position);
        }
    }

    public void removeItem(M item) {
        int position = getItemPosition(item);
        if (position >= 0) {
            models.remove(item);
        }
        presenters.remove(getModelId(item));

        if (position >= 0) {
            notifyItemRemoved(position);
        }
    }

    public void removeItemWithId(Integer id) {
        M modelToRemove = null;
        int position = 0;
        for (M model : models) {
            if (getModelId(model).equals(id)) {
                modelToRemove = model;
                position = getItemPosition(modelToRemove);
                break;
            }
        }
        if (modelToRemove != null) {
            removeItem(modelToRemove);
            notifyItemRemoved(position);
        }
    }

    private int getItemPosition(M item) {
        Object modelId = getModelId(item);

        int position = -1;
        for (int i = 0; i < models.size(); i++) {
            M model = models.get(i);
            if (getModelId(model).equals(modelId)) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void addInternal(M item) {
        models.add(item);
        presenters.put(getModelId(item), createPresenter(item));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    protected M getItem(int position) {
        return models.get(position);
    }
}
