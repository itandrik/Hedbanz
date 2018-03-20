package com.transcendensoft.hedbanz.presentation.mainscreen.menu;
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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.entity.User;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.presentation.StartActivity;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity;
import com.transcendensoft.hedbanz.presentation.usercrud.CredentialsActivity;
import com.transcendensoft.hedbanz.utils.AndroidUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;

/**
 * Fragment that shows user info and menu items such as:
 * changing user data, friends, shop, help, settings.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class MenuFragment extends DaggerFragment{
    @BindView(R.id.tvFriends) TextView mTvFriends;
    @BindView(R.id.tvGamesPlayed) TextView mTvGamesPlayed;
    @BindView(R.id.tvMoney) TextView mTvMoney;
    @BindView(R.id.tvUsername) TextView mTvUsername;
    @BindView(R.id.ivUserImage) ImageView mIvImage;

    @Inject PreferenceManager mPreferenceManager;
    @Inject MainActivity mActivity;

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

        return view;
    }

    private void initUserData(){
        User user = mPreferenceManager.getUser();

        mTvMoney.setText(String.valueOf(user.getMoney()));
        mTvUsername.setText(user.getLogin());
    }

    @OnClick(R.id.btnCredentials)
    protected void onCredentialsClicked(){
        startActivity(new Intent(getActivity(), CredentialsActivity.class));
    }

    @OnClick(R.id.tvFriends)
    protected void onFriendsAmountClicked(){
        onFriendsButtonClicked();
    }

    @OnClick(R.id.btnFriends)
    protected void onFriendsButtonClicked(){
        mActivity.showShortToastMessage(R.string.in_developing);
    }

    @OnClick(R.id.tvMoney)
    protected void onMoneysAmountClicked(){
        mActivity.showShortToastMessage(R.string.in_developing);
        //TODO add money purchasing
    }

    @OnClick(R.id.btnShop)
    protected void onShopClicked(){
        mActivity.showShortToastMessage(R.string.in_developing);
    }

    @OnClick(R.id.btnHelp)
    protected void onHelpClicked(){
        mActivity.showShortToastMessage(R.string.in_developing);
    }

    @OnClick(R.id.btnSettings)
    protected void onSettingsClicked(){
        AndroidUtils.showShortToast(getActivity(), R.string.in_developing);
    }

    @OnClick(R.id.btnExit)
    protected void onLogoutClicked(){
        mPreferenceManager.setIsAuthorised(false);

        Intent intent = new Intent(getActivity(), StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

    }

    @OnClick(R.id.fabDown)
    protected void onFabDownClicked(){
        if(getActivity() != null) {
            mActivity.onBackPressed();
        }
    }
}
