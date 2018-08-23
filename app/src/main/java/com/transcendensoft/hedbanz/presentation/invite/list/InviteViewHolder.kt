package com.transcendensoft.hedbanz.presentation.invite.list

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.Friend
import kotlinx.android.synthetic.main.item_friend_invite.view.*

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
 * for view that represents information about friend that
 * we want to invite to game
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class InviteViewHolder(private val mContext: Context,
                       private val mItemView: View): RecyclerView.ViewHolder(mItemView) {
    private val mIvIcon: ImageView? = mItemView.ivFriendIcon
    private val mTvFriendName: TextView? = mItemView.tvFriendName
    private val mTvSystemText: TextView? = mItemView.tvSystemText
    private val mChbSelect: AppCompatCheckBox? = mItemView.chbFriendSelected

    fun bindFriendIcon(@DrawableRes drawable: Int){
        mIvIcon?.setImageResource(drawable)
    }

    fun bindFriendName(name: String?){
        if(name.isNullOrEmpty()){
            mTvFriendName?.text = ""
        } else {
            mTvFriendName?.text = name
        }
    }

    fun bindFlags(isInGame: Boolean, isInvited: Boolean){
        when {
            isInGame -> {
                mTvSystemText?.text = mContext.getString(R.string.invite_already_in_game)
                mTvSystemText?.visibility = View.VISIBLE
                mChbSelect?.visibility = View.GONE
                (mTvFriendName?.layoutParams as RelativeLayout.LayoutParams)
                        .addRule(RelativeLayout.LEFT_OF, R.id.tvSystemText)
            }
            isInvited -> {
                mTvSystemText?.text = mContext.getString(R.string.invite_already_invited)
                mTvSystemText?.visibility = View.VISIBLE
                mChbSelect?.visibility = View.GONE
                (mTvFriendName?.layoutParams as RelativeLayout.LayoutParams)
                        .addRule(RelativeLayout.LEFT_OF, R.id.tvSystemText)
            }
            else -> {
                mTvSystemText?.visibility = View.GONE
                mChbSelect?.visibility = View.VISIBLE
                (mTvFriendName?.layoutParams as RelativeLayout.LayoutParams)
                        .addRule(RelativeLayout.LEFT_OF, R.id.chbFriendSelected)
            }
        }
    }

    fun bindOnClick(friend: Friend) {
        mItemView.setOnClickListener {
            mChbSelect?.isSelected = !mChbSelect?.isSelected!!
            friend.isSelected = mChbSelect.isSelected
        }
    }
}