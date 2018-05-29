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
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * for view that represents data when user some starts guessing
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GuessWordOtherUserViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvGuessWord) TextView mTvGuessWord;
    private Context mContext;

    public GuessWordOtherUserViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.mContext = context;
    }

    public void bindSomeUserGuessWord(String userName){
        if(TextUtils.isEmpty(userName)){
            mTvGuessWord.setText(mContext.getString(R.string.game_some_user_think_word_to_user_error));
        } else {
            mTvGuessWord.setText(mContext.getString(R.string.game_some_user_think_word_to_user, userName));
        }
    }
}