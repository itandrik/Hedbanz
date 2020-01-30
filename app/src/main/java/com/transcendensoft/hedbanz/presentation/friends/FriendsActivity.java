package com.transcendensoft.hedbanz.presentation.friends;

import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.friends.list.FriendsAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Activity that shows friends. User can remove friend here.
 */
public class FriendsActivity extends BaseActivity implements FriendsContract.View {
    @BindView(R.id.rvFriends) RecyclerView mRecycler;
    @BindView(R.id.rlEmptyListContainer) RelativeLayout mRlEmptyList;
    @BindView(R.id.rlErrorNetwork) ConstraintLayout mRlErrorNetwork;
    @BindView(R.id.rlErrorServer) ConstraintLayout mRlErrorServer;
    @BindView(R.id.flLoadingContainer) FrameLayout mFlLoadingContainer;

    @Inject FriendsAdapter mAdapter;
    @Inject FriendsPresenter mPresenter;

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        ButterKnife.bind(this, this);
        mPresenter.bindView(this);
        mPresenter.setModel(new ArrayList<>());
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
        mRecycler.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setAdapter(mAdapter);

        initRecyclerClickListeners();
    }

    private void initRecyclerClickListeners() {
        mPresenter.processAcceptFriendClick(
                mAdapter.acceptFriendObservable());
        mPresenter.processDeclineFriendClick(
                mAdapter.declineFriendObservable());
        mPresenter.processDeleteFriendClick(
                mAdapter.deleteFriendObservable());
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.ivBack)
    protected void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    @Override
    public void addFriendsToRecycler(List<Friend> friends) {
        if (mAdapter != null) {
            mAdapter.clearAndAddAll(friends);
        }
    }

    @Override
    public void deleteFriend(Friend friend) {
        if (mAdapter != null) {
            mAdapter.remove(friend);
        }
    }

    @Override
    public void clearFriends() {
        if (mAdapter != null) {
            mAdapter.clear();
        }
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    @Override
    public void sureToDeclineFriend(Friend friend) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.friends_decline_title))
                .setMessage(getString(R.string.friends_decline_message, friend.getLogin()))
                .setPositiveButton(getString(R.string.action_yes),
                        (dialog, v) -> {
                            mPresenter.declineFriend(friend);
                            dialog.dismiss();
                        })
                .setNegativeButton(getString(R.string.action_no),
                        (dialog, v) -> dialog.dismiss())
                .setIcon(R.drawable.ic_friendship)
                .show();
    }

    @Override
    public void sureToDeleteFriend(Friend friend) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.friends_delete_title))
                .setMessage(getString(R.string.friends_delete_message, friend.getLogin()))
                .setPositiveButton(getString(R.string.action_yes),
                        (dialog, v) -> {
                            mPresenter.deleteFriend(friend);
                            dialog.dismiss();
                        })
                .setNegativeButton(getString(R.string.action_no),
                        (dialog, v) -> dialog.dismiss())
                .setIcon(R.drawable.ic_friendship)
                .show();
    }

    @Override
    public void successAcceptFriend(Friend friend) {
        hideLoadingDialog();
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.friends_accept_success_title))
                .setMessage(getString(R.string.friends_accept_success_message, friend.getLogin()))
                .setPositiveButton(getString(R.string.action_ok),
                        (dialog, v) -> dialog.dismiss())
                .setIcon(R.drawable.ic_friendship)
                .show();
    }

    @Override
    public void successDeclineFriend(Friend friend) {
        hideLoadingDialog();
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.friends_decline_success_title))
                .setMessage(getString(R.string.friends_decline_success_message, friend.getLogin()))
                .setPositiveButton(getString(R.string.action_ok),
                        (dialog, v) -> dialog.dismiss())
                .setIcon(R.drawable.ic_friendship)
                .show();
    }

    @Override
    public void successDeleteFriend(Friend friend) {
        hideLoadingDialog();
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.friends_delete_success_title))
                .setMessage(getString(R.string.friends_delete_success_message, friend.getLogin()))
                .setPositiveButton(getString(R.string.action_ok),
                        (dialog, v) -> dialog.dismiss())
                .setIcon(R.drawable.ic_friendship)
                .show();
    }

    @Override
    public void errorFriend(Friend friend) {
        hideLoadingDialog();
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.friends_error_title))
                .setMessage(getString(R.string.friends_error_title))
                .setPositiveButton(getString(R.string.action_ok),
                        (dialog, v) -> dialog.dismiss())
                .setIcon(R.drawable.ic_dialog_server_error)
                .show();
    }

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
        mRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyList() {
        hideAll();
        mRlEmptyList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAll() {
        mRlErrorServer.setVisibility(GONE);
        mRlErrorNetwork.setVisibility(GONE);
        mFlLoadingContainer.setVisibility(GONE);
        mRlEmptyList.setVisibility(GONE);
        mRecycler.setVisibility(GONE);
    }
}
