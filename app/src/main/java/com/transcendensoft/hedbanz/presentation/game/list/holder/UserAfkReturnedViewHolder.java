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
 * for view that represents that some user has
 * become AFK or returned to play
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class UserAfkReturnedViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvAfkReturned) TextView mTvAfkReturned;
    private Context mContext;

    public UserAfkReturnedViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.mContext = context;
    }

    public void bindText(boolean isAfk, String userLogin) {
        if (TextUtils.isEmpty(userLogin)) {
            if (isAfk) {
                mTvAfkReturned.setText(mContext.getString(R.string.game_some_user_afk));
            } else {
                mTvAfkReturned.setText(mContext.getString(R.string.game_some_user_returned));
            }
        } else {
            if (isAfk) {
                mTvAfkReturned.setText(mContext.getString(R.string.game_user_afk, userLogin));
            } else {
                mTvAfkReturned.setText(mContext.getString(R.string.game_user_returned, userLogin));
            }
        }
    }
}
