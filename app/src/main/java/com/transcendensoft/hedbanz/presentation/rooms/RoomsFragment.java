package com.transcendensoft.hedbanz.presentation.rooms;
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

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.HedbanzAnalyticsKt;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.RoomFilter;
import com.transcendensoft.hedbanz.presentation.StartActivity;
import com.transcendensoft.hedbanz.presentation.base.BaseFragment;
import com.transcendensoft.hedbanz.presentation.custom.widget.rangeseekbar.RangeSeekBar;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainFragment;
import com.transcendensoft.hedbanz.presentation.rooms.list.RoomsAdapter;
import com.transcendensoft.hedbanz.presentation.rooms.models.RoomList;
import com.transcendensoft.hedbanz.utils.extension.ViewExtensionsKt;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.transcendensoft.hedbanz.data.network.service.firebase.HedbanzFirebaseMessagingService.ACTION_ADD_NEW_ROOM;

/**
 * Fragment that shows room list.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class RoomsFragment extends BaseFragment implements RoomsContract.View {
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rvRooms) RecyclerView mRecycler;
    @BindView(R.id.rlEmptyListContainer) RelativeLayout mRlEmptyList;
    @BindView(R.id.rlErrorNetwork) ConstraintLayout mRlErrorNetwork;
    @BindView(R.id.rlErrorServer) ConstraintLayout mRlErrorServer;
    @BindView(R.id.flLoadingContainer) FrameLayout mFlLoadingContainer;
    @BindView(R.id.parent) RelativeLayout mParentLayout;

    /**
     * Searching and filters
     */
    @BindView(R.id.cvFilters) CardView mCvFilters;
    @BindView(R.id.fabSearchRoom) FloatingActionButton mFabSearch;
    @BindView(R.id.chbApplyFilters) CheckBox mChbApplyFilters;
    @BindView(R.id.chbWithPassword) CheckBox mChbWithPassword;
    @BindView(R.id.rsbMaxPlayers) RangeSeekBar mRangeSeekBarMaxPlayers;
    @BindView(R.id.tvFilterMaxPlayersTitle) TextView mTvFilterMaxPlayersTitle;
    @BindView(R.id.filterDividerView) View mFilterDividerView;

    private RelativeLayout mRlSearchContainer;
    private TextView mTvToolbarTitle;
    private SearchView mSvRoomSearch;
    private ImageView mIvSearchFilter;
    private ImageView mIvCloseSearch;
    private BroadcastReceiver mRoomsReceiver;

    @Inject RoomsPresenter mPresenter;
    @Inject RoomList mPresenterModel;
    @Inject RoomsAdapter mAdapter;
    @Inject MainActivity mActivity;
    @Inject PreferenceManager mPreferenceManager;
    @Inject FirebaseAnalytics mFirebaseAnalytics;

    @Inject
    public RoomsFragment() {

    }

    /*------------------------------------*
     *-------- Fragment lifecycle --------*
     *------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        ButterKnife.bind(this, view);
        ViewExtensionsKt.setupKeyboardHiding(mParentLayout, mActivity);

        mPresenter.setModel(mPresenterModel);
        initSwipeToRefresh();
        initRecycler();
        initSearchView();
        initFilters();
        initRoomsReceiver();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
        }
        mActivity.registerReceiver(mRoomsReceiver, new IntentFilter(ACTION_ADD_NEW_ROOM));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unbindView();
        }
        mActivity.unregisterReceiver(mRoomsReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void setPresenterModel(RoomList model) {
        if (mPresenter != null) {
            mPresenter.setModel(model);
        }
    }

    private void initSwipeToRefresh() {
        mRefreshLayout.setColorSchemeResources(R.color.textDarkRed,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        mRefreshLayout.setOnRefreshListener(() -> {
            if (mPresenter != null) {
                String searchText = mSvRoomSearch.getQuery().toString();
                if (!mChbApplyFilters.isChecked() && TextUtils.isEmpty(searchText)) {
                    mPresenter.refreshRooms();
                } else {
                    mPresenter.updateFilter(null); //filter with old filters
                }
            }
        });
    }

    private void initRecycler() {
        //mAdapter = new RoomsAdapter(mPresenter);
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
            closeSearchAndRefresh();
            mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.CLOSE_FILTER_BUTTON, null);
        });

        mIvSearchFilter.setOnClickListener(v -> {
            if (mCvFilters.getVisibility() == VISIBLE) {
                mCvFilters.setVisibility(GONE);
            } else {
                mCvFilters.setVisibility(VISIBLE);
            }
            mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.FILTER_BUTTON, null);
        });
    }

    @SuppressLint("CheckResult")
    private void initFilters() {
        disableFilters();
        RxSearchView.queryTextChanges(mSvRoomSearch)
                .debounce(350, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .skip(2)
                .filter(text -> !text.equals("#"))
                .subscribe(text -> {
                    if (mPresenter != null) {
                        if (!TextUtils.isEmpty(text) || mChbApplyFilters.isChecked()) {
                            filterRoomsWithText(text);
                        } else {
                            mPresenter.clearTextFilter();
                            mPresenter.refreshRooms();
                        }
                    }
                });
        RxCompoundButton.checkedChanges(mChbApplyFilters)
                .observeOn(AndroidSchedulers.mainThread())
                .skip(1)
                .subscribe(isChecked -> {
                    if (isChecked) {
                        enableFilters();
                        RoomFilter currentFilter = new RoomFilter.Builder()
                                .setIsPrivate(mChbWithPassword.isChecked())
                                .setRoomName(mSvRoomSearch.getQuery().toString())
                                .setMinPlayers((byte) mRangeSeekBarMaxPlayers.getProgress1())
                                .setMaxPlayers((byte) mRangeSeekBarMaxPlayers.getProgress2())
                                .build();
                        mPresenter.filterRooms(currentFilter);
                    } else {
                        disableFilters();
                        clearRooms();
                        mPresenter.clearFilters();
                        if (!TextUtils.isEmpty(mSvRoomSearch.getQuery().toString())) {
                            filterRoomsWithText(mSvRoomSearch.getQuery().toString());
                        } else {
                            mPresenter.clearFilters();
                            mPresenter.refreshRooms();
                        }
                    }
                });
        RxCompoundButton.checkedChanges(mChbWithPassword)
                .observeOn(AndroidSchedulers.mainThread())
                .skip(1)
                .subscribe(isChecked -> {
                    mPresenter.updateFilter(new RoomFilter.Builder()
                            .setIsPrivate(isChecked).build());
                });
        mRangeSeekBarMaxPlayers.setOnSeekChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(RangeSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                // Stub
            }

            @Override
            public void onSectionChanged(RangeSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {
                // Stub
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar seekBar, int thumbPosOnTick) {
                // Stub
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar seekBar, int progressLeft, int progressRight) {
                mPresenter.updateFilter(new RoomFilter.Builder()
                        .setMinPlayers((byte) progressLeft)
                        .setMaxPlayers((byte) progressRight)
                        .build());
            }
        });
    }

    private void initRoomsReceiver() {
        mRoomsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ((intent != null) && (intent.getAction() != null) &&
                        intent.getAction().equalsIgnoreCase(ACTION_ADD_NEW_ROOM) &&
                        mRecycler.getScrollY() <= 0) {
                    mPresenter.refreshRooms();
                }
            }
        };
    }

    private void disableFilters() {
        int disabledColor = ContextCompat.getColor(getActivity(), R.color.textDisabled);
        mRangeSeekBarMaxPlayers.setEnabled(false);
        mChbWithPassword.setEnabled(false);
        mTvFilterMaxPlayersTitle.setTextColor(disabledColor);
        mFilterDividerView.setBackgroundColor(disabledColor);
    }

    private void enableFilters() {
        int enabledColor = ContextCompat.getColor(getActivity(), R.color.textPrimary);
        mChbWithPassword.setEnabled(true);
        mTvFilterMaxPlayersTitle.setTextColor(enabledColor);
        mRangeSeekBarMaxPlayers.setEnabled(true);
        mFilterDividerView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.textDarkRed));
    }

    private void filterRoomsWithText(CharSequence text) {
        mPresenter.updateFilter(new RoomFilter.Builder()
                .setRoomName(text.toString())
                .build());
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
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.SEARCH_BUTTON, null);
    }

    public void closeSearchAndRefresh() {
        mTvToolbarTitle.setVisibility(View.VISIBLE);
        mRlSearchContainer.setVisibility(View.GONE);
        mCvFilters.setVisibility(GONE);
        mFabSearch.show();
        mChbApplyFilters.setChecked(false);
        mSvRoomSearch.setQuery("", false);
        mPresenter.clearFilters();
        mPresenter.refreshRooms();
    }

    public void hideFilters() {
        try {
            mTvToolbarTitle.setVisibility(View.VISIBLE);
            mRlSearchContainer.setVisibility(View.GONE);
            mCvFilters.setVisibility(GONE);
            mFabSearch.show();
        } catch (NullPointerException e) {
            Timber.e(e);
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

    @Override
    public void showUnauthorizedError() {
        Drawable d = VectorDrawableCompat.create(getResources(), R.drawable.ic_unhappy, null);
        new AlertDialog.Builder(mActivity)
                .setPositiveButton(getString(R.string.action_ok),
                        (dialog, which) -> mPresenter.unbindFirebaseToken())
                .setOnDismissListener(dialog -> mPresenter.unbindFirebaseToken())
                .setOnCancelListener(dialog -> mPresenter.unbindFirebaseToken())
                .setIcon(d)
                .setTitle(getString(R.string.game_error_title))
                .setMessage(getString(R.string.rooms_unauthorized_user))
                .show();
    }

    @Override
    public void forceLogout() {
        hideLoadingDialog();

        mPreferenceManager.setIsAuthorised(false);
        mPreferenceManager.setUser(null);
        mPreferenceManager.setFirebaseTokenBinded(false);

        Intent intent = new Intent(getActivity(), StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    @Override
    public void stopRefreshingBar() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hideAll() {
        mRlErrorServer.setVisibility(GONE);
        mRlErrorNetwork.setVisibility(GONE);
        mFlLoadingContainer.setVisibility(GONE);
        mRefreshLayout.setVisibility(GONE);
        mRlEmptyList.setVisibility(GONE);
    }
}
