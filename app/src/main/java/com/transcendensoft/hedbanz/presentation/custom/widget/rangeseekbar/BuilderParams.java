package com.transcendensoft.hedbanz.presentation.custom.widget.rangeseekbar;
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.transcendensoft.hedbanz.utils.ViewUtils;

/**
 * //TODO add class description
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class BuilderParams{
    Context mContext;
    //seekBar
    int mSeekBarType = 0;
    float mMax = 100;
    float mMin = 0;
    float mProgress1 = 0;
    float mProgress2 = 0;
    boolean mClearPadding = false;
    boolean mIsFloatProgress = false;
    boolean mForbidUserSeek = false;
    boolean mTouchToSeek = true;
    //indicator
    int mIndicatorType = 0;
    boolean mShowIndicator = true;
    boolean mIndicatorStay = false;
    int mIndicatorColor = Color.parseColor("#FF4081");
    int mIndicatorTextColor = Color.parseColor("#FFFFFF");
    int mIndicatorTextSize;
    View mIndicatorCustomView = null;
    View mIndicatorCustomTopContentView = null;
    //track
    int mBackgroundTrackSize;
    int mProgressTrackSize;
    int mBackgroundTrackColor = Color.parseColor("#D7D7D7");
    int mProgressTrackColor = Color.parseColor("#FF4081");
    boolean mTrackRoundedCorners = true;
    //tick
    int mTickNum = 5;
    int mTickType = 1;
    int mTickSize;
    int mTickColor = Color.parseColor("#FF4081");
    boolean mTickHideBothEnds = false;
    boolean mTickOnThumbLeftHide = false;
    Drawable mTickDrawable = null;
    //text
    int mTextSize;
    int mTextColor = Color.parseColor("#FF4081");
    String mLeftEndText = null;
    String mRightEndText = null;
    CharSequence[] mTextArray = null;
    Typeface mTextTypeface = Typeface.DEFAULT;
    //thumb
    int mThumbColor = Color.parseColor("#FF4081");
    int mThumbSize;
    Drawable mThumbDrawable = null;
    boolean mThumbProgressStay = false;

    BuilderParams(Context context) {
        this.mContext = context;
        this.mIndicatorTextSize = ViewUtils.spToPx(mContext, 13);
        this.mBackgroundTrackSize = ViewUtils.dpToPx(mContext, 2);
        this.mProgressTrackSize = ViewUtils.dpToPx(mContext, 2);
        this.mTickSize = ViewUtils.dpToPx(mContext, 10);
        this.mTextSize = ViewUtils.spToPx(mContext, 13);
        this.mThumbSize = ViewUtils.dpToPx(mContext, 14);
    }

    BuilderParams copy(BuilderParams p) {
        this.mContext = p.mContext;
        //seekBar
        this.mSeekBarType = p.mSeekBarType;
        this.mMax = p.mMax;
        this.mMin = p.mMin;
        this.mProgress1 = p.mProgress1;
        this.mProgress2 = p.mProgress2;
        this.mClearPadding = p.mClearPadding;
        this.mIsFloatProgress = p.mIsFloatProgress;
        this.mForbidUserSeek = p.mForbidUserSeek;
        this.mTouchToSeek = p.mTouchToSeek;
        //indicator
        this.mIndicatorType = p.mIndicatorType;
        this.mShowIndicator = p.mShowIndicator;
        this.mIndicatorStay = p.mIndicatorStay;
        this.mIndicatorColor = p.mIndicatorColor;
        this.mIndicatorTextColor = p.mIndicatorTextColor;
        this.mIndicatorTextSize = p.mIndicatorTextSize;
        this.mIndicatorCustomView = p.mIndicatorCustomView;
        this.mIndicatorCustomTopContentView = p.mIndicatorCustomTopContentView;
        //track
        this.mBackgroundTrackSize = p.mBackgroundTrackSize;
        this.mProgressTrackSize = p.mProgressTrackSize;
        this.mBackgroundTrackColor = p.mBackgroundTrackColor;
        this.mProgressTrackColor = p.mProgressTrackColor;
        this.mTrackRoundedCorners = p.mTrackRoundedCorners;
        //tick
        this.mTickNum = p.mTickNum;
        this.mTickType = p.mTickType;
        this.mTickSize = p.mTickSize;
        this.mTickColor = p.mTickColor;
        this.mTickHideBothEnds = p.mTickHideBothEnds;
        this.mTickOnThumbLeftHide = p.mTickOnThumbLeftHide;
        this.mTickDrawable = p.mTickDrawable;
        //text
        this.mTextSize = p.mTextSize;
        this.mTextColor = p.mTextColor;
        this.mLeftEndText = p.mLeftEndText;
        this.mRightEndText = p.mRightEndText;
        this.mTextArray = p.mTextArray;
        this.mTextTypeface = p.mTextTypeface;
        //thumb
        this.mThumbColor = p.mThumbColor;
        this.mThumbSize = p.mThumbSize;
        this.mThumbDrawable = p.mThumbDrawable;
        this.mThumbProgressStay = p.mThumbProgressStay;
        return this;
    }
}

