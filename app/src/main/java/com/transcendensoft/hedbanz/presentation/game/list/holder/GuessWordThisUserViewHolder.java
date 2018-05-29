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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.presentation.game.list.GuessWordsHelperAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * for view that represents data when user guess some word
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GuessWordThisUserViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.etGuessWord) EditText mEtGuessWord;
    @BindView(R.id.rvGuessHelpers) RecyclerView mRvGuessHelpers;
    @BindView(R.id.ivSubmitWord) ImageView mIvSubmitWord;

    private Context mContext;
    private PublishSubject<String> mHelperStringsSubject;

    public GuessWordThisUserViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mContext = context;
    }

    public void bindRecyclerViewGuessHelpers(List<String> helperStrings){
        GuessWordsHelperAdapter adapter = new GuessWordsHelperAdapter(helperStrings);
        mRvGuessHelpers.setItemAnimator(new DefaultItemAnimator());
        mRvGuessHelpers.setLayoutManager(new LinearLayoutManager(
                mContext,LinearLayoutManager.HORIZONTAL, false));
        mRvGuessHelpers.setAdapter(adapter);
        mHelperStringsSubject = adapter.getHelperStringsSubject();
    }

    public void bindText(String text){
        if(!TextUtils.isEmpty(text)){
            mEtGuessWord.setText(text);
        }
    }

    public void bindLoading(boolean isLoading){
        //TODO implement loading for guessing words
    }

    public Observable<String> submitWordObservable(){
        return Observable.create(emitter -> {
            mIvSubmitWord.setOnClickListener(view -> {
                emitter.onNext(mEtGuessWord.getText().toString().trim());
            });
        });
    }

    public PublishSubject<String> helperStringsObservable() {
        return mHelperStringsSubject;
    }

    public Context getContext() {
        return mContext;
    }
}