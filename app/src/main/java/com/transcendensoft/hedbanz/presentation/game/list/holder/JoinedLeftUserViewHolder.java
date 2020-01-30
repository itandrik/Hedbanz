package com.transcendensoft.hedbanz.presentation.game.list.holder;
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
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.ViewHolder}
 * for view that represents text when some user has joined or left.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class JoinedLeftUserViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tvJoinedLeft) TextView mTvJoinedLeft;

    private Context mContext;

    public JoinedLeftUserViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.mContext = context;
    }

    public void bindJoinedLeftUserMessage(String login, boolean isJoined){
        if(!TextUtils.isEmpty(login)) {
            if (isJoined) {
                mTvJoinedLeft.setText(mContext.getString(R.string.game_user_joined, login));
            } else {
                mTvJoinedLeft.setText(mContext.getString(R.string.game_user_left, login));
            }
        } else {
            mTvJoinedLeft.setText("");
        }
    }
}
