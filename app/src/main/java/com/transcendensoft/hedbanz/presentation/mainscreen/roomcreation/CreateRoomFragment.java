package com.transcendensoft.hedbanz.presentation.mainscreen.roomcreation;
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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.entity.Room;
import com.transcendensoft.hedbanz.presentation.base.PresenterManager;
import com.transcendensoft.hedbanz.utils.AndroidUtils;
import com.warkiz.widget.IndicatorSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Fragment, that shows room creation.
 * After room creation it opens and game starts.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class CreateRoomFragment extends Fragment implements CreateRoomContract.View{
    @BindView(R.id.tvErrorRoomName) TextView mTvErrorRoomName;
    @BindView(R.id.tvErrorRoomPassword) TextView mTvErrorRoomPassword;
    @BindView(R.id.etRoomName) EditText mEtRoomName;
    @BindView(R.id.etRoomPassword) EditText mEtRoomPassword;
    @BindView(R.id.isbPlayersQuantity) IndicatorSeekBar mIsbMaxPlayersQuantity;

    private CreateRoomPresenter mPresenter;
    private ProgressDialog mProgress;

    /*------------------------------------*
     *-------- Fragment lifecycle --------*
     *------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_creation, container, false);

        ButterKnife.bind(this, view);

        initProgressDialog();
        initPresenter(savedInstanceState);

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(mPresenter, outState);
    }

    @Override
    public Context provideContext() {
        return getActivity();
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    private void initProgressDialog() {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(getString(R.string.action_loading));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }

    private void initPresenter(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            mPresenter = new CreateRoomPresenter();
        } else {
            mPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.btnCreateRoom)
    protected void onCreateRoomClicked(){
        if(mPresenter != null){
            Room room = new Room.Builder()
                    .setMaxPlayers((byte)mIsbMaxPlayersQuantity.getProgress())
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
        AndroidUtils.showShortToast(getActivity(), "Room created successfully");
        //TODO open view
    }

    @Override
    public void createRoomError(Room room) {
        AndroidUtils.showShortToast(getActivity(), "Error while room creation");
        //TODO show some message
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void showServerError() {
        hideLoading();
        AndroidUtils.showShortToast(getActivity(), R.string.error_server);
    }

    @Override
    public void showNetworkError() {
        hideLoading();
        AndroidUtils.showShortToast(getActivity(), R.string.error_network);
    }

    @Override
    public void showLoading() {
        hideAll();
        if (mProgress != null) {
            mProgress.show();
        }
    }

    @Override
    public void showContent() {
        // Stub
    }

    @Override
    public void showIncorrectRoomName(@StringRes int errorMessage) {
        hideLoading();
        mTvErrorRoomName.setVisibility(View.VISIBLE);
        mTvErrorRoomName.setText(errorMessage);
    }

    @Override
    public void showIncorrectRoomPassword(@StringRes int errorMessage) {
        hideLoading();
        mTvErrorRoomPassword.setVisibility(View.VISIBLE);
        mTvErrorRoomPassword.setText(errorMessage);
    }

    private void hideAll(){
        hideLoading();
        mTvErrorRoomName.setVisibility(GONE);
        mTvErrorRoomPassword.setVisibility(GONE);
    }

    private void hideLoading(){
        if(mProgress != null){
            mProgress.hide();
        }
    }
}
