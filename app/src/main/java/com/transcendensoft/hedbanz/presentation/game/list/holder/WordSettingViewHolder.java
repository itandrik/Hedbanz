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
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    @BindView(R.id.tilSetWord) TextInputLayout mTilSetWord;
    @BindView(R.id.tietSetWord) TextInputEditText mTietSetWord;
    @BindView(R.id.ivSubmitWord) ImageView mIvSetWord;
    @BindView(R.id.pbWordLoading) ProgressBar mPbWordLoading;

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

    public void bindText(String text){
        if(!TextUtils.isEmpty(text)) {
            mTietSetWord.setText(text);
        } else {
            mTietSetWord.setText("");
        }
    }

    public void bindLoading(boolean isLoading, boolean isFinished) {
        if (isLoading && !isFinished) {
            mIvSetWord.setVisibility(View.GONE);
            mPbWordLoading.setVisibility(View.VISIBLE);
            mIvSetWord.setColorFilter(ContextCompat.getColor(mContext, R.color.google_green),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            mTietSetWord.setEnabled(false);
            mTilSetWord.setEnabled(false);
            mTietSetWord.clearFocus();
            mTilSetWord.clearFocus();
            mIvSetWord.setEnabled(false);
        } else if (!isLoading && isFinished) {
            mIvSetWord.setVisibility(View.VISIBLE);
            mIvSetWord.setColorFilter(ContextCompat.getColor(mContext, R.color.textSecondary),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            mPbWordLoading.setVisibility(View.GONE);
            mIvSetWord.setEnabled(false);
            mTilSetWord.setEnabled(false);
            mTietSetWord.clearFocus();
            mTietSetWord.setEnabled(false);
            mTilSetWord.clearFocus();
        } else {
            mIvSetWord.setVisibility(View.VISIBLE);
            mPbWordLoading.setVisibility(View.GONE);
            mTietSetWord.setEnabled(true);
            mIvSetWord.setEnabled(true);
            mIvSetWord.setColorFilter(ContextCompat.getColor(mContext, R.color.google_green),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    public Observable<String> setWordObservable() {
        return Observable.create(emitter -> {
            mIvSetWord.setOnClickListener(view -> {
                String text = mTietSetWord.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    mTilSetWord.setError(mContext.getString(R.string.game_word_setting_error));
                } else {
                    mTilSetWord.setError(null);
                    emitter.onNext(text);
                }
            });
        });
    }

    public Observable<Boolean> setWordEtFocusedObservable(){
        return Observable.create(emitter -> {
            mTietSetWord.setOnFocusChangeListener((v, hasFocus) -> {
                emitter.onNext(hasFocus);
            });
        });
    }
}
