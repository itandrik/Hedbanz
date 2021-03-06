package com.transcendensoft.hedbanz.presentation.friends.list.holder;
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

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Friend;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * for view that represents already accepted friend
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class AcceptedFriendViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ivFriendIcon) ImageView mIvIcon;
    @BindView(R.id.ivRemoveFriend) ImageView mIvRemoveFriend;
    @BindView(R.id.tvFriendName) TextView mTvFriendName;

    public AcceptedFriendViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bindName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mTvFriendName.setText(name);
        } else {
            mTvFriendName.setText("");
        }
    }

    public void bindIcon(@DrawableRes int drawableIcon) {
        mIvIcon.setImageResource(drawableIcon);
    }

    public Observable<Friend> removeFriendObservable(Friend friend) {
        return Observable.create(emitter -> {
            mIvRemoveFriend.setOnClickListener(v -> {
                emitter.onNext(friend);
            });
        });
    }
}