package com.transcendensoft.hedbanz.view.fragment;
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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.adapter.RoomsAdapter;
import com.transcendensoft.hedbanz.model.entity.Room;
import com.transcendensoft.hedbanz.presenter.PresenterManager;
import com.transcendensoft.hedbanz.presenter.impl.RoomsPresenterImpl;
import com.transcendensoft.hedbanz.view.RoomsView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows room list.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RoomsFragment extends Fragment implements RoomsView {
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rvRooms) RecyclerView mRecycler;
    @BindView(R.id.rlEmptyListContainer) RelativeLayout mRlEmptyList;
    @BindView(R.id.rlErrorNetwork) RelativeLayout mRlErrorNetwork;
    @BindView(R.id.rlErrorServer) RelativeLayout mRlErrorServer;
    @BindView(R.id.flLoadingContainer) FrameLayout mFlLoadingContainer;

    private RoomsPresenterImpl mPresenter;
    private RoomsAdapter mAdapter;

    /*------------------------------------*
     *-------- Fragment lifecycle --------*
     *------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        ButterKnife.bind(this, view);

        initPresenter(savedInstanceState);
        initSwipeToRefresh();
        initRecycler();
        if(mPresenter != null){
            mPresenter.loadRooms();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unbindView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(mPresenter, outState);
    }

    @Override
    public Context provideContext() {
        return getActivity();
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    private void initPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mPresenter = new RoomsPresenterImpl();
        } else {
            mPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
    }

    private void initSwipeToRefresh(){
        mRefreshLayout.setOnRefreshListener(() -> {
            if(mPresenter != null){
                mPresenter.refreshRooms();
            }
        });
    }

    private void initRecycler(){
        mAdapter = new RoomsAdapter();
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));

        mRecycler.setAdapter(mAdapter);
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void addRoomsToRecycler(List<Room> rooms) {
        if(mAdapter != null){
            mAdapter.addAll(rooms);
        }
    }

    @Override
    public void clearAndAddRoomsToRecycler(List<Room> rooms) {
        if(mAdapter != null) {
            mAdapter.clearAndAddAll(rooms);
        }
    }

    /*------------------------------------*
         *-------- On click listeners --------*
         *------------------------------------*/
    @OnClick(R.id.btnRetryNetwork)
    protected void onRetryNetworkClicked(){
        onRetryServerClicked();
    }

    @OnClick(R.id.btnRetryServer)
    protected void onRetryServerClicked(){
        if(mPresenter != null){
            mPresenter.loadRooms();
        }
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void showServerError() {
        hideAll();
        mRlErrorServer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNetworkError() {
        hideAll();
        mRlErrorNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        hideAll();
        mFlLoadingContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent() {
        hideAll();
        mRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyList() {
        hideAll();
        mRlEmptyList.setVisibility(View.VISIBLE);
    }

    private void hideAll() {
        mRlErrorServer.setVisibility(View.GONE);
        mRlErrorNetwork.setVisibility(View.GONE);
        mFlLoadingContainer.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.GONE);
        mRlEmptyList.setVisibility(View.GONE);
    }
}
