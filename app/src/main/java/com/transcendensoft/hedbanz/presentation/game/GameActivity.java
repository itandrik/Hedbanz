package com.transcendensoft.hedbanz.presentation.game;

import android.os.Bundle;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.models.RoomDTO;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class GameActivity extends BaseActivity implements GameContract.View{
    @Inject GamePresenter mPresenter;

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this, this);

        if (mPresenter != null && getIntent() != null) {
            long roomId = getIntent().getLongExtra(getString(R.string.bundle_room_id), 0L);
            mPresenter.setModel(new RoomDTO.Builder().setId(roomId).build());
        }
        //TODO initRecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unbindView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void showServerError() {

    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void hideAll() {

    }
}
