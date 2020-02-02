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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.HedbanzAnalyticsKt;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.StartActivity;
import com.transcendensoft.hedbanz.presentation.base.BaseFragment;
import com.transcendensoft.hedbanz.presentation.feedback.FeedbackFragment;
import com.transcendensoft.hedbanz.presentation.intro.IntroActivity;
import com.transcendensoft.hedbanz.presentation.language.LanguageActivity;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity;
import com.transcendensoft.hedbanz.presentation.usercrud.CredentialsActivity;
import com.transcendensoft.hedbanz.utils.AndroidUtils;
import com.transcendensoft.hedbanz.utils.extension.FragmentExtensionsKt;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows user info and menu items such as:
 * changing user data, friends, shop, help, profile.
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
    @Inject FirebaseAnalytics mFirebaseAnalytics;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavHostFragment.findNavController(this).addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()){
                case R.id.menuFragment:
                    FragmentExtensionsKt.setupMainScreenToolbar(this, ((MainActivity) requireActivity()).getToolbar(), getString(R.string.menu_title));
                    break;
                case R.id.changeIconFragment:
                    FragmentExtensionsKt.setupNavigationToolbar(this, ((MainActivity) requireActivity()).getToolbar(), getString(R.string.change_icon_choose_avatar));
                    break;
                case R.id.friendsFragment:
                    FragmentExtensionsKt.setupNavigationToolbar(this, ((MainActivity) requireActivity()).getToolbar(), getString(R.string.friends_toolbar_title));
                    break;
                case R.id.feedbackFragment:
                    FragmentExtensionsKt.setupNavigationToolbar(this, ((MainActivity) requireActivity()).getToolbar(), getString(R.string.feedback_title));
                    break;

            }
        });
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
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.CREDENTIALS_BUTTON, null);
    }

    @OnClick(R.id.ivUserImage)
    protected void onUserImageClicked(){
        NavHostFragment.findNavController(this).navigate(R.id.action_menuFragment_to_changeIconFragment);
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.USER_IMAGE_BUTTON, null);
    }

    @OnClick(R.id.tvFriends)
    protected void onFriendsAmountClicked() {
        NavHostFragment.findNavController(this).navigate(R.id.action_menuFragment_to_friendsFragment);
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.FRIENDS_CIRCLE, null);
    }

    @OnClick(R.id.btnFriends)
    protected void onFriendsButtonClicked() {
        NavHostFragment.findNavController(this).navigate(R.id.action_menuFragment_to_friendsFragment);
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.FRIENDS_BUTTON, null);
    }

    @OnClick(R.id.tvMoney)
    protected void onMoneysAmountClicked() {
        mActivity.showShortToastMessage(R.string.in_developing);
        //TODO add money purchasing
    }

    @OnClick(R.id.btnShop)
    protected void onShopClicked() {
        mActivity.showShortToastMessage(R.string.in_developing);
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.SHOP_BUTTON, null);
    }

    @OnClick(R.id.btnHelp)
    protected void onHelpClicked() {
        startActivity(new Intent(getActivity(), IntroActivity.class));
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.HELP_BUTTON, null);
    }

    @OnClick(R.id.btnSettings)
    protected void onSettingsClicked() {
        AndroidUtils.showShortToast(getActivity(), R.string.in_developing);
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.SETTINGS_BUTTON, null);
    }

    @OnClick(R.id.btnFeedback)
    protected void onFeedbackClicked() {
        NavHostFragment.findNavController(this).navigate(R.id.action_menuFragment_to_feedbackFragment);
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.FEEDBACK_BUTTON, null);
    }

    @OnClick(R.id.btnLanguage)
    protected void onLanguageClicked(){
        startActivity(new Intent(getActivity(), LanguageActivity.class));
        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.LANGUAGE_BUTTON, null);
    }

    @OnClick(R.id.btnRate)
    protected void onRateClicked() {
        final String appPackageName = mActivity.getPackageName();

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }

        mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.RATE_BUTTON, null);

    }

    @OnClick(R.id.btnExit)
    protected void onLogoutClicked() {
        if (mPresenter != null) {
            mPresenter.unbindFirebaseToken();
            mFirebaseAnalytics.logEvent(HedbanzAnalyticsKt.EXIT_BUTTON, null);
        }
    }

    @OnClick(R.id.cvDown)
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
        mPreferenceManager.setAuthorizationToken(null);

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
                .setPositiveButton(getString(R.string.action_ok), (dialog, v) -> {
                    dialog.dismiss();
                    showLogoutSuccess();
                })
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
        mShimmerFrameLayout.startShimmer();
    }

    @Override
    public void setUserData(@NotNull User user) {
        mTvFriends.setVisibility(View.VISIBLE);
        mTvGamesPlayed.setVisibility(View.VISIBLE);
        mTvMoney.setVisibility(View.INVISIBLE);// TODO change to visible when money becomes in prod
        mTvFriendsTitle.setVisibility(View.VISIBLE);
        mTvGamesPlayedTitle.setVisibility(View.VISIBLE);
        mTvMoneyTitle.setVisibility(View.INVISIBLE);// TODO change to visible when money becomes in prod
        mTvUsername.setVisibility(View.VISIBLE);
        mIvImage.setVisibility(View.VISIBLE);

        mShimmerFrameLayout.setVisibility(View.GONE);
        mShimmerFrameLayout.stopShimmer();

        mTvUsername.setText(user.getLogin());
        mTvMoney.setText(String.valueOf(user.getMoney()));
        mTvFriends.setText(String.valueOf(user.getFriendsNumber()));
        mTvGamesPlayed.setText(String.valueOf(user.getGamesNumber()));

        Drawable d = VectorDrawableCompat.create(getResources(), user.getIconId().getResId(), null);
        mIvImage.setImageDrawable(d);
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
