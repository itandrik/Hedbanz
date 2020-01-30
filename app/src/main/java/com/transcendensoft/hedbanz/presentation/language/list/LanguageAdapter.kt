package com.transcendensoft.hedbanz.presentation.language.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.Language
import com.transcendensoft.hedbanz.domain.entity.SelectableLanguage
import javax.inject.Inject

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
/**
 * List adapter for languages with possibility of selecting
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class LanguageAdapter @Inject constructor() : androidx.recyclerview.widget.RecyclerView.Adapter<LanguageViewHolder>() {
    var items: MutableList<SelectableLanguage> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
            val selectedLanguage = value.first { it.isSelected }
            lastCheckedItem = selectedLanguage

            notifyDataSetChanged()
        }

    private var lastCheckedViewHolder: LanguageViewHolder? = null
    private var lastCheckedItem: SelectableLanguage? = null

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder =
            LanguageViewHolder(parent.context, LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_language, parent, false))

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val selectableLanguage = items[position]

        selectableLanguage.position = position
        if(lastCheckedViewHolder == null && lastCheckedItem == selectableLanguage){
            lastCheckedViewHolder = holder
        }

        holder.bindFlag(selectableLanguage.language.iconRes)
        holder.bindLanguageName(selectableLanguage.language.textRes)
        holder.bindIsSelected(selectableLanguage.isSelected)
        holder.bindOnClick(selectableLanguage) {
            lastCheckedViewHolder?.bindIsSelected(false)
            lastCheckedItem?.isSelected = false

            lastCheckedViewHolder = holder
            lastCheckedItem = selectableLanguage
        }
    }

    fun getSelectedLanguage() = lastCheckedItem?.language ?: Language.ENGLISH // Default
}