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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.adapter.RoomsAdapter;
import com.transcendensoft.hedbanz.model.entity.Room;
import com.transcendensoft.hedbanz.model.entity.RoomFilter;
import com.transcendensoft.hedbanz.presenter.PresenterManager;
import com.transcendensoft.hedbanz.presenter.impl.RoomsPresenterImpl;
import com.transcendensoft.hedbanz.view.RoomsView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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

    /**
     * Searching and filters
     */
    @BindView(R.id.cvFilters) CardView mCvFilters;
    @BindView(R.id.fabSearchRoom) FloatingActionButton mFabSearch;
    @BindView(R.id.chbApplyFilters) CheckBox mChbApplyFilters;
    @BindView(R.id.chbWithPassword) CheckBox mChbWithPassword;
    @BindView(R.id.spinnerFromPlayers) Spinner mSpinnerFromPlayers;
    @BindView(R.id.spinnerToPlayers) Spinner mSpinnerToPlayers;
    private RelativeLayout mRlSearchContainer;
    private TextView mTvToolbarTitle;
    private SearchView mSvRoomSearch;
    private ImageView mIvSearchFilter;
    private ImageView mIvCloseSearch;

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
        initSearchView();
        initFilters();

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
            mPresenter.setModel(new ArrayList<>());
        } else {
            mPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
    }

    private void initSwipeToRefresh() {
        mRefreshLayout.setColorSchemeResources(R.color.textDarkRed,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        mRefreshLayout.setOnRefreshListener(() -> {
            if (mPresenter != null) {
                String searchText = mSvRoomSearch.getQuery().toString();
                if(TextUtils.isEmpty(searchText)) {
                    mPresenter.refreshRooms();
                } else {
                    filterRoomsWithText(searchText);
                }
            }
        });
    }

    private void initRecycler() {
        mAdapter = new RoomsAdapter(mPresenter);
        mAdapter.setBottomReachedListener(mPresenter);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && mFabSearch.isShown()) {
                    mFabSearch.hide();
                } else if ((dy < 0) && (mRlSearchContainer.getVisibility() != VISIBLE)) {
                    mFabSearch.show();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void initSearchView() {
        MainFragment mainFragment = (MainFragment) getParentFragment();
        if (mainFragment != null) {
            Toolbar toolbar = mainFragment.getToolbar();
            if (toolbar != null) {
                mTvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
                mRlSearchContainer = toolbar.findViewById(R.id.rlSearchContainer);
                mIvCloseSearch = toolbar.findViewById(R.id.ivCloseSearch);
                mIvSearchFilter = toolbar.findViewById(R.id.ivSearchFilter);
                mSvRoomSearch = toolbar.findViewById(R.id.svSearchRoom);

                initSearchOnClickListeners();
            }
        }
    }

    private void initSearchOnClickListeners() {
        mIvCloseSearch.setOnClickListener(v -> {
            onCloseSearchClicked();
        });

        mIvSearchFilter.setOnClickListener(v -> {
            if (mCvFilters.getVisibility() == VISIBLE) {
                mCvFilters.setVisibility(GONE);
            } else {
                mCvFilters.setVisibility(VISIBLE);
            }
        });
    }

    private void initFilters() {
        RxSearchView.queryTextChanges(mSvRoomSearch)
                .debounce(350, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .skip(2)
                .filter(text -> !text.equals("#"))
                .subscribe(text -> {
                    if (mPresenter != null) {
                        if(!TextUtils.isEmpty(text)) {
                            filterRoomsWithText(text);
                        } else {
                            mPresenter.clearFiltersAndText();
                            mPresenter.refreshRooms();
                        }
                    }
                });

        RxCompoundButton.checkedChanges(mChbApplyFilters)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isChecked -> {
                    if(isChecked){
                        RoomFilter roomFilter = new RoomFilter.Builder()
                                .setIsPrivate(mChbWithPassword.isChecked())
                                .setMinPlayers(Byte.parseByte(mSpinnerFromPlayers.getSelectedItem().toString()))
                                .setMaxPlayers(Byte.parseByte(mSpinnerToPlayers.getSelectedItem().toString()))
                                .build();
                        mPresenter.filterRooms(roomFilter);
                    } else {
                        clearRooms();
                    }
                });

        List<String> playersQuantity =
                Arrays.asList("2","3","4","5","6","7","8");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item, playersQuantity);
        mSpinnerFromPlayers.setAdapter(adapter);
        mSpinnerToPlayers.setAdapter(adapter);
        mSpinnerToPlayers.setSelection(6);

    }

    private void filterRoomsWithText(CharSequence text) {
        RoomFilter roomFilter = new RoomFilter.Builder()
                .setRoomName(text.toString())
                .build();
        mPresenter.filterRooms(roomFilter);
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void addRoomsToRecycler(List<Room> rooms) {
        if (mAdapter != null) {
            mAdapter.addAll(rooms);
        }
    }

    @Override
    public void clearRooms() {
        if (mAdapter != null) {
            mAdapter.clearAll();
        }
    }

    @Override
    public void removeLastRoom() {
        if (mAdapter != null) {
            mAdapter.removeLastItem();
        }
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.btnRetryNetwork)
    public void onRetryNetworkClicked() {
        onRetryServerClicked();
    }

    @OnClick(R.id.btnRetryServer)
    protected void onRetryServerClicked() {
        if (mPresenter != null) {
            mPresenter.refreshRooms();
        }
    }

    @OnClick(R.id.fabSearchRoom)
    protected void onFabSearchClicked() {
        mTvToolbarTitle.setVisibility(GONE);
        mRlSearchContainer.setVisibility(View.VISIBLE);
        mSvRoomSearch.onActionViewExpanded();
        mFabSearch.hide();
    }

    public void onCloseSearchClicked() {
        mTvToolbarTitle.setVisibility(View.VISIBLE);
        mRlSearchContainer.setVisibility(View.GONE);
        mCvFilters.setVisibility(GONE);
        mFabSearch.show();
        mSvRoomSearch.setQuery("", false);
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

    @Override
    public void stopRefreshingBar() {
        mRefreshLayout.setRefreshing(false);
    }

    private void hideAll() {
        mRlErrorServer.setVisibility(GONE);
        mRlErrorNetwork.setVisibility(GONE);
        mFlLoadingContainer.setVisibility(GONE);
        mRefreshLayout.setVisibility(GONE);
        mRlEmptyList.setVisibility(GONE);
    }
}
