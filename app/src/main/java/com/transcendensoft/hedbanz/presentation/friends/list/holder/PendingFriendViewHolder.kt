package com.transcendensoft.hedbanz.presentation.friends.list.holder

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.item_friend_pending.view.*

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
 * for view that represents pending friend
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class PendingFriendViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    private var mIvIcon = itemView.ivFriendIcon
    private var mTvFriendName = itemView.tvFriendName

    fun bindName(name: String) {
        if (!TextUtils.isEmpty(name)) {
            mTvFriendName?.text = name
        } else {
            mTvFriendName?.text = ""
        }
    }

    fun bindIcon(@DrawableRes drawableIcon: Int) {
        mIvIcon?.setImageResource(drawableIcon)
    }
}