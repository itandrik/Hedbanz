package com.transcendensoft.hedbanz.presentation.game.list.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_guess_word_helper.view.*
/**
 * Copyright 2017. Andrii Chernysh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * for view that represents helper string for guessing word
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class GuessWordHelperViewHolder(private val mItemView: View?) : RecyclerView.ViewHolder(mItemView) {
    private val mTvHelperGuessWord = mItemView?.tvGuessWordHelper
    private val mCvGuessWordHelper = mItemView?.cvGuessWordHelper

    fun bindText(text: String?) {
        if(!text.isNullOrEmpty()){
            mTvHelperGuessWord?.text = text
        }
    }

    fun guessWordHelperObservable() =
            Observable.create<String> { emitter ->
                mItemView?.setOnClickListener {
                    emitter.onNext(mTvHelperGuessWord?.text.toString().trim())
                }
            }!!

    fun setEnabled(isEnabled: Boolean){
        mCvGuessWordHelper?.isEnabled = isEnabled
        mCvGuessWordHelper?.isClickable = isEnabled
    }
}