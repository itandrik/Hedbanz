package com.transcendensoft.hedbanz.presentation.changeicon.list

import android.content.Context
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_user_icon.view.*

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
 * for view that represents available icon for user
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class ChangeIconViewHolder(private val context: Context,
                           private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {
    fun bindUserIcon(iconId: Int) {
        val drawable = VectorDrawableCompat.create(context.resources, iconId, null)
        mItemView.ivIcon?.setImageDrawable(drawable)
    }

    fun bindIconSelected(isSelected: Boolean){
        val visibility =  if(isSelected) View.VISIBLE else View.GONE
        mItemView.ivUserIconCheck?.visibility = visibility
    }

    fun bindUserIconClickListener(selectableIcon: SelectableIcon) =
            Observable.create<Int> { emitter ->
                mItemView.setOnClickListener {
                    emitter.onNext(selectableIcon.iconId)
                }
            }
}