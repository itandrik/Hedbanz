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
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Question;
import com.transcendensoft.hedbanz.presentation.game.list.GuessWordsHelperAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * {@link RecyclerView.ViewHolder}
 * for view that represents data when user guess some word
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GuessWordThisUserViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tilGuessWord) TextInputLayout mTilGuessWord;
    @BindView(R.id.tietGuessWord) TextInputEditText mTietGuessWord;
    @BindView(R.id.rvGuessHelpers) RecyclerView mRvGuessHelpers;
    @BindView(R.id.ivSubmitWord) ImageView mIvSubmitWord;
    @BindView(R.id.pbGuessLoading) ProgressBar mPbGuessLoading;
    @BindView(R.id.tvGuessWordTitle) TextView mTvGuessWordTitle;

    private Context mContext;
    private PublishSubject<Question> mHelperStringsObservable = PublishSubject.create();

    public GuessWordThisUserViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mContext = context;
    }

    public void bindRecyclerViewGuessHelpers(List<String> helperStrings, long questionId) {
        if (mRvGuessHelpers.getAdapter() == null || mRvGuessHelpers.getAdapter().getItemCount() == 0) {
            GuessWordsHelperAdapter adapter = new GuessWordsHelperAdapter(helperStrings, questionId);
            mRvGuessHelpers.setItemAnimator(new DefaultItemAnimator());
            mRvGuessHelpers.setLayoutManager(new LinearLayoutManager(
                    mContext, LinearLayoutManager.HORIZONTAL, false));
            mRvGuessHelpers.setAdapter(adapter);

            adapter.getHelperStringsSubject()
                    .doOnNext(question -> {
                        mTietGuessWord.setText(question.getMessage());
                        mTietGuessWord.clearFocus();
                    }).subscribe(mHelperStringsObservable);
        }
    }

    public void bindText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTietGuessWord.setText(text);
        } else {
            mTietGuessWord.setText("");
        }
    }

    public void bindTitle(int attempts) {
        String text = mContext.getString(R.string.game_this_user_think_word_to_user, attempts);

        if (attempts <= 0 || attempts > 3) {
            mTvGuessWordTitle.setText(text.substring(0, text.indexOf('.')));
        } else {
            mTvGuessWordTitle.setText(text);
        }
    }

    public void bindLoading(boolean isLoading, boolean isFinished) {
        if (isLoading && !isFinished) {
            mIvSubmitWord.setVisibility(View.GONE);
            mPbGuessLoading.setVisibility(View.VISIBLE);
            mIvSubmitWord.getDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.google_green),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            mTietGuessWord.setEnabled(false);
            mTilGuessWord.setEnabled(false);
            mTietGuessWord.clearFocus();
            mTilGuessWord.clearFocus();
            mIvSubmitWord.setEnabled(false);
        } else if (!isLoading && isFinished) {
            mIvSubmitWord.setVisibility(View.VISIBLE);
            mIvSubmitWord.getDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.textSecondary),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            mPbGuessLoading.setVisibility(View.GONE);
            mTietGuessWord.setEnabled(false);
            mIvSubmitWord.setEnabled(false);
            mRvGuessHelpers.setEnabled(false);
            ((GuessWordsHelperAdapter) mRvGuessHelpers.getAdapter()).disable();
            mTietGuessWord.clearFocus();
            mTilGuessWord.clearFocus();
        } else {
            mIvSubmitWord.setVisibility(View.VISIBLE);
            mPbGuessLoading.setVisibility(View.GONE);
            mIvSubmitWord.getDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.google_green),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            mRvGuessHelpers.setEnabled(true);
            ((GuessWordsHelperAdapter) mRvGuessHelpers.getAdapter()).enable();
            mTietGuessWord.setEnabled(true);
            mIvSubmitWord.setEnabled(true);
        }
    }

    public Observable<Question> submitWordObservable(Long questionId) {
        return Observable.create(emitter -> {
            mIvSubmitWord.setOnClickListener(view -> {
                Question question = new Question();

                String text = mTietGuessWord.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    mTilGuessWord.setError(mContext.getString(R.string.game_guess_word_error));
                } else {
                    mTilGuessWord.setError(null);
                    question.setMessage(transformInputTextAccordingRules(text));
                    mTietGuessWord.setText(question.getMessage());
                    question.setQuestionId(questionId);

                    emitter.onNext(question);
                }
            });
        });
    }

    public Observable<Question> getHelperStringsObservable(long questionId) {
        GuessWordsHelperAdapter adapter = (GuessWordsHelperAdapter)(mRvGuessHelpers.getAdapter());
        adapter.setQuestionId(questionId);
        adapter.notifyDataSetChanged();

        return mHelperStringsObservable;
    }

    /*public Observable<Question> helperStringsObservable(Long questionId) {
        if (mHelperStringsObservable != null) {
            return mHelperStringsObservable.map(text -> {
                Question question = new Question();
                question.setMessage(mTietGuessWord.getText().toString().trim());
                question.setQuestionId(questionId);

                return question;
            });
        } else {
            return Observable.empty();
        }
    }*/

    public Observable<Boolean> guessWordEtFocusedObservable(){
        return Observable.create(emitter -> {
            mTietGuessWord.setOnFocusChangeListener((v, hasFocus) -> {
                emitter.onNext(hasFocus);
            });
        });
    }

    @NonNull
    private String transformInputTextAccordingRules(String text) {
        String startText = mContext.getString(R.string.game_set_word_start_text) + " ";
        String endText = "?";
        if (!text.toUpperCase().startsWith(startText.toUpperCase())) {
            text = startText + text.substring(0,1).toLowerCase() + text.substring(1);
        } else {
            text = startText + text.substring(2);
        }

        if (!text.endsWith(endText)) {
            text = text + endText;
        }
        return text;
    }

    public Context getContext() {
        return mContext;
    }
}
