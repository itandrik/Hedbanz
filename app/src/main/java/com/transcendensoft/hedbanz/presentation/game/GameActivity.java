package com.transcendensoft.hedbanz.presentation.game;

import android.animation.Animator;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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

import com.google.gson.Gson;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.game.list.GameListAdapter;
import com.transcendensoft.hedbanz.utils.KeyboardUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.OVER_SCROLL_NEVER;

public class GameActivity extends BaseActivity implements GameContract.View {
    @Inject GamePresenter mPresenter;
    @Inject GameListAdapter mAdapter;
    @Inject Gson mGson;

    @BindView(R.id.rvGameList) RecyclerView mRecycler;
    @BindView(R.id.etChatMessage) EditText mEtChatMessage;
    @BindView(R.id.rlGameDataContainer) RelativeLayout mRlDataContainer;
    @BindView(R.id.rlErrorNetwork) RelativeLayout mRlErrorNetwork;
    @BindView(R.id.rlErrorServer) RelativeLayout mRlErrorServer;
    @BindView(R.id.flLoadingContainer) FrameLayout mFlLoadingContainer;
    @BindView(R.id.tvSystemField) TextView mTvSystemField;
    @BindView(R.id.ivSystemAnimation) ImageView mIvSystemAnimation;
    @BindView(R.id.ivSystemSadIcon) ImageView mIvSystemSad;
    @BindView(R.id.ivSystemHappyIcon) ImageView mIvSystemHappy;
    @BindView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @BindView(R.id.fabLogout) FloatingActionButton mFabLogout;
    @BindView(R.id.fabMenu) FloatingActionButton mFabMenu;


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
            String password = getIntent().getStringExtra(getString(R.string.bundle_room_password));
            boolean isAfterRoomCreation = getIntent()
                    .getBooleanExtra(getString(R.string.bundle_is_after_creation), false);
            String roomIntentString = getIntent().getStringExtra(getString(R.string.bundle_room_data));

            Room room;
            if(roomIntentString != null){
                room = mGson.fromJson(roomIntentString, Room.class);
                // After room creation
            } else {
                // After simple opening room
                room = new Room.Builder()
                        .setId(roomId)
                        .setWithPassword(true)
                        .setPassword(password)
                        .build();
            }

