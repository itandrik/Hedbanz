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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.di.qualifier.ActivityContext;
import com.transcendensoft.hedbanz.domain.entity.HedbanzAnalyticsKt;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BaseFragment;
import com.transcendensoft.hedbanz.presentation.game.menu.list.UserMenuListAdapter;
import com.transcendensoft.hedbanz.presentation.game.models.RxRoom;
import com.transcendensoft.hedbanz.presentation.game.models.RxUser;
import com.transcendensoft.hedbanz.presentation.invite.InviteDialogFragment;
import com.transcendensoft.hedbanz.presentation.userdetails.UserDetailsDialogFragment;
import com.transcendensoft.hedbanz.utils.ViewUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Fragment that shows sidebar menu with users.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GameMenuFragment extends BaseFragment implements GameMenuContract.View {
    public static final float NOT_EXIST_USER_ALPHA = 0.3f;
    public static final float EXIST_USER_ALPHA = 1.f;

    @BindView(R.id.rvPlayers) RecyclerView mRecycler;
    @BindView(R.id.fabInvite) FloatingActionButton mFabInvite;
    @BindView(R.id.flLoadingContainer) FrameLayout mFlLoadingContainer;
    @BindView(R.id.collapsingToolbarGameMenu) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.llUsers) LinearLayout mLlUsersIndicator;

    @Inject RxRoom mModel;
    @Inject GameMenuPresenter mPresenter;
    @Inject UserMenuListAdapter mAdapter;
    @Inject UserDetailsDialogFragment mUserDetailsDialogFragment;
    @Inject InviteDialogFragment mInviteDialogFragment;
    @Inject PreferenceManager mPreferenceManger;
    @Inject @ActivityContext Context mContext;
    @Inject FirebaseAnalytics mFirebaseAnalytics;

    private Comparator<RxUser> playersComparator = (o1, o2) -> {
        if (o2.getUser().equals(mPreferenceManger.getUser())) {
            return 1;
        }

        if (o1.getUser().equals(mPreferenceManger.getUser())) {
            return -1;
        }

        return Long.compare(o1.getUser().getId(), o2.getUser().getId());
    };

    @Inject
    public GameMenuFragment() {
    }

    /*------------------------------------*
     *-------- Fragment lifecycle --------*
     *------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_menu, container, false);
        ButterKnife.bind(this, view);

        mPresenter.setModel(mModel);
        initRecycler();
        initCollapsingToolbar();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
            initRecyclerClickListeners();
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
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    private void initRecycler() {
        mRecycler.setAdapter(mAdapter);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.addItemDecoration(new DividerItemDecoration(
                mContext, DividerItemDecoration.VERTICAL));

        LinearLayoutManager manager = new LinearLayoutManager(
                mContext, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(manager);
    }

    private void initCollapsingToolbar() {
        try {
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.open_sans_light);
            mCollapsingToolbar.setCollapsedTitleTypeface(typeface);
            mCollapsingToolbar.setExpandedTitleTypeface(typeface);
        } catch (Resources.NotFoundException e) {
            Timber.e(e);
        }
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void setRoomName(String roomName) {
        if (!TextUtils.isEmpty(roomName)) {
            mCollapsingToolbar.setTitle(roomName);
        } else {
            mCollapsingToolbar.setTitle("");
        }
    }

    @Override
    public void clearAndAddPlayers(List<RxUser> rxUsers) {
        if (mAdapter != null) {
            try {
                mAdapter.clearAll();
                Collections.sort(rxUsers, playersComparator);
                mAdapter.addAll(rxUsers);
                //mAdapter.sort(playersComparator);
            } catch (ConcurrentModificationException e) {
                Timber.e(e);
            }
        }
    }

    @Override
    public void addPlayer(RxUser rxUser) {
        if (mAdapter != null) {
            try {
                List<RxUser> rxUsers = mAdapter.getModels();
                rxUsers.add(rxUser);
                Collections.sort(rxUsers, playersComparator);

                mAdapter.clearAll();
                mAdapter.addAll(rxUsers);
            } catch (ConcurrentModificationException e) {
                Timber.e(e);
            }
        }
    }

    @Override
    public void removePlayer(RxUser rxUser) {
        if (mAdapter != null) {
            mAdapter.removeItem(rxUser);
        }
    }

    @Override
    public void setMaxPlayersCount(int maxPlayersCount) {
        mLlUsersIndicator.removeAllViews();
        for (int i = 0; i < maxPlayersCount; i++) {
            ImageView ivPlayer = new ImageView(mContext);
            ivPlayer.setAlpha(NOT_EXIST_USER_ALPHA);

            Drawable drawable = VectorDrawableCompat.create(
                    mContext.getResources(), R.drawable.ic_user, null);
            ivPlayer.setImageDrawable(drawable);

            int size = ViewUtils.dpToPx(mContext, 24);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            ivPlayer.setLayoutParams(params);

            mLlUsersIndicator.addView(ivPlayer);
        }
    }

    @Override
    public void setCurrentPlayersCount(int currentPlayersCount) {
        for (int i = 0; i < mLlUsersIndicator.getChildCount(); i++) {
            ImageView ivPlayer = (ImageView) mLlUsersIndicator.getChildAt(i);
            if (i < currentPlayersCount) {
                ivPlayer.setAlpha(EXIST_USER_ALPHA);
            } else {
                ivPlayer.setAlpha(NOT_EXIST_USER_ALPHA);
            }
        }
    }

    /*------------------------------------*
     *----- Recycler click listeners -----*
     *------------------------------------*/
    private void initRecyclerClickListeners() {
        mPresenter.processPlayerClickListener(mAdapter.getItemClickListener());
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.fabInvite)
    protected void onInviteClicked() {
        mInviteDialogFragment.setRoom(mPresenter.getRoom());
        mInviteDialogFragment.show(getChildFragmentManager(), getString(R.string.tag_fragment_invite));
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.INVITE_BUTTON, null);
    }

    @Override
    public void onPlayerClicked(User user) {
        if (!user.equals(mPreferenceManger.getUser())) {
            mUserDetailsDialogFragment.setUser(user);
            mUserDetailsDialogFragment.show(getChildFragmentManager(),
                    getString(R.string.tag_fragment_user_details));
        }
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void showLoading() {
        hideAll();
        mFlLoadingContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent() {
        hideAll();
        mFabInvite.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAll() {
        mFabInvite.setVisibility(View.GONE);
        mFlLoadingContainer.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
    }

    @Override
    public void setInviteEnabled(boolean isEnabled) {
        mFabInvite.setEnabled(isEnabled);
        if (isEnabled) {
            mFabInvite.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(mContext, R.color.google_green)));
        } else {
            mFabInvite.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(mContext, R.color.invite_fab_disabled)));
        }
    }
}
