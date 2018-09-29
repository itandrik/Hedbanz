package com.transcendensoft.hedbanz.presentation.game.list.holder;
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

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.utils.DateUtils;
import com.transcendensoft.hedbanz.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * for view that represents a message_received from another users.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class SomeUserMessageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ivUserImage) ImageView mIvUserImage;
    @BindView(R.id.tvMessage) TextView mTvMessage;
    @BindView(R.id.tvLogin) TextView mTvLogin;
    @BindView(R.id.tvTime) TextView mTvTime;
    @BindView(R.id.separator) View mSeparator;
    @BindView(R.id.rlSomeMessageContainer) RelativeLayout mRlContainer;
    @BindView(R.id.tvUserWord) TextView mTvUserWord;
    @BindView(R.id.ivWinIcon) ImageView mIvWin;

    private Context mContext;
    private View itemView;

    public SomeUserMessageViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.itemView = itemView;
        this.mContext = context;
    }

    public void bindUserWord(String word) {
        if (!TextUtils.isEmpty(word)) {
            mTvUserWord.setVisibility(View.VISIBLE);
            mTvUserWord.setText(word);
        } else {
            mTvUserWord.setVisibility(View.GONE);
        }
    }

    public void bindMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            mTvMessage.setText(message);
        } else {
            mTvMessage.setText("");
        }

        if (mTvUserWord.getVisibility() == View.VISIBLE) {
            ((RelativeLayout.LayoutParams) mTvMessage.getLayoutParams()).topMargin =
                    ViewUtils.dpToPx(mContext, 16.f);
        } else if (mIvUserImage.getVisibility() == View.VISIBLE) {
            ((RelativeLayout.LayoutParams) mTvMessage.getLayoutParams()).topMargin =
                    ViewUtils.dpToPx(mContext, 8.f);
        }
    }

    public void bindUserImage(@DrawableRes int drawableRes) {
        VectorDrawableCompat drawableCompat = VectorDrawableCompat.create(
                mContext.getResources(), drawableRes, null);
        mIvUserImage.setImageDrawable(drawableCompat);

        if (mIvUserImage.getVisibility() == View.VISIBLE) {
            ((RelativeLayout.LayoutParams) mIvUserImage.getLayoutParams()).topMargin =
                    ViewUtils.dpToPx(mContext, 16.f);
        } else {
            // ((RelativeLayout.LayoutParams) mIvUserImage.getLayoutParams()).topMargin =
            //        ViewUtils.dpToPx(mContext, 8.f);
        }
    }

    public void bindUserLogin(String login) {
        if (TextUtils.isEmpty(login)) {
            mTvLogin.setVisibility(View.GONE);
        } else {
            mTvLogin.setText(login);
        }
    }

    public void bindShowHideLoginAndImage(boolean isHide) {
        if (isHide) {
            mTvLogin.setVisibility(View.GONE);
            mTvUserWord.setVisibility(View.GONE);
            mIvUserImage.setVisibility(View.INVISIBLE);
            mSeparator.setVisibility(View.VISIBLE);
            mRlContainer.setMinimumHeight(0);
            ((RelativeLayout.LayoutParams) mTvMessage.getLayoutParams())
                    .topMargin = ViewUtils.dpToPx(mContext, 3);
            ((RelativeLayout.LayoutParams) mIvUserImage.getLayoutParams())
                    .topMargin = ViewUtils.dpToPx(mContext, 3);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
            itemView.setLayoutParams(params);
        } else {
            mTvLogin.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mTvUserWord.getText().toString())) {
                mTvUserWord.setVisibility(View.VISIBLE);
            }
            mIvUserImage.setVisibility(View.VISIBLE);
            mSeparator.setVisibility(View.GONE);
            mRlContainer.setMinimumHeight(getListPreferredItemHeight());
        }
    }

    private int getListPreferredItemHeight() {
        int[] textSizeAttr = new int[]{android.R.attr.listPreferredItemHeight};
        int indexOfAttrTextSize = 0;
        TypedArray a = mContext.obtainStyledAttributes(new TypedValue().data, textSizeAttr);
        int height = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();

        return height;
    }

    public void bindTime(Long time) {
        String humanReadableTime = DateUtils.convertDateToHoursMinutes(time);
        if (!TextUtils.isEmpty(humanReadableTime)) {
            mTvTime.setText(humanReadableTime);
        } else {
            mTvTime.setText("");
        }
    }

    public void bindIsWinner(boolean isWinner, boolean isHide) {
        if (isWinner && !isHide) {
            mIvWin.setVisibility(View.VISIBLE);
            mTvUserWord.setVisibility(View.GONE);
        } else {
            mIvWin.setVisibility(View.GONE);
            if (TextUtils.isEmpty(mTvUserWord.getText())) {
                mTvUserWord.setVisibility(View.GONE);
            } else if (!isHide) {
                mTvUserWord.setVisibility(View.VISIBLE);
            }
        }
    }

    public void bindTopMargin(boolean isTopMarginNeeded) {
        if (isTopMarginNeeded) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.topMargin = ViewUtils.dpToPx(mContext, 8);
            itemView.setLayoutParams(params);
        }
    }
}
