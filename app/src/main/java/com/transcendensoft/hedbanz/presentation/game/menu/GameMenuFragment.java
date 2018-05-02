package com.transcendensoft.hedbanz.presentation.game.menu;
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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.RxRoom;
import com.transcendensoft.hedbanz.presentation.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment that shows sidebar menu with users.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class GameMenuFragment extends BaseFragment implements GameMenuContract.View{
    @BindView(R.id.rvPlayers) RecyclerView mRvPlayers;

    @Inject RxRoom mModel;
    @Inject GameMenuPresenter mPresenter;

    @Inject
    public GameMenuFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_menu, container, false);

        ButterKnife.bind(this, view);

        mPresenter.setModel(mModel);
        initRecycler();

        return view;
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    private void initRecycler(){
       /* mAdapter.setBottomReachedListener(mPresenter);
        initAdapterClickListeners();

        mRecycler.setAdapter(mAdapter);
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        mRecycler.setLayoutManager(manager);
        mRecycler.setItemViewCacheSize(100);*/
    }

    /*------------------------------------*
     *----- Recycler click listeners -----*
     *------------------------------------*/



    @Override
    public void showLoading() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void hideAll() {

    }
}
