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

import com.transcendensoft.hedbanz.data.network.retrofit.NoConnectivityException;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.domain.interactor.friends.AcceptFriend;
import com.transcendensoft.hedbanz.domain.interactor.friends.DeclineFriend;
import com.transcendensoft.hedbanz.domain.interactor.friends.GetFriends;
import com.transcendensoft.hedbanz.domain.interactor.friends.RemoveFriend;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
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
    private DeclineFriend mDeclineFriendInteractor;
    private RemoveFriend mRemoveFriendInteractor;
    private ObservableTransformer mObservableTransformer;

    @Inject
    public FriendsPresenter(GetFriends getFriendsInteractor,
                            AcceptFriend acceptFriendInteractor,
                            RemoveFriend removeFriendInteractor,
                            DeclineFriend declineFriendInteractor,
                            ObservableTransformer transformer) {
        this.mGetFriendsInteractor = getFriendsInteractor;
        this.mAcceptFriendInteractor = acceptFriendInteractor;
        this.mRemoveFriendInteractor = removeFriendInteractor;
        this.mDeclineFriendInteractor = declineFriendInteractor;
        this.mObservableTransformer = transformer;
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
        mDeclineFriendInteractor.dispose();
    }

    @Override
    public void processAcceptFriendClick(Observable<Friend> clickObservable) {
        addDisposable(clickObservable
                .compose(applySchedulers())
                .subscribe(this::acceptFriend, Timber::e));
    }

    @Override
    public void processDeclineFriendClick(Observable<Friend> clickObservable) {
        addDisposable(clickObservable
                .compose(applySchedulers())
                .subscribe(view()::sureToDeclineFriend, Timber::e));
    }

    @Override
    public void processDeleteFriendClick(Observable<Friend> clickObservable) {
        addDisposable(clickObservable
                .compose(applySchedulers())
                .subscribe(view()::sureToDeleteFriend, Timber::e));
    }

    @Override
    public void deleteFriend(Friend friend) {
        view().showLoadingDialog();
        RemoveFriend.Param param = new RemoveFriend.Param(DataPolicy.API, friend.getId());
        mRemoveFriendInteractor.execute(param,
                () -> {
                    view().successDeleteFriend(friend);
                    getFriends();
                },
                err -> processFriendError(friend, err));
    }

    @Override
    public void acceptFriend(Friend friend) {
        view().showLoadingDialog();
        AcceptFriend.Param param = new AcceptFriend.Param(DataPolicy.API, friend.getId());
        mAcceptFriendInteractor.execute(param,
                () -> {
                    view().successAcceptFriend(friend);
                    getFriends();
                },
                err -> processFriendError(friend, err));
    }

    @Override
    public void declineFriend(Friend friend) {
        view().showLoadingDialog();
        DeclineFriend.Param param = new DeclineFriend.Param(DataPolicy.API, friend.getId());
        mDeclineFriendInteractor.execute(param,
                () -> {
                    view().successDeclineFriend(friend);
                    getFriends();
                },
                err -> processFriendError(friend, err));
    }

    private void processFriendError(Friend friend, Throwable err) {
        view().errorFriend(friend);
        Timber.e(err);
    }

    @Override
    public void getFriends() {
        mGetFriendsInteractor.execute(DataPolicy.API,
                this::processGetFriendsOnNext,
                this::processOnError,
                () -> {},
                (d) -> view().showLoading());

    }

    private void processGetFriendsOnNext(List<Friend> friends) {
        if (friends == null || friends.isEmpty()) {
            view().showEmptyList();
        } else {
            Collections.sort(friends, (o1, o2) -> {
                if (o1.isAccepted() == o2.isAccepted()) {
                    return (int) (o1.getId() - o2.getId());
                } else if (o1.isAccepted()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            view().addFriendsToRecycler(friends);
            view().showContent();
        }
    }

    private void processOnError(Throwable err) {
        Timber.e(err);
        if (err instanceof NoConnectivityException) {
            view().showNetworkError();
        } else {
            view().showServerError();
        }
    }

    @SuppressWarnings("unchecked")
    private <S> ObservableTransformer<S, S> applySchedulers() {
        return (ObservableTransformer<S, S>) mObservableTransformer;
    }
}
