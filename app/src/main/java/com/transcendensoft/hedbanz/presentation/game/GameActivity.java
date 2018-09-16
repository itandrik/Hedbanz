package com.transcendensoft.hedbanz.presentation.game;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
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
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.network.service.firebase.HedbanzFirebaseMessagingService;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.game.list.GameListAdapter;
import com.transcendensoft.hedbanz.presentation.notification.NotificationManager;
import com.transcendensoft.hedbanz.utils.AndroidUtils;
import com.transcendensoft.hedbanz.utils.KeyboardUtils;
import com.transcendensoft.hedbanz.utils.extension.ViewExtensionsKt;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.OVER_SCROLL_NEVER;
import static com.transcendensoft.hedbanz.data.network.service.firebase.HedbanzFirebaseMessagingService.ACTION_LAST_USER;

public class GameActivity extends BaseActivity implements GameContract.View {
    @Inject GamePresenter mPresenter;
    @Inject GameListAdapter mAdapter;
    @Inject Gson mGson;
    @Inject NotificationManager mNotificationManager;
    @Inject PreferenceManager mPreferenceManager;

    @BindView(R.id.rvGameList) RecyclerView mRecycler;
    @BindView(R.id.etChatMessage) EmojiEditText mEtChatMessage;
    @BindView(R.id.rlGameDataContainer) RelativeLayout mRlDataContainer;
    @BindView(R.id.rlErrorNetwork) ConstraintLayout mRlErrorNetwork;
    @BindView(R.id.rlErrorServer) ConstraintLayout mRlErrorServer;
    @BindView(R.id.flLoadingContainer) FrameLayout mFlLoadingContainer;
    @BindView(R.id.tvSystemField) TextView mTvSystemField;
    @BindView(R.id.ivSystemAnimation) ImageView mIvSystemAnimation;
    @BindView(R.id.ivSystemSadIcon) ImageView mIvSystemSad;
    @BindView(R.id.ivSystemHappyIcon) ImageView mIvSystemHappy;
    @BindView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @BindView(R.id.fabLogout) FloatingActionButton mFabLogout;
    @BindView(R.id.fabMenu) FloatingActionButton mFabMenu;
    @BindView(R.id.parent) RelativeLayout mParentLayout;
    @BindView(R.id.ivEmoji) ImageView mIvEmojiKeyboard;

