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
import android.view.View;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.ViewHolder}
 * for view that represents that word has been setted for specific user
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class WordSettedViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvWordSetted) TextView mTvWordSetted;
    private Context mContext;

    public WordSettedViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.mContext = context;
    }

    public void bindText(String wordSetterUserLogin, String wordReceiverWordLogin){
        mTvWordSetted.setText(mContext.getString(R.string.game_word_setted,
                wordSetterUserLogin, wordReceiverWordLogin));
    }

    public void bindTextSettedToCurrentUser(String wordSetterUserLogin){
        mTvWordSetted.setText(mContext.getString(R.string.game_word_setted_to_current_user,
                wordSetterUserLogin));
    }

    public void bindCurrentUserSettedText(String wordReceiverWordLogin){
        mTvWordSetted.setText(mContext.getString(R.string.game_word_current_user_setted,
                wordReceiverWordLogin));
    }

}
