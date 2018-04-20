package com.transcendensoft.hedbanz.presentation.game;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.game.list.GameListAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class GameActivity extends BaseActivity implements GameContract.View {
    @Inject GamePresenter mPresenter;
    @Inject GameListAdapter mAdapter;

    @BindView(R.id.rvGameList) RecyclerView mRecycler;
    @BindView(R.id.etChatMessage) EditText mEtChatMessage;
    @BindView(R.id.rlGameDataContainer) RelativeLayout mRlDataContainer;
    @BindView(R.id.rlErrorNetwork) RelativeLayout mRlErrorNetwork;
    @BindView(R.id.rlErrorServer) RelativeLayout mRlErrorServer;
    @BindView(R.id.flLoadingContainer) FrameLayout mFlLoadingContainer;
    @BindView(R.id.tvSystemField) TextView mTvSystemField;
    @BindView(R.id.ivSystemAnimation) ImageView mIvSystemAnimation;

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
        initAdapterClickListeners();

        mRecycler.setAdapter(mAdapter);
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        mRecycler.setLayoutManager(manager);
    }

    private void initAdapterClickListeners() {
        if (mPresenter != null) {
            mPresenter.processRetryNetworkPagination(
                    mAdapter.retryNetworkClickObservable());
            mPresenter.processRetryServerPagination(
                    mAdapter.retryServerClickObservable());
        }
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.ivSend)
    protected void onSendMessageClicked() {
        String message = mEtChatMessage.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            mPresenter.sendMessage(message);
            mEtChatMessage.setText("");
        }
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void addMessage(Message message) {
        if (mAdapter != null) {
            mAdapter.add(message);
            mRecycler.smoothScrollToPosition(mAdapter.getItems().size() - 1);
        }
    }

    @Override
    public void addMessage(int position, Message message) {
        if (mAdapter != null) {
            mAdapter.add(position, message);
            mRecycler.smoothScrollToPosition(mAdapter.getItems().size() - 1);
        }
    }

    @Override
    public void clearMessages() {
        if (mAdapter != null) {
            mAdapter.clear();
        }
    }

    @Override
    public void addMessages(List<Message> messages) {
        if (mAdapter != null) {
            mAdapter.addAll(messages);
        }
    }

    @Override
    public void addMessages(int position, List<Message> messages) {
        if (mAdapter != null) {
            mAdapter.addAll(position, messages);
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
        if (mAdapter != null) {
            mAdapter.update(position, message);
        }
    }

    @Override
    public void removeLastMessage() {
        if (mAdapter != null) {
            mAdapter.remove(0);
        }
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void showServerError() {
        hideAll();
        mRlErrorServer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNetworkError() {
        hideAll();
        mRlErrorNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        hideAll();
        mFlLoadingContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent() {
        hideAll();
        mRlDataContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyList() {
        showContent();
    }

    @Override
    public void hideAll() {
        mRlErrorServer.setVisibility(GONE);
        mRlErrorNetwork.setVisibility(GONE);
        mFlLoadingContainer.setVisibility(GONE);
        mRlDataContainer.setVisibility(GONE);
    }

    @Override
    public void showFooterTyping(List<User> users) {
        if (users == null || users.isEmpty()) {
            mTvSystemField.setText("");
            stopTypingAnimation();
        } else {
            String typingText = getTypingMessage(users);
            mTvSystemField.setText(typingText);

            startTypingAnimation();
        }
    }

    private void startTypingAnimation() {
        final AnimatedVectorDrawableCompat avd =
                AnimatedVectorDrawableCompat.create(this, R.drawable.typing_animation);
        mIvSystemAnimation.setImageDrawable(avd);

        avd.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                avd.start();
            }
        });
        avd.start();
        mIvSystemAnimation.setVisibility(View.VISIBLE);
    }

    private void stopTypingAnimation() {
        ((Animatable) mIvSystemAnimation.getDrawable()).stop();
        mIvSystemAnimation.setVisibility(View.INVISIBLE);
        mIvSystemAnimation.setImageDrawable(null);
    }

    private String getTypingMessage(List<User> users) {
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("Typing indicator. Users list is NULL or empty.");
        } else if (users.size() == 1) {
            return getString(R.string.game_typing_one_user, users.get(0).getLogin());
        } else if (users.size() == 2) {
            return getString(R.string.game_typing_two_user,
                    users.get(0).getLogin(),
                    users.get(1).getLogin());
        } else {
            return getString(R.string.game_typing_several_users);
        }
    }

    @Override
    public void showFooterServerError() {
        mTvSystemField.setText(getString(R.string.error_server));
    }

    @Override
    public void showFooterNetworkError() {
        mTvSystemField.setText(getString(R.string.error_network));
    }
}
