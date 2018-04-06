package com.transcendensoft.hedbanz.presentation.game;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.game.list.GameListAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends BaseActivity implements GameContract.View {
    @Inject GamePresenter mPresenter;
    @Inject GameListAdapter mAdapter;

    @BindView(R.id.rvGameList) RecyclerView mRecycler;
    @BindView(R.id.etChatMessage) EditText mEtChatMessage;

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
            mPresenter.setModel(new Room.Builder().setId(roomId).build());
        }

        initRecycler();
        mPresenter.messageTextChanges(mEtChatMessage);
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
    private void initRecycler() {
        mAdapter.setBottomReachedListener(mPresenter);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        mRecycler.setLayoutManager(manager);
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.ivSend)
    protected void onSendMessageClicked(){
        mPresenter.sendMessage(mEtChatMessage.getText().toString());
        mEtChatMessage.setText("");
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void addMessage(Message message) {
        if (mAdapter != null) {
            mAdapter.add(message);
            mRecycler.smoothScrollToPosition(mAdapter.getItems().size()-1);
        }
    }

    @Override
    public void addMessage(int position, Message message) {
        if (mAdapter != null) {
            mAdapter.add(position, message);
            mRecycler.smoothScrollToPosition(mAdapter.getItems().size()-1);
        }
    }

    @Override
    public void clearAndAddMessages(List<Message> messages) {
        if (mAdapter != null) {
            mAdapter.clearAndAddAll(messages);
        }
    }

    @Override
    public void removeMessage(int position) {
        if (mAdapter != null) {
            mAdapter.remove(position);
        }
    }

    @Override
    public void setMessage(int position, Message message) {

    }

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
