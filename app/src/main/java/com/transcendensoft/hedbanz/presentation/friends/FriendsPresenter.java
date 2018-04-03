package com.transcendensoft.hedbanz.presentation.friends;
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

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.domain.interactor.friends.AcceptFriend;
import com.transcendensoft.hedbanz.domain.interactor.friends.GetFriends;
import com.transcendensoft.hedbanz.domain.interactor.friends.RemoveFriend;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;

import java.net.ConnectException;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Implementation of friends presenter.
 * Getting and deleting friends from server and DB.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class FriendsPresenter extends BasePresenter<List<Friend>, FriendsContract.View>
        implements FriendsContract.Presenter {
    private GetFriends mGetFriendsInteractor;
    private AcceptFriend mAcceptFriendInteractor;
    private RemoveFriend mRemoveFriendInteractor;

    @Inject
    public FriendsPresenter(GetFriends getFriendsInteractor,
                            AcceptFriend acceptFriendInteractor,
                            RemoveFriend removeFriendInteractor) {
        this.mGetFriendsInteractor = getFriendsInteractor;
        this.mAcceptFriendInteractor = acceptFriendInteractor;
        this.mRemoveFriendInteractor = removeFriendInteractor;
    }

    @Override
    protected void updateView() {
        if (model == null || model.isEmpty()) {
            getFriends();
        } else {
            view().clearFriends();
            view().addFriendsToRecycler(model);
        }
    }

    @Override
    public void destroy() {
        mAcceptFriendInteractor.dispose();
        mGetFriendsInteractor.dispose();
        mRemoveFriendInteractor.dispose();
    }

    @Override
    public void deleteFriendWithId(Friend friend) {
        RemoveFriend.Param params = new RemoveFriend.Param(DataPolicy.API, friend.getId());
        mRemoveFriendInteractor.execute(params, () -> {
            // TODO friend deleted
        }, err -> {
            Timber.e(err);
            view().showShortToastMessage(R.string.error_server);
        });

    }

    @Override
    public void getFriends() {
        mGetFriendsInteractor.execute(DataPolicy.API,
                this::processGetFriendsOnNext,
                this::processGetFriendsOnError,
                () -> view().showContent(),
                (d) -> view().showLoading());

    }

    private void processGetFriendsOnNext(List<Friend> friends) {
        if (friends == null || friends.isEmpty()) {
            view().showEmptyList();
        } else {
            view().addFriendsToRecycler(friends);
        }
    }

    private void processGetFriendsOnError(Throwable err) {
        Timber.e(err);
        if (err instanceof ConnectException) {
            view().showNetworkError();
        } else {
            view().showServerError();
        }
    }
}
