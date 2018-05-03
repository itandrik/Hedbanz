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

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.presentation.base.MvpViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  View holder realization for concrete user in game mode
 *  side bar menu.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class UserMenuItemViewHolder extends MvpViewHolder<UserMenuItemPresenter>
        implements UserMenuItemContract.View {
    @BindView(R.id.tvUserWord) TextView mTvWord;
    @BindView(R.id.tvUserLogin) TextView mTvLogin;
    @BindView(R.id.ivFriend) ImageView mIvIsFriend;
    @BindView(R.id.ivUserIcon) ImageView mIvUserIcon;
    @BindView(R.id.tvAfkShadow) TextView mTvAfk;

    public UserMenuItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(v -> {
           presenter.onClickUser();
        });
    }

    @Override
    public void setIcon(int icon) {
        mIvUserIcon.setImageResource(icon);
    }

    @Override
    public void setName(String name) {
        if(!TextUtils.isEmpty(name)){
            mTvLogin.setText(name);
        } else {
            mTvLogin.setText("");
        }
    }

    @Override
    public void setIsAfk(boolean isAfk) {
        if(isAfk){
            mTvAfk.setVisibility(View.VISIBLE);
        } else {
            mTvAfk.setVisibility(View.GONE);
        }
    }

    @Override
    public void setIsFriend(boolean isFriend) {
        if(isFriend){
            mIvIsFriend.setVisibility(View.VISIBLE);
        } else {
            mIvIsFriend.setVisibility(View.GONE);
        }
    }

    @Override
    public void setWord(String word) {
        if(!TextUtils.isEmpty(word)){
            mTvWord.setText(word);
            mTvWord.setVisibility(View.VISIBLE);
        } else {
            mTvWord.setText("");
            mTvWord.setVisibility(View.GONE);
        }
    }
}
