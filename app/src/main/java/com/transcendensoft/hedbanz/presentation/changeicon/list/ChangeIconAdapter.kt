package com.transcendensoft.hedbanz.presentation.changeicon.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.transcendensoft.hedbanz.R
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

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
 * Adapter for change user icon recycler
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class ChangeIconAdapter @Inject constructor(): RecyclerView.Adapter<ChangeIconViewHolder>() {
    var items: MutableList<SelectableIcon> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }
    val clickSubject = PublishSubject.create<Int>()

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeIconViewHolder =
            ChangeIconViewHolder(parent.context, LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user_icon, parent, false))

    override fun onBindViewHolder(holder: ChangeIconViewHolder, position: Int) {
        val selectableIcon = items[position]

        holder.bindUserIcon(selectableIcon.iconId)
        holder.bindIconSelected(selectableIcon.isSelected)
        holder.bindUserIconClickListener(selectableIcon).subscribe(clickSubject)
    }
}