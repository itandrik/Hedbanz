package com.transcendensoft.hedbanz.presentation.friends;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.friends.list.FriendsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Activity that shows friends. User can remove friend here.
 */
public class FriendsActivity extends BaseActivity implements FriendsContract.View{
    @BindView(R.id.rvFriends) RecyclerView mRecycler;
    @BindView(R.id.rlEmptyListContainer) RelativeLayout mRlEmptyList;
    @BindView(R.id.rlErrorNetwork) RelativeLayout mRlErrorNetwork;
    @BindView(R.id.rlErrorServer) RelativeLayout mRlErrorServer;
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

        initRecycler();
        addFriends();
    }

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    private void initRecycler(){
        mRecycler.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setAdapter(mAdapter);
    }

    private void addFriends(){
        Friend friend1 = new Friend.Builder()
                .setId(1)
                .setLogin("Friend 1")
                .setIsAccepted(false)
                .build();

        Friend friend2 = new Friend.Builder()
                .setId(2)
                .setLogin("Friend 2")
                .setIsAccepted(true)
                .build();

        Friend friend3 = new Friend.Builder()
                .setId(3)
                .setLogin("Friend 3")
                .setIsAccepted(true)
                .build();

        Friend friend4 = new Friend.Builder()
                .setId(4)
                .setLogin("Friend 4")
                .setIsAccepted(true)
                .build();

        Friend friend5 = new Friend.Builder()
                .setId(5)
                .setLogin("Friend 5")
                .setIsAccepted(false)
                .build();

        List<Friend> friends = new ArrayList<>();
        friends.add(friend1);
        friends.add(friend2);
        friends.add(friend3);
        friends.add(friend4);
        friends.add(friend5);

        Collections.sort(friends, (o1, o2) -> {
            if(o1.isAccepted() == o2.isAccepted()){
                return (int) (o1.getId() - o2.getId());
            } else if(o1.isAccepted()){
                return 1;
            } else {
                return -1;
            }
        });

        mAdapter.clearAndAddAll(friends);
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.ivBack)
    protected void onBackClicked(){
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
        mAdapter.addAll(friends);
    }

    @Override
    public void deleteFriend(Friend friend) {
        mAdapter.remove(friend);
    }

    @Override
    public void clearFriends() {
        mAdapter.clear();
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
