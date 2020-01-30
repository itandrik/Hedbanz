package com.transcendensoft.hedbanz.presentation.game.list.delegates

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager
import com.transcendensoft.hedbanz.domain.entity.Message
import com.transcendensoft.hedbanz.domain.entity.MessageType
import com.transcendensoft.hedbanz.domain.entity.Question
import com.transcendensoft.hedbanz.presentation.game.list.holder.AskingQuestionOtherUserViewHolder
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
 * This delegate is responsible for creating {@link AskingQuestionOtherUserViewHolder}
 * and binding ViewHolder widgets according to model.
 *
 * An AdapterDelegate get added to an AdapterDelegatesManager.
 * This manager is the man in the middle between RecyclerView.
 * Adapter and each AdapterDelegate.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class AskingQuestionOtherUserAdapterDelegate @Inject constructor(
        private val preferenceManager: PreferenceManager
) :
        AdapterDelegate<List<@JvmSuppressWildcards Message>>() {
    private val thumbsUpSubject: PublishSubject<Long> = PublishSubject.create()
    private val thumbsDownSubject: PublishSubject<Long> = PublishSubject.create()
    private val winSubject: PublishSubject<Long> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup?): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val context = parent?.context
        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_asking_question_other_user, parent, false)
        return AskingQuestionOtherUserViewHolder(context!!, itemView)
    }

    override fun isForViewType(items: List<Message>, position: Int): Boolean {
        val message = items[position]
        return message.messageType == MessageType.ASKING_QUESTION_OTHER_USER &&
                message is Question
    }

    override fun onBindViewHolder(items: List<Message>, position: Int,
                                  holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val question = items[position] as Question
        val currentUser = preferenceManager.user

        if (holder is AskingQuestionOtherUserViewHolder) {
            val userFrom = question.userFrom

            var login: String? = null
            var word: String? = null
            if (userFrom != null) {
                login = userFrom.login
                word = userFrom.word
            }

            var isAddTopMargin = false
            if (items.size > 1 && position != 0) {
                val prevMessage = items[position - 1]
                if (prevMessage.userFrom != null) {
                    if (prevMessage.userFrom != userFrom) {
                        isAddTopMargin = true
                    }
                }
            }

            holder.bindUserWord(word)
            holder.bindShowHideLoginAndImage(false)
            holder.bindUserLogin(login)
            holder.bindMessage(question.message)
            holder.bindUserImage(userFrom?.iconId?.resId ?: R.drawable.logo)
            holder.bindTime(question.createDate.time)

            holder.bindProgress(question.yesVoters, question.noVoters,
                    question.winVoters, question.allUsersCount ?: 0)
            holder.bindWin(question.isWin)
            holder.bindTopMargin(isAddTopMargin)

            val isEnableThumbsDownClick = !question.noVoters.contains(currentUser)
            holder.thumbsDownClickObservable(question.questionId, isEnableThumbsDownClick)
                    .subscribe(thumbsDownSubject)

            val isEnableThumbsUpClick = !question.yesVoters.contains(currentUser)
            holder.thumbsUpClickObservable(question.questionId, isEnableThumbsUpClick)
                    .subscribe(thumbsUpSubject)

            val isEnableWinClick = !question.winVoters.contains(currentUser)
            holder.winClickObservable(question.questionId, isEnableWinClick)
                    .subscribe(winSubject)
        }
    }

    fun thumbsDownClickObservable() = thumbsDownSubject
    fun thumbsUpClickObservable() = thumbsUpSubject
    fun winClickObservable() = winSubject
}