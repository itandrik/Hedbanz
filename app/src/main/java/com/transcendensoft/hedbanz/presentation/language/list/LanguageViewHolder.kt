package com.transcendensoft.hedbanz.presentation.language.list

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.transcendensoft.hedbanz.domain.entity.SelectableLanguage
import kotlinx.android.synthetic.main.item_language.view.*

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
 * View holder for recycler view that shows list of languages
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class LanguageViewHolder(private val mContext: Context,
                         private val mItemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(mItemView) {
    private val mTvLanguage = mItemView.tvCountryName
    private val mIvLanguage = mItemView.ivCountryFlag
    private val mRbLanguage = mItemView.rbCountrySelected

    fun bindFlag(@DrawableRes drawableRes: Int) {
        val drawable = VectorDrawableCompat.create(mContext.resources, drawableRes, null)
        mIvLanguage?.setImageDrawable(drawable)
    }

    fun bindLanguageName(@StringRes stringRes: Int) {
        mTvLanguage?.text = mContext.getString(stringRes)
    }

    fun bindIsSelected(isSelected: Boolean) {
        mRbLanguage?.isChecked = isSelected
    }

    fun bindOnClick(language: SelectableLanguage, clickCallback: () -> Unit) {
        mItemView?.setOnClickListener {
            if (!mRbLanguage!!.isChecked) {
                mRbLanguage.isChecked = true
            }
        }
        mRbLanguage?.setOnCheckedChangeListener { _, isChecked ->
            mRbLanguage.isSelected = isChecked
            language.isSelected = isChecked
            clickCallback()
        }
    }
}