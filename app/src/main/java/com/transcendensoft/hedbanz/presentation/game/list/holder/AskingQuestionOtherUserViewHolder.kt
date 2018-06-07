package com.transcendensoft.hedbanz.presentation.game.list.holder

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.DrawableRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.User
import com.transcendensoft.hedbanz.utils.DateUtils
import com.transcendensoft.hedbanz.utils.ViewUtils
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_asking_question_this_user.view.*
import kotlinx.android.synthetic.main.item_message_some_user.view.*

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
 * for view that represents asking question of some other User
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class AskingQuestionOtherUserViewHolder(context: Context, itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val mCvThumbsUp = itemView?.cvThumbsUp
    private val mCvThumbsDown = itemView?.cvThumbsDown
    private val mPbThumbsUp = itemView?.numberProgressBarThumbsUp
    private val mPbThumbsDown = itemView?.numberProgressBarThumbsDown
    private val mTvPlayersThumbsUp = itemView?.tvPlayersThumbsUp
    private val mTvPlayersThumbsDown = itemView?.tvPlayersThumbsDown
    private val mThumbsUpPlayersDivider = itemView?.dividerThumbsUpPlayers
    private val mThumbsDownPlayersDivider = itemView?.dividerThumbsDownPlayers
    private val mTvUserWord = itemView?.tvUserWord
    private val mTvMessage = itemView?.tvMessage
    private val mIvUserImage = itemView?.ivUserImage
    private val mTvLogin = itemView?.tvLogin
    private val mSeparator = itemView?.separator
    private val mRlContainer = itemView?.rlSomeMessageContainer
    private val mTvTime = itemView?.tvTime
    private val mTvTotal = itemView?.tvTotal

    private val mContext = context


    fun bindUserWord(word: String?) {
        if(word.isNullOrEmpty()){
            mTvUserWord?.visibility = View.GONE
        } else {
            mTvUserWord?.visibility = View.VISIBLE
            mTvUserWord?.text = word
        }
    }

    fun bindMessage(message: String) {
        if (!TextUtils.isEmpty(message)) {
            mTvMessage?.text = message
        } else {
            mTvMessage?.text = ""
        }

        if (mTvUserWord?.visibility == View.VISIBLE) {
            (mTvMessage?.layoutParams as RelativeLayout.LayoutParams).topMargin =
                    ViewUtils.dpToPx(mContext, 16f)
        } else {
            (mTvMessage?.layoutParams as RelativeLayout.LayoutParams).topMargin =
                    ViewUtils.dpToPx(mContext, 8f)
        }
    }

    fun bindUserImage(@DrawableRes drawableRes: Int) {
        val drawableCompat = VectorDrawableCompat.create(
                mContext.resources, R.drawable.logo, null)
        mIvUserImage?.setImageDrawable(drawableCompat)

        if (mIvUserImage?.visibility == View.VISIBLE) {
            (mIvUserImage.layoutParams as RelativeLayout.LayoutParams).topMargin =
                    ViewUtils.dpToPx(mContext, 16f)
        } else {
            (mIvUserImage?.layoutParams as RelativeLayout.LayoutParams).topMargin =
                    ViewUtils.dpToPx(mContext, 8f)
        }
    }

    fun bindUserLogin(login: String?) {
        if (login.isNullOrEmpty()) {
            mTvLogin?.visibility = View.GONE
        } else {
            mTvLogin?.text = login
        }
    }

    fun bindShowHideLoginAndImage(isHide: Boolean) {
        if (isHide) {
            mTvLogin?.visibility = View.GONE
            mIvUserImage?.visibility = View.INVISIBLE
            mSeparator?.visibility = View.VISIBLE
            mRlContainer?.minimumHeight = 0
        } else {
            mTvLogin?.visibility = View.VISIBLE
            mIvUserImage?.visibility = View.VISIBLE
            mSeparator?.visibility = View.GONE
            mRlContainer?.minimumHeight = getListPreferredItemHeight()
        }
    }

    private fun getListPreferredItemHeight(): Int {
        val textSizeAttr = intArrayOf(android.R.attr.listPreferredItemHeight)
        val indexOfAttrTextSize = 0
        val a = mContext.obtainStyledAttributes(TypedValue().data, textSizeAttr)
        val textSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1)
        a.recycle()

        return textSize
    }

    fun bindTime(time: Long?) {
        val humanReadableTime = DateUtils.convertDateToHoursMinutes(time)
        if (!TextUtils.isEmpty(humanReadableTime)) {
            mTvTime?.text = humanReadableTime
        } else {
            mTvTime?.text = ""
        }
    }

    /*------------------------------------*
     *-------- Card view binding ---------*
     *------------------------------------*/
    fun bindProgress(usersThumbsUp: List<User>, usersThumbsDown: List<User>, allUsersCount: Int) {
        setProgressBarsMax(allUsersCount)

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
            (mTvTotal?.layoutParams as RelativeLayout.LayoutParams)
                    .addRule(RelativeLayout.BELOW, R.id.tvPlayersThumbsDown)
        } else {
            mTvPlayersThumbsDown?.visibility = View.VISIBLE
            mThumbsDownPlayersDivider?.visibility = View.VISIBLE
            mTvPlayersThumbsDown?.text = usersThumbsUp.joinToString(separator = ", ")
            mPbThumbsDown?.progress = usersThumbsDown.size
            (mTvTotal?.layoutParams as RelativeLayout.LayoutParams)
                    .addRule(RelativeLayout.BELOW, R.id.dividerThumbsDownPlayers)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalInfo(usersThumbsUp: List<User>, usersThumbsDown: List<User>, allUsersCount: Int) {
        mTvTotal?.let {
            mTvTotal.text = "${mContext.getString(R.string.game_asking_total_votes)} " +
                    "${usersThumbsUp.size + usersThumbsDown.size}/$allUsersCount"
        }
    }

    fun thumbsUpClickObservable(questionId: Long) =
            Observable.create<Long> { emitter ->
                mCvThumbsUp?.setOnClickListener {
                    emitter.onNext(questionId)
                }
            }!!

    fun thumbsDownClickObservable(questionId: Long) =
            Observable.create<Long> { emitter ->
                mCvThumbsDown?.setOnClickListener {
                    emitter.onNext(questionId)
                }
            }!!
}