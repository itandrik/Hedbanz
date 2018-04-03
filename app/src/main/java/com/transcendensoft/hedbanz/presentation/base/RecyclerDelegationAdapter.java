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

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Base recycler view adapter based on delegate, that contains base methods operating views.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class RecyclerDelegationAdapter<T> extends ListDelegationAdapter<List<T>> {
    public RecyclerDelegationAdapter() {
        setItems(new ArrayList<>());
    }

    public void clearAndAddAll(List<T> entities){
        setItems(entities);
        notifyDataSetChanged();
    }

    public void addAll(List<T> entities){
        int startPosition = getItems().size() - 1;
        getItems().addAll(entities);
        notifyItemRangeChanged(startPosition, startPosition + entities.size());
    }

    public void clear(){
        getItems().clear();
        notifyDataSetChanged();
    }

    public void add(T entity){
        getItems().add(entity);
        notifyItemInserted(getItems().size());
    }

    public void add(int position, T entity){
        getItems().add(position, entity);
        notifyItemInserted(position);
    }

    public void remove(int position){
        getItems().remove(position);
        notifyItemRemoved(position);
    }

    public void update(int position, T entity){
        getItems().set(position, entity);
        notifyItemChanged(position);
    }

    public T getItem(int position){
        return getItems().get(position);
    }
}
