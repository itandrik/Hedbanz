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

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.presentation.base.MvpViewHolder;
import com.transcendensoft.hedbanz.presentation.rooms.RoomsPresenter;
import com.transcendensoft.hedbanz.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * View holder realization for concrete room.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */

public class RoomItemViewHolder extends MvpViewHolder<RoomItemPresenterImpl> implements RoomItemContract.View {
    @BindView(R.id.tvRoomName) TextView mTvName;
    @BindView(R.id.ivRoomIcon) ImageView mIvIcon;
    @BindView(R.id.ivRoomLocked) ImageView mIvProtected;
    @BindView(R.id.rlItemContainer) RelativeLayout mCardContainer;
    @BindView(R.id.llErrorContainer) LinearLayout mLlError;
    @BindView(R.id.tvRoomsErrorText) TextView mTvErrorText;
    @BindView(R.id.btnReload) Button mBtnRetryError;
    @BindView(R.id.pbRoomLoading) ProgressBar mPbLoading;
    @BindView(R.id.tvActive) TextView mTvActive;
    @BindView(R.id.flRoomIconContainer) FrameLayout mFlRoomIconContainer;
    @BindView(R.id.llUsersContainer) LinearLayout mLlUsersContainer;

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private RoomsPresenter mCallbackPresenter;

    public RoomItemViewHolder(Context context, View itemView, RoomsPresenter callbackPresenter) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        mContext = context;
        this.mCallbackPresenter = callbackPresenter;

        setOnClickListeners(callbackPresenter);
        initProgressDialog();
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.action_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
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
    public void setIcon(int icon, @DrawableRes int sticker) {
        Drawable iconDrawable = VectorDrawableCompat.create(mContext.getResources(), icon, null);
        Drawable stickerDrawable = VectorDrawableCompat.create(mContext.getResources(), sticker, null);

        mIvIcon.setImageDrawable(iconDrawable);
        mFlRoomIconContainer.setBackground(stickerDrawable);
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
        mLlUsersContainer.removeAllViews();

        int size = ViewUtils.dpToPx(mContext, 16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);

        for (int i = 0; i < currentPlayers; i++) {
            Drawable d = VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_user, null);
            ImageView image = new ImageView(mContext);
            image.setImageDrawable(d);
            image.setLayoutParams(params);

            mLlUsersContainer.addView(image);
        }

        for (int i = 0; i < maxPlayers - currentPlayers; i++) {
            Drawable d = VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_user, null);
            ImageView image = new ImageView(mContext);
            image.setImageDrawable(d);
            image.setAlpha(0.5f);
            image.setLayoutParams(params);

            mLlUsersContainer.addView(image);
        }
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
    public void setIsActive(boolean isActive) {
        if (isActive) {
            mTvActive.setVisibility(View.VISIBLE);
        } else {
            mTvActive.setVisibility(View.GONE);
        }
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

    @Override
    public ViewGroup getItemView() {
        return (ViewGroup) itemView;
    }

    @Override
    public Context provideContext() {
        return mContext;
    }

    @Override
    public void showLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }
}
