package com.transcendensoft.hedbanz.presentation.game.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.presentation.game.list.holder.GuessWordHelperViewHolder
import io.reactivex.Observable

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
 * Adapter for helper words, when some user try to guess his word
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class GuessWordsHelperAdapter(private val helperStrings: List<String>):
        RecyclerView.Adapter<GuessWordHelperViewHolder>() {
    var helperStringsObservable: Observable<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuessWordHelperViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_guess_word_helper, parent, false)
        return GuessWordHelperViewHolder(itemView)
    }

    override fun getItemCount(): Int = helperStrings.size

    override fun onBindViewHolder(holder: GuessWordHelperViewHolder, position: Int) {
        val helperString = helperStrings[position]
        holder.bindText(helperString)
        helperStringsObservable = holder.guessWordHelperObservable()
    }
}