    private BroadcastReceiver mLastPlayerBroadcastReceiver;
    private EmojiPopup mEmojiPopup;
    private boolean isKeyboardOpened = false;
    private boolean isScrollDown = false;
    private boolean isEmojiKeyboardImageClicked = false;
    private MediaPlayer mMediaPlayer;

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        ButterKnife.bind(this, this);
        ViewExtensionsKt.setupKeyboardHiding(mParentLayout, this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        MobileAds.initialize(this, getString(R.string.admob_ads_id));

        if (mPresenter != null && getIntent() != null) {
            long roomId = getIntent().getLongExtra(getString(R.string.bundle_room_id), 0L);
            String password = getIntent().getStringExtra(getString(R.string.bundle_room_password));
            boolean isAfterRoomCreation = getIntent()
                    .getBooleanExtra(getString(R.string.bundle_is_after_creation), false);
            boolean isActive = getIntent()
                    .getBooleanExtra(getString(R.string.bundle_is_active_game), false);
            String roomIntentString = getIntent().getStringExtra(getString(R.string.bundle_room_data));

            Room room;
            if (roomIntentString != null) {
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
            mPresenter.setIsActive(isActive);
            mPresenter.setModel(room);
        }

        initNavDrawer();
        initRecycler();
        initBroadcastReceivers();
        initKeyboardListener();
        initEmojiPopup();

        mPresenter.messageTextChanges(mEtChatMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.bindView(this);
            if ((mRecycler != null) && (mAdapter != null) &&
                    (mAdapter.getItems() != null) &&
                    (mAdapter.getItems().size() > 0) && !isScrollDown) {
                scrollToTheVeryDown();
            }
        }
        registerReceiver(mLastPlayerBroadcastReceiver, new IntentFilter(ACTION_LAST_USER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.unbindView();
        }
        stopTypingAnimation();
        unregisterReceiver(mLastPlayerBroadcastReceiver);
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
            mPresenter.processSetWordFocused(
                    mAdapter.setWordFocusedObservable());
            mPresenter.processGuessWordFocused(
                    mAdapter.guessWordFocusedObservable());
        }
    }

    private void initBroadcastReceivers() {
        mLastPlayerBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null &&
                        intent.getAction().equalsIgnoreCase(
                                HedbanzFirebaseMessagingService.ACTION_LAST_USER)) {
                    showLastUserDialog();
                }
            }
        };
    }

    private void initKeyboardListener() {
        mParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // mParentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Rect r = new Rect();
                mParentLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = mParentLayout.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    isKeyboardOpened = true;
                } else {
                    isKeyboardOpened = false;
                    mEmojiPopup.dismiss();
                    if (!isEmojiKeyboardImageClicked) {
                        Drawable imageDrawable = VectorDrawableCompat.create(
                                getResources(), R.drawable.ic_smile_keyboard, null);
                        mIvEmojiKeyboard.setImageDrawable(imageDrawable);
                    }
                }
                isEmojiKeyboardImageClicked = false;
            }
        });
    }

    private void initEmojiPopup() {
        mEmojiPopup = EmojiPopup.Builder.fromRootView(mParentLayout).build(mEtChatMessage);
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

    @OnFocusChange(R.id.etChatMessage)
    protected void onEtChatFocusChange(View view, boolean hasFocus) {
        if(hasFocus){
            isScrollDown = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            String text = "";
            String positiveButtonText = "";
            if (mPresenter.isGameActive()) {
                text = getString(R.string.game_exit_room_message_active);
                positiveButtonText = getString(R.string.game_action_exit_game_active);
            } else {
                text = getString(R.string.game_exit_room_message);
                positiveButtonText = getString(R.string.game_action_exit_game);
            }

            new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setMessage(text)
                    .setTitle(getString(R.string.game_exit_room_title))
                    .setPositiveButton(positiveButtonText, (dialog, which) -> {
                        dialog.dismiss();
                        leaveFromRoom(false);
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

    @OnClick(R.id.ivEmoji)
    protected void onEmojiKeyboardClicked() {
        mEmojiPopup.toggle();
        isEmojiKeyboardImageClicked = true;
        processSmileKeyboardIcon();
    }

    @OnClick(R.id.btnRetryServer)
    protected void OnRetryServerClicked() {
        mPresenter.refreshMessageHistory();
    }

    @OnClick(R.id.btnRetryNetwork)
    protected void OnRetryNetworkClicked() {
        mPresenter.refreshMessageHistory();
    }

    private void processSmileKeyboardIcon() {
        if (mEmojiPopup != null) {
            Drawable imageDrawable = null;
            if (mEmojiPopup.isShowing() || !isKeyboardOpened) {
                imageDrawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_keyboard, null);
            } else {
                imageDrawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_smile_keyboard, null);
            }
            mIvEmojiKeyboard.setImageDrawable(imageDrawable);
        }
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void addMessage(Message message) {
        if (mAdapter != null) {
            mAdapter.add(message);
            if (!isScrollDown) {
                scrollToTheVeryDown();
            }
        }
    }

    @Override
    public void addMessage(int position, Message message) {
        if (mAdapter != null) {
            mAdapter.add(position, message);
            if (!isScrollDown) {
                scrollToTheVeryDown();
            }
        }
    }

    private void scrollToTheVeryDown() {
        mRecycler.post(() -> {
            try {
                mRecycler.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            } catch (IllegalArgumentException e) {
                Timber.e(e);
            }
        });
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
            scrollToTheVeryDown();
        }
    }

    @Override
    public void addMessages(int position, List<Message> messages) {
        if (mAdapter != null) {
            mAdapter.addAll(position, messages);
            scrollToTheVeryDown();
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

    @Override
    public void setIsScrollDownMessages(boolean isScroll) {
        isScrollDown = isScroll;
    }

    @Override
    public void focusMessageEditText() {
        mEtChatMessage.requestFocus();
        scrollToTheVeryDown();
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
        Drawable icon = VectorDrawableCompat.create(getResources(), R.drawable.ic_win_happy, null);
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.game_win_user_alert_message))
                .setTitle(getString(R.string.game_win_user_alert_title))
                .setPositiveButton(getString(R.string.action_ok), (dialog, which) -> dialog.dismiss())
                .setIcon(icon)
                .show();
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
            stopTypingAnimation();
            startTypingAnimation();
        }
    }

    private void startTypingAnimation() {
        AnimatedVectorDrawableCompat avd =
                AnimatedVectorDrawableCompat.create(this, R.drawable.typing_animation);
        mIvSystemAnimation.setImageDrawable(avd);
        avd.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                startTypingAnimation();
            }
        });
        //animatedVectorDrawableCompat.registerAnimationCallback(mAnimationCallback);
        //mIvSystemAnimation.setImageDrawable(animatedVectorDrawableCompat);
        avd.start();
        mIvSystemAnimation.setVisibility(View.VISIBLE);
    }

    private void stopTypingAnimation() {
        AnimatedVectorDrawableCompat typingAnimatable = ((AnimatedVectorDrawableCompat) mIvSystemAnimation.getDrawable());
        if (typingAnimatable != null) {
            typingAnimatable.clearAnimationCallbacks();
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
                    leaveFromRoom(false);
                    dialog.dismiss();
                })
                .setCancelable(false)
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

    @Override
    public void showUserKicked() {
        Drawable d = VectorDrawableCompat.create(getResources(), R.drawable.ic_unhappy, null);
        new AlertDialog.Builder(this)
                .setPositiveButton(getString(R.string.action_ok), (dialog, which) -> {
                    mNotificationManager.cancelKickNotification();
                    mPreferenceManager.setIsUserKicked(false);
                    dialog.dismiss();
                    leaveFromRoom(true);
                })
                .setOnDismissListener(dialog -> {
                    mNotificationManager.cancelKickNotification();
                    mPreferenceManager.setIsUserKicked(false);
                    dialog.dismiss();
                    leaveFromRoom(true);
                })
                .setIcon(d)
                .setTitle(getString(R.string.game_kicked_title))
                .setMessage(getString(R.string.game_kicked_message))
                .show();
    }

    @Override
    public void showLastUserDialog() {
        if (!mPreferenceManager.isUserKicked()) {
            Drawable d = VectorDrawableCompat.create(getResources(), R.drawable.ic_unhappy, null);
            new AlertDialog.Builder(this)
                    .setPositiveButton(getString(R.string.action_ok), (dialog, which) -> {
                        mPreferenceManager.setIsLastUser(false);
                        dialog.dismiss();
                        leaveFromRoom(true);
                    })
                    .setOnDismissListener(dialog -> {
                        mPreferenceManager.setIsLastUser(false);
                        dialog.dismiss();
                        leaveFromRoom(true);
                    })
                    .setOnCancelListener(dialog -> {
                        mPreferenceManager.setIsLastUser(false);
                        dialog.dismiss();
                        leaveFromRoom(true);
                    })
                    .setIcon(d)
                    .setTitle(getString(R.string.game_last_player_title))
                    .setMessage(getString(R.string.game_last_player_message))
                    .show();
        }
    }

    @Override
    public void showErrorToast(@StringRes int message) {
        AndroidUtils.showLongToast(this, getString(message));
        hideLoadingDialog();
    }

    @Override
    public void showErrorDialog(@StringRes int message) {
        Drawable d = VectorDrawableCompat.create(getResources(), R.drawable.ic_unhappy, null);
        new AlertDialog.Builder(this)
                .setPositiveButton(getString(R.string.action_ok), (dialog, which) -> {
                    dialog.dismiss();
                    leaveFromRoom(true);
                })
                .setOnDismissListener(dialog -> leaveFromRoom(true))
                .setIcon(d)
                .setTitle(getString(R.string.game_error_title))
                .setMessage(getString(message))
                .show();
    }

    @Override
    public void showLeaveWhenServerError() {
        Drawable d = VectorDrawableCompat.create(getResources(), R.drawable.ic_dialog_server_error, null);
        new AlertDialog.Builder(this)
                .setPositiveButton(getString(R.string.game_action_resume_game),
                        (dialog, which) -> dialog.dismiss())
                .setNegativeButton(getString(R.string.game_action_leave_room),
                        (dialog, which) -> finish())
                .setIcon(d)
                .setTitle(getString(R.string.game_error_leave_when_server_error_title))
                .setMessage(getString(R.string.game_error_leave_when_server_error_message))
                .show();
    }

    private void leaveFromRoom(boolean isAfterErrorLastOrKicked) {
        if (mPresenter.doesGameHasServerConnectionError() && !isAfterErrorLastOrKicked) {
            showLeaveWhenServerError();
        } else {
            mNotificationManager.cancelAllLeaveFromRoomNotifications();
            mPresenter.setIsLeaveFromRoom(true);
            mPresenter.leaveFromRoom();
            mPresenter.destroy();
            finish();
        }
    }

    /*------------------------------------*
     *---------- Playing sounds ----------*
     *------------------------------------*/
    @Override
    public void playGameOverSound() {
        stopSound();
        mMediaPlayer = MediaPlayer.create(this, R.raw.game_over);
        mMediaPlayer.start();
    }

    @Override
    public void playGuessWordSound() {
        stopSound();
        mMediaPlayer = MediaPlayer.create(this, R.raw.guess_word);
        mMediaPlayer.start();
    }

    @Override
    public void playUserAskingSound() {
        stopSound();
        mMediaPlayer = MediaPlayer.create(this, R.raw.asking_question);
        mMediaPlayer.start();
    }

    @Override
    public void playMessageReceivedSound() {
        stopSound();
        mMediaPlayer = MediaPlayer.create(this, R.raw.message_received);
        mMediaPlayer.start();
    }

    @Override
    public void playUserKickedSound() {
        stopSound();
        mMediaPlayer = MediaPlayer.create(this, R.raw.user_kicked);
        mMediaPlayer.start();
    }

    @Override
    public void playWordSettingSound() {
        stopSound();
        mMediaPlayer = MediaPlayer.create(this, R.raw.word_setting);
        mMediaPlayer.start();
    }

    @Override
    public void playWinSound() {
        stopSound();
        mMediaPlayer = MediaPlayer.create(this, R.raw.win);
        mMediaPlayer.start();
    }

    private void stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
