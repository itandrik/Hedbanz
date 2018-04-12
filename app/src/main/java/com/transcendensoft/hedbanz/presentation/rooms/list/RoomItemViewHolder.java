package com.transcendensoft.hedbanz.presentation.rooms.list;
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
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.presentation.base.MvpViewHolder;
import com.transcendensoft.hedbanz.presentation.rooms.RoomsPresenter;

/**
 * View holder realization for concrete room.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class RoomItemViewHolder extends MvpViewHolder<RoomItemPresenterImpl> implements RoomItemContract.View {
    private TextView mTvName;
    private ImageView mIvIcon;
    private ImageView mIvProtected;
    private TextView mTvCurAndMaxPlayers;
    private CardView mCardContainer;
    private LinearLayout mLlError;
    private TextView mTvErrorText;
    private Button mBtnRetryError;
    private ProgressBar mPbLoading;

    private Context mContext;

    public RoomItemViewHolder(Context context, View itemView, RoomsPresenter callbackPresenter) {
        super(itemView);

        //TODO try butterKnife
        mTvName = itemView.findViewById(R.id.tvRoomName);
        mIvIcon = itemView.findViewById(R.id.ivRoomIcon);
        mIvProtected = itemView.findViewById(R.id.ivRoomLocked);
        mPbLoading = itemView.findViewById(R.id.pbRoomLoading);
        mCardContainer = itemView.findViewById(R.id.roomCard);
        mLlError = itemView.findViewById(R.id.llErrorContainer);
        mTvErrorText = itemView.findViewById(R.id.tvRoomsErrorText);
        mBtnRetryError = itemView.findViewById(R.id.btnReload);

        mContext = context;
        setOnClickListeners(callbackPresenter);
    }

    private void setOnClickListeners(RoomsPresenter callbackPresenter) {
        mCardContainer.setOnClickListener(v -> {
            if (presenter != null) {
                presenter.onClickRoom();
            }
        });

        mBtnRetryError.setOnClickListener(v -> {
            callbackPresenter.loadNextRooms();
        });
    }

    @Override
    public void setIcon(int icon) {
        mIvIcon.setImageResource(icon);
    }

    @Override
    public void setName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mTvName.setText(name);
        } else {
            mTvName.setText("");
        }
    }

    @Override
    public void setCurAndMaxPlayers(int currentPlayers, int maxPlayers) {

    }

    @Override
    public void setIsProtected(boolean isProtected) {
        Drawable drawable;
        if (isProtected) {
            drawable = VectorDrawableCompat.create(
                    mContext.getResources(), R.drawable.ic_room_locked, null);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, R.color.textPrimary));
            mIvProtected.setImageResource(R.drawable.ic_room_locked);
        } else {
            drawable = VectorDrawableCompat.create(
                    mContext.getResources(), R.drawable.ic_password, null);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, R.color.textSecondaryLight));
        }
        mIvProtected.setImageDrawable(drawable);
    }

    @Override
    public void showLoadingItem() {
        hideAll();
        mPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCard() {
        hideAll();
        mCardContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorNetwork() {
        hideAll();
        mTvErrorText.setText(mContext.getString(R.string.error_network));
        mLlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorServer() {
        hideAll();
        mTvErrorText.setText(mContext.getString(R.string.error_server));
        mLlError.setVisibility(View.VISIBLE);
    }

    private void hideAll() {
        mLlError.setVisibility(View.GONE);
        mPbLoading.setVisibility(View.GONE);
        mCardContainer.setVisibility(View.GONE);
    }

    //public View getItemView(){
     //   return itemView;
   // }


    @Override
    public ViewGroup getItemView() {
        return (ViewGroup) itemView;
    }

    @Override
    public Context provideContext() {
        return mContext;
    }
}
