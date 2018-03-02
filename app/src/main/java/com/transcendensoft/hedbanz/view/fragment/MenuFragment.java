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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.model.data.PreferenceManager;
import com.transcendensoft.hedbanz.util.AndroidUtils;
import com.transcendensoft.hedbanz.view.activity.CredentialsActivity;
import com.transcendensoft.hedbanz.view.activity.MainActivity;
import com.transcendensoft.hedbanz.view.activity.StartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows user info and menu items such as:
 * changing user data, friends, shop, help, settings.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class MenuFragment extends Fragment{
    @BindView(R.id.tvFriends) TextView mTvFriends;
    @BindView(R.id.tvGamesPlayed) TextView mTvGamesPlayed;
    @BindView(R.id.tvMoney) TextView mTvMoney;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ButterKnife.bind(this, view);

        view.findViewById(R.id.btnCredentials).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CredentialsActivity.class));
        });

        return view;
    }

    @OnClick(R.id.btnCredentials)
    protected void onCredentialsClicked(){
        AndroidUtils.showShortToast(getActivity(), R.string.in_developing);
    }

    @OnClick(R.id.tvFriends)
    protected void onFriendsAmountClicked(){
        onFriendsButtonClicked();
    }

    @OnClick(R.id.btnFriends)
    protected void onFriendsButtonClicked(){
        AndroidUtils.showShortToast(getActivity(), R.string.in_developing);
    }

    @OnClick(R.id.tvMoney)
    protected void onMoneysAmountClicked(){
        AndroidUtils.showShortToast(getActivity(), R.string.in_developing);
        //TODO add money purchasing
    }

    @OnClick(R.id.btnShop)
    protected void onShopClicked(){
        AndroidUtils.showShortToast(getActivity(), R.string.in_developing);
    }

    @OnClick(R.id.btnHelp)
    protected void onHelpClicked(){
        AndroidUtils.showShortToast(getActivity(), R.string.in_developing);
    }

    @OnClick(R.id.btnSettings)
    protected void onSettingsClicked(){
        AndroidUtils.showShortToast(getActivity(), R.string.in_developing);
    }

    @OnClick(R.id.btnExit)
    protected void onLogoutClicked(){
        new PreferenceManager(getActivity()).setIsAuthorised(false);

        Intent intent = new Intent(getActivity(), StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

    }

    @OnClick(R.id.fabDown)
    protected void onFabDownClicked(){
        if(getActivity() != null) {
            ((MainActivity) getActivity()).onBackPressed();
        }
    }
}
