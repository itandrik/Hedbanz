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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * for view that represents that word are setting for specific user
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class WordSettingViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvSetWordTitle) TextView mTvSetWordTitle;
    @BindView(R.id.etSetWord) EditText mEtSetWord;
    @BindView(R.id.ivSubmitWord) ImageView mIvSetWord;

    private Context mContext;

    public WordSettingViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.mContext = context;
    }

    public void bindTitle(String userLogin) {
        if (!TextUtils.isEmpty(userLogin)) {
            mTvSetWordTitle.setText(mContext.getString(R.string.game_set_word_title, userLogin));
        } else {
            mTvSetWordTitle.setText(mContext.getString(R.string.game_set_word_title_random));
        }
    }

    public Observable<String> setWordObservable() {
        return Observable.create(emitter -> {
            mIvSetWord.setOnClickListener(view -> {
                emitter.onNext(mEtSetWord.getText().toString().trim());
            });
        });
    }
}