            mPresenter.setAfterRoomCreation(isAfterRoomCreation);
            mPresenter.setModel(room);
        }

        initNavDrawer();
        initRecycler();

        mPresenter.messageTextChanges(mEtChatMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
            if ((mRecycler != null) && (mAdapter != null) &&
                    (mAdapter.getItems() != null) &&
                    (mAdapter.getItems().size() > 0)) {
                mRecycler.smoothScrollToPosition(mAdapter.getItems().size() - 1);
            }
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
    private void initNavDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, null,
                R.string.game_toolbar_open_menu_content_description,
                R.string.game_toolbar_close_menu_content_description);
        toggle.setDrawerIndicatorEnabled(false);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initRecycler() {
        mAdapter.setBottomReachedListener(mPresenter);
        initAdapterClickListeners();

        mRecycler.setAdapter(mAdapter);
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        mRecycler.setLayoutManager(manager);
        mRecycler.setItemViewCacheSize(100);
        mRecycler.setOverScrollMode(OVER_SCROLL_NEVER);

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                showOrHideFab(mFabLogout, dy);
                showOrHideFab(mFabMenu, dy);
                if (dy < -50) {
                    KeyboardUtils.hideSoftInput(GameActivity.this);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void showOrHideFab(FloatingActionButton fab, int dy) {
        if (dy < 0 && fab.isShown()) {
            fab.hide();
        } else if (dy > 0) {
            fab.show();
        }
    }

    private void initAdapterClickListeners() {
        if (mPresenter != null) {
            mPresenter.processRetryNetworkPagination(
                    mAdapter.retryNetworkClickObservable());
            mPresenter.processRetryServerPagination(
                    mAdapter.retryServerClickObservable());
            mPresenter.processSetWordToUserObservable(
                    mAdapter.setWordObservable());
            mPresenter.processGuessWordHelperText(
                    mAdapter.guessWordHelperStringObservable());
            mPresenter.processGuessWordSubmit(
                    mAdapter.guessWordSubmitObservable());
            mPresenter.processThumbsUpClick(
                    mAdapter.askingQuestionThumbsUpObservable());
            mPresenter.processThumbsDownClick(
                    mAdapter.askingQuestionThumbsDownObservable());
            mPresenter.processWinClick(
                    mAdapter.askingQuestionWinObservable());
            mPresenter.processRestartGameClick(
                    mAdapter.restartGameObservable());
            mPresenter.processCancelGameClick(
                    mAdapter.cancelGameObservable());
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

    @OnClick(R.id.fabLogout)
    protected void onExitFromRoom() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setMessage(getString(R.string.game_exit_room_message))
                    .setTitle(getString(R.string.game_exit_room_title))
                    .setPositiveButton(getString(R.string.game_action_exit_game), (dialog, which) -> {
                        super.onBackPressed();
                    })
                    .setNegativeButton(getString(R.string.game_action_resume_game), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        }
    }

    @OnClick(R.id.fabMenu)
    protected void onMenuClicked() {
        mDrawerLayout.openDrawer(GravityCompat.END);
        KeyboardUtils.hideSoftInput(this);
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
            //mRecycler.scrollToPosition(position + messages.size());
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

    @Override
    public void invalidateMessageWithPosition(int position) {
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(position);
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
    public void showWinDialog() {
        showShortToastMessage("You won!!!");
    }

    @Override
    public void showFooterTyping(List<User> users) {
        mIvSystemSad.setVisibility(View.GONE);
        mIvSystemHappy.setVisibility(View.GONE);
        if (users == null || users.isEmpty()) {
            mTvSystemField.setText("");
            stopTypingAnimation();
        } else {
            String typingText = getTypingMessage(users);
            mTvSystemField.setText(typingText);
            mTvSystemField.setTextColor(ContextCompat.getColor(this, R.color.textSecondary));
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
        Animatable typingAnimatable = ((Animatable) mIvSystemAnimation.getDrawable());
        if (typingAnimatable != null) {
            typingAnimatable.stop();
            mIvSystemAnimation.setVisibility(View.INVISIBLE);
            mIvSystemAnimation.setImageDrawable(null);
        }
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
        mTvSystemField.setTextColor(ContextCompat.getColor(this, R.color.google_red));
        mIvSystemSad.setVisibility(View.VISIBLE);
        mIvSystemHappy.setVisibility(View.GONE);
    }

    @Override
    public void showFooterDisconnected() {
        mTvSystemField.setText(getString(R.string.game_error_disconnected));
        mTvSystemField.setTextColor(ContextCompat.getColor(this, R.color.google_red));
        mIvSystemSad.setVisibility(View.VISIBLE);
        mIvSystemHappy.setVisibility(View.GONE);
    }

    @Override
    public void showFooterReconnected() {
        mTvSystemField.setText(getString(R.string.game_error_reconnected));
        mTvSystemField.setTextColor(ContextCompat.getColor(this, R.color.google_green));
        mIvSystemSad.setVisibility(View.GONE);
        mIvSystemHappy.setVisibility(View.VISIBLE);
        mTvSystemField.animate()
                .alpha(0.f)
                .setStartDelay(2000)
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mTvSystemField.setText("");
                        mTvSystemField.setAlpha(1.f);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        mIvSystemHappy.animate()
                .alpha(0.f)
                .setStartDelay(2000)
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mIvSystemHappy.setAlpha(1.f);
                        mIvSystemHappy.setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

    }

    @Override
    public void showFooterReconnecting() {
        mTvSystemField.setText(getString(R.string.game_error_reconnecting));
        mTvSystemField.setTextColor(ContextCompat.getColor(this, R.color.google_red));
        mIvSystemSad.setVisibility(View.VISIBLE);
        mIvSystemHappy.setVisibility(View.GONE);
    }

    @Override
    public void showRestoreRoom() {
        new AlertDialog.Builder(this)
                .setPositiveButton(getString(R.string.game_action_restore_room), (dialog, which) -> {
                    mPresenter.restoreRoom();
                })
                .setNegativeButton(getString(R.string.game_action_leave_room), (dialog, which) -> {
                    finish();
                })
                .setTitle(getString(R.string.game_restore_room_title))
                .setMessage(getString(R.string.game_restore_room_message))
                .show();
    }

    @Override
    public void showUserAfk(boolean isAfk, String login) {
        if (TextUtils.isEmpty(login)) {
            if (isAfk) {
                showShortToastMessage(R.string.game_some_user_afk);
            } else {
                showShortToastMessage(R.string.game_some_user_returned);
            }
        } else {
            if (isAfk) {
                showShortToastMessage(getString(R.string.game_user_afk, login));
            } else {
                showShortToastMessage(getString(R.string.game_user_returned, login));
            }
        }
    }
}
