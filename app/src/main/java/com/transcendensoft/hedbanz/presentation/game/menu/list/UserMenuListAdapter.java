package com.transcendensoft.hedbanz.presentation.game.menu.list;
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

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.MvpRecyclerListAdapter;
import com.transcendensoft.hedbanz.presentation.game.models.RxUser;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;

/**
 * List adapter for users in menu side bar
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class UserMenuListAdapter extends MvpRecyclerListAdapter
        <RxUser, UserMenuItemPresenter, UserMenuItemViewHolder> {
    private ObservableTransformer mSchedulersTransformer;
    private PublishSubject<User> mItemClickListener = PublishSubject.create();

    @Inject
    public UserMenuListAdapter(ObservableTransformer schedulersTransformer) {
        this.mSchedulersTransformer = schedulersTransformer;
    }

    @NonNull
    @Override
    protected UserMenuItemPresenter createPresenter(@NonNull RxUser model) {
        UserMenuItemPresenter presenter = new UserMenuItemPresenter(mSchedulersTransformer);
        presenter.setModel(model);
        presenter.getItemClickObservable().subscribe(mItemClickListener);
        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull RxUser model) {
        return model.getUser().getId();
    }

    @NonNull
    @Override
    public UserMenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player_menu, parent, false);
        return new UserMenuItemViewHolder(parent.getContext(), itemView);
    }

    public Observable<User> getItemClickListener() {
        return mItemClickListener;
    }
}
