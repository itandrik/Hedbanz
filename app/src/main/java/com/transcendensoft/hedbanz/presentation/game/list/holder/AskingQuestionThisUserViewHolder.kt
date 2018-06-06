package com.transcendensoft.hedbanz.presentation.game.list.holder

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.User
import com.transcendensoft.hedbanz.utils.DateUtils
import kotlinx.android.synthetic.main.item_asking_question_this_user.view.*
import kotlinx.android.synthetic.main.item_message_this_user.view.*
import timber.log.Timber
import java.sql.Timestamp

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
 * for view that represents asking question of current User
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class AskingQuestionThisUserViewHolder(context: Context, itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val mPbThumbsUp = itemView?.numberProgressBarThumbsUp
    private val mPbThumbsDown = itemView?.numberProgressBarThumbsDown
    private val mTvPlayersThumbsUp = itemView?.tvPlayersThumbsUp
    private val mTvPlayersThumbsDown = itemView?.tvPlayersThumbsDown
    private val mThumbsUpPlayersDivider = itemView?.dividerThumbsUpPlayers
    private val mThumbsDownPlayersDivider = itemView?.dividerThumbsDownPlayers
    private val mTvMessage = itemView?.tvMessage
    private val mTvTime = itemView?.tvTime
    private val mPbLoading = itemView?.pbMessageLoading
    private val mTvTotal = itemView?.tvTotal
    private val mContext = context

    fun bindMessage(message: String) {
        if (!TextUtils.isEmpty(message)) {
            mTvMessage?.text = message
        } else {
            mTvMessage?.text = ""
        }
    }

    fun bindTime(time: Timestamp?) {
        if (time != null) {
            val humanReadableTime = DateUtils.convertDateToHoursMinutes(time.time)
            if (!TextUtils.isEmpty(humanReadableTime)) {
                mTvTime?.text = humanReadableTime
            } else {
                mTvTime?.text = ""
            }
        } else {
            mTvTime?.text = ""
        }
    }

    fun bindLoading(isLoading: Boolean, isFinished: Boolean) {
        if (isLoading && !isFinished) {
            mPbLoading?.visibility = View.VISIBLE
            mTvTime?.text = ""
        } else if (isFinished && !isLoading) {
            mPbLoading?.visibility = View.GONE
        }
    }

    /*------------------------------------*
     *-------- Card view binding ---------*
     *------------------------------------*/
    fun bindProgress(usersThumbsUp: List<User>, usersThumbsDown: List<User>, allUsersCount: Int){
        setProgressBarsMax(allUsersCount)

        if(usersThumbsUp.size + usersThumbsDown.size != allUsersCount){
            Timber.e("Error, while bind progress in asking question item." +
                    " ThumbUp + ThumbDown users != allUsersCount")
        }

        setThumbsUpInfo(usersThumbsUp)
        setThumbsDownInfo(usersThumbsDown, usersThumbsUp)

        setTotalInfo(usersThumbsUp, usersThumbsDown, allUsersCount)
    }

    private fun setProgressBarsMax(allUsersCount: Int) {
        mPbThumbsUp?.max = allUsersCount
        mPbThumbsDown?.max = allUsersCount
    }

    private fun setThumbsUpInfo(usersThumbsUp: List<User>) {
        if (usersThumbsUp.isEmpty()) {
            mTvPlayersThumbsUp?.visibility = View.GONE
            mThumbsUpPlayersDivider?.visibility = View.GONE
        } else {
            mTvPlayersThumbsUp?.visibility = View.VISIBLE
            mThumbsUpPlayersDivider?.visibility = View.VISIBLE
            mTvPlayersThumbsUp?.text = usersThumbsUp.joinToString(separator = ", ")
            mPbThumbsUp?.progress = usersThumbsUp.size
        }
    }

    private fun setThumbsDownInfo(usersThumbsDown: List<User>, usersThumbsUp: List<User>) {
        if (usersThumbsDown.isEmpty()) {
            mTvPlayersThumbsDown?.visibility = View.GONE
            mThumbsDownPlayersDivider?.visibility = View.GONE
        } else {
            mTvPlayersThumbsDown?.visibility = View.VISIBLE
            mThumbsDownPlayersDivider?.visibility = View.VISIBLE
            mTvPlayersThumbsDown?.text = usersThumbsUp.joinToString(separator = ", ")
            mPbThumbsDown?.progress = usersThumbsDown.size
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalInfo(usersThumbsUp: List<User>, usersThumbsDown: List<User>, allUsersCount: Int) {
        mTvTotal?.let {
            mTvTotal.text = "${mContext.getString(R.string.game_asking_total_votes)} " +
                    "${usersThumbsUp.size + usersThumbsDown.size}/$allUsersCount"
        }
    }
}