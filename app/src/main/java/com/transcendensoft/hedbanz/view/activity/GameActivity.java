package com.transcendensoft.hedbanz.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.model.entity.Room;
import com.transcendensoft.hedbanz.presenter.PresenterManager;
import com.transcendensoft.hedbanz.presenter.impl.GamePresenterImpl;
import com.transcendensoft.hedbanz.view.GameView;

import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity implements GameView{

    private GamePresenterImpl mPresenter;
    private ProgressDialog mProgressDialog;

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this, this);

        initPresenter(savedInstanceState);
        initProgressDialog();
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
            mPresenter.disconnectSockets();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(mPresenter, outState);
    }

    @Override
    public Context provideContext() {
        return this;
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    private void initPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mPresenter = new GamePresenterImpl();
        } else {
            mPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        if (mPresenter != null && getIntent() != null) {
            long roomId = getIntent().getLongExtra(getString(R.string.bundle_room_id), 0L);
            mPresenter.setModel(new Room.Builder().setId(roomId).build());
        }
    }

    private void initProgressDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.action_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
    }

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
}
