package com.transcendensoft.hedbanz.presentation.roomcreation;
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
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.di.qualifier.ActivityContext;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.presentation.base.BaseFragment;
import com.transcendensoft.hedbanz.presentation.game.GameActivity;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity;
import com.transcendensoft.hedbanz.presentation.rooms.models.RoomList;
import com.transcendensoft.hedbanz.utils.AndroidUtils;
import com.transcendensoft.hedbanz.utils.KeyboardUtils;
import com.transcendensoft.hedbanz.utils.extension.ViewExtensionsKt;
import com.warkiz.widget.IndicatorSeekBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;

/**
 * Fragment, that shows room creation.
 * After room creation it opens and game starts.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class CreateRoomFragment extends BaseFragment implements CreateRoomContract.View {
    @BindView(R.id.tvErrorRoomName) TextView mTvErrorRoomName;
    @BindView(R.id.chbRoomPasswordTitle) AppCompatCheckBox mChbPasswordTitle;
    @BindView(R.id.tvErrorRoomPassword) TextView mTvErrorRoomPassword;
    @BindView(R.id.etRoomName) EditText mEtRoomName;
    @BindView(R.id.etRoomPassword) AppCompatEditText mEtRoomPassword;
    @BindView(R.id.isbPlayersQuantity) IndicatorSeekBar mIsbMaxPlayersQuantity;
    @BindView(R.id.svCreateRoom) ScrollView mSvContainer;

    @Inject CreateRoomPresenter mPresenter;
    @Inject @ActivityContext Context mContext;
    @Inject MainActivity mActivity;
    @Inject RoomList mPresenterModel;
    @Inject Gson mGson;

    @Inject
    public CreateRoomFragment() {
        //Requires empty public constructor
    }

    /*------------------------------------*
     *-------- Fragment lifecycle --------*
     *------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_creation, container, false);

        ButterKnife.bind(this, view);
        ViewExtensionsKt.setupKeyboardHiding(mSvContainer, mActivity);

        mPresenter.setModel(mPresenterModel);
        initPasswordCheckBox();
        initScrollContainer();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unbindView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    private void initPasswordCheckBox(){
        Disposable disposable = RxCompoundButton.checkedChanges(mChbPasswordTitle)
                .subscribe(isChecked -> {
                    if(isChecked){
                        mEtRoomPassword.setEnabled(true);
                        mTvErrorRoomPassword.setEnabled(true);
                        changeEditTextColor(mEtRoomPassword, R.color.colorPrimaryDark);
                        changeEditTextDrawable(R.drawable.ic_room_password,
                                R.color.textDarkRed, mEtRoomPassword);
                    } else {
                        mEtRoomPassword.setEnabled(false);
                        mTvErrorRoomPassword.setEnabled(false);
                        changeEditTextColor(mEtRoomPassword, R.color.textSecondaryLight);
                        changeEditTextDrawable(R.drawable.ic_room_password,
                                R.color.disabledField, mEtRoomPassword);
                    }
                });
        addRxBindingDisposable(disposable);
    }

    private void changeEditTextDrawable(@DrawableRes int drawable, @ColorRes int color,
                                        AppCompatEditText editText) {
        Drawable drawableLeft = VectorDrawableCompat.create(getResources(), drawable, null);
        drawableLeft = DrawableCompat.wrap(drawableLeft);
        DrawableCompat.setTint(drawableLeft, ContextCompat.getColor(
                mContext, color));
        editText.setCompoundDrawablesWithIntrinsicBounds(
                drawableLeft, null, null, null);
    }

    private void changeEditTextColor(AppCompatEditText editText, @ColorRes int color) {
        editText.getBackground().mutate()
                .setColorFilter(ContextCompat.getColor(
                        mContext, color),
                        PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void setPresenterModel(RoomList model) {
        if(mPresenter != null){
            mPresenter.setModel(model);
        }
    }

    private int mScrollY;
    private void initScrollContainer(){
        mSvContainer.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = mSvContainer.getScrollY(); // For ScrollView
            if(getActivity() != null && Math.abs(mScrollY - scrollY) > 20) {
                KeyboardUtils.hideSoftInput(getActivity());
            }

            mScrollY = scrollY;
        });
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.btnCreateRoom)
    protected void onCreateRoomClicked() {
        if (mPresenter != null) {
            Room room = new Room.Builder()
                    .setWithPassword(mChbPasswordTitle.isChecked())
                    .setMaxPlayers((byte) mIsbMaxPlayersQuantity.getProgress())
                    .setName(mEtRoomName.getText().toString())
                    .setPassword(mEtRoomPassword.getText().toString())
                    .build();
            mPresenter.createRoom(room);
        }
    }

    /*------------------------------------*
     *-------- Presenter results ---------*
     *------------------------------------*/
    @Override
    public void createRoomSuccess(Room room) {
        hideLoadingDialog();
        mEtRoomName.setText("");
        mEtRoomPassword.setText("");
        mTvErrorRoomName.setVisibility(GONE);
        mTvErrorRoomPassword.setVisibility(GONE);

        Intent intent = new Intent(mContext, GameActivity.class);

        intent.putExtra(mContext.getString(R.string.bundle_room_id),(Long) room.getId());
        intent.putExtra(mContext.getString(R.string.bundle_is_after_creation), true);
        intent.putExtra(mContext.getString(R.string.bundle_room_data), mGson.toJson(room, Room.class));
        mContext.startActivity(intent);
    }

    @Override
    public void createRoomError() {
        hideLoadingDialog();
        mEtRoomPassword.setText("");
        AndroidUtils.showShortToast(getActivity(), R.string.room_creation_error_creating);
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void showServerError() {
        super.showServerError();
        AndroidUtils.showShortToast(getActivity(), R.string.error_server);
    }

    @Override
    public void showNetworkError() {
        super.showNetworkError();
        AndroidUtils.showShortToast(getActivity(), R.string.error_network);
    }

    @Override
    public void showLoading() {
        // Stub
    }

    @Override
    public void showContent() {
        // Stub
    }

    @Override
    public void showIncorrectRoomName(@StringRes int errorMessage) {
        hideLoadingDialog();
        mTvErrorRoomName.setVisibility(View.VISIBLE);
        mTvErrorRoomName.setText(getString(errorMessage));
    }

    @Override
    public void showIncorrectRoomPassword(@StringRes int errorMessage) {
        hideLoadingDialog();
        mTvErrorRoomPassword.setVisibility(View.VISIBLE);
        mTvErrorRoomPassword.setText(getString(errorMessage));
    }

    @Override
    public void showMaxActiveRoomsError() {
        hideLoadingDialog();
        Drawable d = VectorDrawableCompat.create(getResources(), R.drawable.ic_unhappy, null);
        new AlertDialog.Builder(mActivity)
                .setPositiveButton(getString(R.string.action_ok), (dialog, which) -> dialog.dismiss())
                .setOnDismissListener(dialog -> {
                    mEtRoomPassword.setText("");
                    mEtRoomName.setText("");
                })
                .setIcon(d)
                .setTitle(getString(R.string.game_error_title))
                .setMessage(getString(R.string.room_user_has_max_active_rooms_number))
                .show();
    }

    @Override
    public void hideAll() {
        hideLoadingDialog();
        mTvErrorRoomName.setVisibility(GONE);
        mTvErrorRoomPassword.setVisibility(GONE);
    }
}
