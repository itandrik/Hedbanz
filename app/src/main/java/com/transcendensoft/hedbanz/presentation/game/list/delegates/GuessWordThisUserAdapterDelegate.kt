package com.transcendensoft.hedbanz.presentation.game.list.delegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.Message
import com.transcendensoft.hedbanz.domain.entity.MessageType
import com.transcendensoft.hedbanz.presentation.base.RxAdapterDelegate
import com.transcendensoft.hedbanz.presentation.game.list.holder.GuessWordThisUserViewHolder
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

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
 * This delegate is responsible for creating {@link GuessWordThisUserViewHolder}
 * and binding ViewHolder widgets according to model.
 *
 * An AdapterDelegate get added to an AdapterDelegatesManager.
 * This manager is the man in the middle between RecyclerView.
 * Adapter and each AdapterDelegate.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class GuessWordThisUserAdapterDelegate @Inject constructor(
        private val guessWordSubject: PublishSubject<String> = PublishSubject.create(),
        private var helperStringSubject: PublishSubject<String> = PublishSubject.create()
) : RxAdapterDelegate<List<@JvmSuppressWildcards Message>>() {

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val context = parent?.context
        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_guess_word_this_user, parent, false)
        return GuessWordThisUserViewHolder(context, itemView)
    }

    override fun isForViewType(items: List<Message>, position: Int): Boolean {
        val message = items[position]
        return message.messageType == MessageType.GUESS_WORD_THIS_USER
    }

    override fun onBindViewHolder(items: List<Message>, position: Int,
                                  holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        if (holder is GuessWordThisUserViewHolder) {
            val helperStringsArray = holder.context.resources
                    .getStringArray(R.array.guess_helpers)
            holder.bindRecyclerViewGuessHelpers(helperStringsArray.toList())
            holder.submitWordObservable().subscribe(guessWordSubject)
            helperStringSubject = holder.helperStringsObservable()
        }
    }

    fun guessWordObservable() = guessWordSubject

    fun guessWordHelperStringsObservable() = helperStringSubject
}