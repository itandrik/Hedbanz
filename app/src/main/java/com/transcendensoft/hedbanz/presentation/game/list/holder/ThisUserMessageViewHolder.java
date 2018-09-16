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

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.utils.DateUtils;

import java.sql.Timestamp;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * for view that represents a message_received from current user.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class ThisUserMessageViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tvMessage) TextView mTvMessage;
    @BindView(R.id.tvTime) TextView mTvTime;
    @BindView(R.id.pbMessageLoading) ProgressBar mPbLoading;

    public ThisUserMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindMessage(String message){
        if(!TextUtils.isEmpty(message)){
            mTvMessage.setText(message);
        } else {
            mTvMessage.setText("");
        }
    }

    public void bindTime(Timestamp time){
        if(time != null) {
            String humanReadableTime = DateUtils.convertDateToHoursMinutes(time.getTime());
            if (!TextUtils.isEmpty(humanReadableTime)) {
                mTvTime.setText(humanReadableTime);
            } else {
                mTvTime.setText("");
            }
        } else {
            mTvTime.setText("");
        }
    }

    public void bindLoading(boolean isLoading, boolean isFinished){
        if(isLoading && !isFinished){
            mPbLoading.setVisibility(View.VISIBLE);
            mTvTime.setText("");
        } else if(isFinished && !isLoading) {
            mPbLoading.setVisibility(View.GONE);
        }
    }
}
