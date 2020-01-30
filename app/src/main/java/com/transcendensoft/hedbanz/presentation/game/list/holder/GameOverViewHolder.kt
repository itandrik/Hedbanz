package com.transcendensoft.hedbanz.presentation.game.list.holder

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.transcendensoft.hedbanz.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_game_over.view.*

/**
 * Copyright 2018. Andrii Chernysh
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
 * for view that represents game over.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class GameOverViewHolder(val context: Context, itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    fun bindEnabledButtons(isEnabled: Boolean){
        if(isEnabled){
            itemView.btnRestart?.isEnabled = true
            itemView.btnRestart?.isClickable = true
            itemView.btnRestart?.setBackgroundResource(R.drawable.restart_button_bg)
            itemView.btnCancel?.setTextColor(ContextCompat.getColor(context, R.color.textSecondaryDark))

            itemView.btnCancel?.isEnabled = true
            itemView.btnCancel?.isClickable = true
            itemView.btnCancel?.setBackgroundResource(R.drawable.cancel_button_bg)
        } else {
            itemView.btnRestart?.isEnabled = false
            itemView.btnRestart?.isClickable = false
            itemView.btnRestart?.setBackgroundResource(R.drawable.message_this_user)
            itemView.btnRestart?.setTextColor(ContextCompat.getColor(context, R.color.textSecondaryDark))

            itemView.btnCancel?.isEnabled = false
            itemView.btnCancel?.isClickable = false
            itemView.btnCancel?.setBackgroundResource(R.drawable.message_other_user)
        }
    }

    fun restartClickObservable() =
            Observable.create<View> {emitter ->
                itemView.btnRestart?.setOnClickListener {
                    emitter.onNext(it)
                }
            }!!

    fun cancelClickObservable() =
            Observable.create<View> { emitter ->
                itemView.btnCancel?.setOnClickListener {
                    emitter.onNext(it)
                }
            }!!
}