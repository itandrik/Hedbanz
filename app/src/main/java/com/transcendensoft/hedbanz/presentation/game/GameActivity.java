package com.transcendensoft.hedbanz.presentation.game;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.game.list.GameListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends BaseActivity implements GameContract.View {
    @Inject GamePresenter mPresenter;
    @Inject GameListAdapter mAdapter;

    @BindView(R.id.rvGameList) RecyclerView mRecycler;

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
        mRecycler.setAdapter(mAdapter);
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        mRecycler.setLayoutManager(manager);

        //addTempMessages();
    }

    private void addTempMessages() {
        List<Message> messages = new ArrayList<>();

        Message message1 = new Message.Builder()
                .setMessage("Hello world")
                .setId(1)
                .setMessageType(MessageType.SIMPLE_MESSAGE_THIS_USER)
                .build();
        Message message2 = new Message.Builder()
                .setMessage("Hey I said: Hello world")
                .setId(2)
                .setMessageType(MessageType.SIMPLE_MESSAGE_THIS_USER)
                .build();

        User user1 = new User.Builder()
                .setId(1)
                .setLogin("user1")
                .build();
        Message message3 = new Message.Builder()
                .setMessage("user joined")
                .setId(3)
                .setMessageType(MessageType.JOINED_USER)
                .setUserFrom(user1)
                .build();

        User user2 = new User.Builder()
                .setId(2)
                .setLogin("user2")
                .build();
        Message message4 = new Message.Builder()
                .setMessage("user 2 joined")
                .setId(4)
                .setMessageType(MessageType.LEFT_USER)
                .setUserFrom(user2)
                .build();

        User user3 = new User.Builder()
                .setId(3)
                .setLogin("user3")
                .build();
        Message message5 = new Message.Builder()
                .setMessage("Heeey its me, user 3.")
                .setId(5)
                .setMessageType(MessageType.SIMPLE_MESSAGE_OTHER_USER)
                .setUserFrom(user3)
                .build();
        Message message10 = new Message.Builder()
                .setMessage("User 3 one more message")
                .setId(10)
                .setMessageType(MessageType.SIMPLE_MESSAGE_OTHER_USER)
                .setUserFrom(user3)
                .build();

        Message message6 = new Message.Builder()
                .setId(6)
                .setMessageType(MessageType.START_TYPING)
                .setUserFrom(user3)
                .build();

        Message message7 = new Message.Builder()
                .setId(7)
                .setMessageType(MessageType.START_TYPING)
                .setUserFrom(user2)
                .build();

        Message message8 = new Message.Builder()
                .setId(8)
                .setMessageType(MessageType.STOP_TYPING)
                .setUserFrom(user2)
                .build();

        Message message9 = new Message.Builder()
                .setId(9)
                .setMessage("Message before typing")
                .setMessageType(MessageType.SIMPLE_MESSAGE_THIS_USER)
                .build();

        /*processSimpleMessage(messages, message1);
        processSimpleMessage(messages, message2);
        processSimpleMessage(messages, message3);
        processSimpleMessage(messages, message4);
        processSimpleMessage(messages, message5);
        processSimpleMessage(messages, message10);
        processTypingMessage(messages, message6);
        processTypingMessage(messages, message7);
        processTypingMessage(messages, message8);
        processSimpleMessage(messages, message9);*/

        mAdapter.clearAndAddAll(messages);
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void addMessage(Message message) {
        if(mAdapter != null){
            mAdapter.add(message);
        }
    }

    @Override
    public void addMessage(int position, Message message) {
        if(mAdapter != null){
            mAdapter.add(position, message);
        }
    }

    @Override
    public void removeMessage(int position) {
        if(mAdapter != null){
            mAdapter.remove(position);
        }
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
