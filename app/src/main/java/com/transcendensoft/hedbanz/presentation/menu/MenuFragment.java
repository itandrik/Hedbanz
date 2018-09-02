package com.transcendensoft.hedbanz.presentation.menu;
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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.StartActivity;
import com.transcendensoft.hedbanz.presentation.base.BaseFragment;
import com.transcendensoft.hedbanz.presentation.changeicon.ChangeIconActivity;
import com.transcendensoft.hedbanz.presentation.feedback.FeedbackActivity;
import com.transcendensoft.hedbanz.presentation.friends.FriendsActivity;
import com.transcendensoft.hedbanz.presentation.intro.IntroActivity;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity;
import com.transcendensoft.hedbanz.presentation.usercrud.CredentialsActivity;
import com.transcendensoft.hedbanz.utils.AndroidUtils;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows user info and menu items such as:
 * changing user data, friends, shop, help, settings.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class MenuFragment extends BaseFragment implements MenuFragmentContract.View {
    @BindView(R.id.tvFriends) TextView mTvFriends;
    @BindView(R.id.tvGamesPlayed) TextView mTvGamesPlayed;
    @BindView(R.id.tvMoney) TextView mTvMoney;
    @BindView(R.id.tvFriendsTitle) TextView mTvFriendsTitle;
    @BindView(R.id.tvGamesPlayedTitle) TextView mTvGamesPlayedTitle;
    @BindView(R.id.tvMoneyTitle) TextView mTvMoneyTitle;
    @BindView(R.id.tvUsername) TextView mTvUsername;
    @BindView(R.id.ivUserImage) ImageView mIvImage;
    @BindView(R.id.shimmerLayout) ShimmerFrameLayout mShimmerFrameLayout;

    @Inject PreferenceManager mPreferenceManager;
    @Inject MainActivity mActivity;
    @Inject MenuFragmentPresenter mPresenter;

    @Inject
    public MenuFragment() {
        //Requires empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ButterKnife.bind(this, view);
        initUserData();

        if (mPresenter != null) {
            mPresenter.setModel(new User());
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
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    private void initUserData() {
        User user = mPreferenceManager.getUser();

        mTvMoney.setText(String.valueOf(user.getMoney()));
        mTvUsername.setText(user.getLogin());
    }

    @OnClick(R.id.btnCredentials)
    protected void onCredentialsClicked() {
        startActivity(new Intent(getActivity(), CredentialsActivity.class));
    }

    @OnClick(R.id.ivUserImage)
    protected void onUserImageClicked(){
        startActivity(new Intent(getActivity(), ChangeIconActivity.class));
    }

    @OnClick(R.id.tvFriends)
    protected void onFriendsAmountClicked() {
        onFriendsButtonClicked();
    }

    @OnClick(R.id.btnFriends)
    protected void onFriendsButtonClicked() {
        startActivity(new Intent(mActivity, FriendsActivity.class));
    }

    @OnClick(R.id.tvMoney)
    protected void onMoneysAmountClicked() {
        mActivity.showShortToastMessage(R.string.in_developing);
        //TODO add money purchasing
    }

    @OnClick(R.id.btnShop)
    protected void onShopClicked() {
        mActivity.showShortToastMessage(R.string.in_developing);
    }

    @OnClick(R.id.btnHelp)
    protected void onHelpClicked() {
        startActivity(new Intent(getActivity(), IntroActivity.class));
    }

    @OnClick(R.id.btnSettings)
    protected void onSettingsClicked() {
        AndroidUtils.showShortToast(getActivity(), R.string.in_developing);
    }

    @OnClick(R.id.btnFeedback)
    protected void onFeedbackClicked() {
        startActivity(new Intent(getActivity(), FeedbackActivity.class));
    }

    @OnClick(R.id.btnSettings)
    protected void onRateClicked() {
        final String appPackageName = mActivity.getPackageName();

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @OnClick(R.id.btnExit)
    protected void onLogoutClicked() {
        if (mPresenter != null) {
            mPresenter.unbindFirebaseToken();
        }
    }

    @OnClick(R.id.fabDown)
    protected void onFabDownClicked() {
        if (getActivity() != null) {
            mActivity.onBackPressed();
        }
    }

    @Override
    public void showLogoutSuccess() {
        hideLoadingDialog();

        mPreferenceManager.setIsAuthorised(false);
        mPreferenceManager.setUser(null);

        Intent intent = new Intent(getActivity(), StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    @Override
    public void showLogoutServerError() {
        hideLoadingDialog();

        new AlertDialog.Builder(mActivity)
                .setTitle(getString(R.string.menu_logout_error_title))
                .setMessage(getString(R.string.menu_logout_server_error))
                .setIcon(R.drawable.ic_dialog_server_error)
                .setPositiveButton(getString(R.string.action_ok), (dialog, v) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    @Override
    public void showLogoutNetworkError() {
        hideLoadingDialog();

        new AlertDialog.Builder(mActivity)
                .setTitle(getString(R.string.menu_logout_error_title))
                .setMessage(getString(R.string.menu_logout_network_error))
                .setIcon(R.drawable.ic_dialog_network_error)
                .setPositiveButton(getString(R.string.action_ok), (dialog, v) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    @Override
    public void showUserDataLoading() {
        mTvFriends.setVisibility(View.GONE);
        mTvGamesPlayed.setVisibility(View.GONE);
        mTvMoney.setVisibility(View.GONE);
        mTvFriendsTitle.setVisibility(View.GONE);
        mTvGamesPlayedTitle.setVisibility(View.GONE);
        mTvMoneyTitle.setVisibility(View.GONE);
        mTvUsername.setVisibility(View.GONE);
        mIvImage.setVisibility(View.GONE);

        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        mShimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    public void setUserData(@NotNull User user) {
        mTvFriends.setVisibility(View.VISIBLE);
        mTvGamesPlayed.setVisibility(View.VISIBLE);
        mTvMoney.setVisibility(View.VISIBLE);
        mTvFriendsTitle.setVisibility(View.VISIBLE);
        mTvGamesPlayedTitle.setVisibility(View.VISIBLE);
        mTvMoneyTitle.setVisibility(View.VISIBLE);
        mTvUsername.setVisibility(View.VISIBLE);
        mIvImage.setVisibility(View.VISIBLE);

        mShimmerFrameLayout.setVisibility(View.GONE);
        mShimmerFrameLayout.stopShimmerAnimation();

        mTvMoney.setText(String.valueOf(user.getMoney()));
        mTvFriends.setText(String.valueOf(user.getFriendsNumber()));
        mTvGamesPlayed.setText(String.valueOf(user.getFriendsNumber()));
    }

    @Override
    public void showLoading() {
        // Stub
    }

    @Override
    public void showContent() {
        // Stub
    }

    @Override
    public void hideAll() {
        // Stub
    }
}
