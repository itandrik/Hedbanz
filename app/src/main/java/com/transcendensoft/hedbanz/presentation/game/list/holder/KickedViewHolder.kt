package com.transcendensoft.hedbanz.presentation.game.list.holder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.transcendensoft.hedbanz.R
import kotlinx.android.synthetic.main.item_kick.view.*

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
 * for view that shows that user has been kicked from game
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class KickedViewHolder (val mContext: Context?, itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val mTvUserKicked = itemView?.tvUserKicked

    fun bindUserLogin(login: String?) {
        mTvUserKicked?.text = mContext?.getString(R.string.game_user_kicked, login ?: "")
    }
}