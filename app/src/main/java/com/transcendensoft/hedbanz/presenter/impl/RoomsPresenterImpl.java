package com.transcendensoft.hedbanz.presenter.impl;
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

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.transcendensoft.hedbanz.adapter.MvpRecyclerAdapter;
import com.transcendensoft.hedbanz.holder.MvpViewHolder;
import com.transcendensoft.hedbanz.model.api.manager.RoomsCrudApiManager;
import com.transcendensoft.hedbanz.model.entity.Room;
import com.transcendensoft.hedbanz.model.entity.ServerResult;
import com.transcendensoft.hedbanz.model.entity.ServerStatus;
import com.transcendensoft.hedbanz.presenter.BasePresenter;
import com.transcendensoft.hedbanz.presenter.RoomsPresenter;
import com.transcendensoft.hedbanz.util.AndroidUtils;
import com.transcendensoft.hedbanz.view.RoomsView;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Presenter from MVP pattern, that contains
 * methods show all available rooms
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RoomsPresenterImpl extends BasePresenter<List<Room>, RoomsView>
        implements RoomsPresenter, MvpRecyclerAdapter.OnBottomReachedListener {
    private static final String TAG = RoomsPresenterImpl.class.getName();
    private int mCurrentPage = 0;

    @Override
    protected void updateView() {
        if (model.isEmpty()) {
            refreshRooms();
        }
    }

    @Override
    public void loadNextRooms() {
        Disposable disposable = RoomsCrudApiManager.getInstance()
                .getRooms(mCurrentPage)
                .subscribe(
                        this::processRoomsOnNext,
                        this::processRoomOnError,
                        () -> view().stopRefreshingBar(),
                        this::processRoomOnSubscribe);
        addDisposable(disposable);
    }

    private void processRoomOnError(Throwable err) {
        Log.e(TAG, "Error " + err.getMessage());
        Crashlytics.logException(err);
        view().showServerError();
    }

    private void processRoomOnSubscribe(Disposable d) {
        if (!d.isDisposed() && view().provideContext() != null) {
            if (AndroidUtils.isNetworkConnected(view().provideContext())) {
                if (mCurrentPage == 0) {
                    view().showLoading();
                }
            } else {
                //TODO add paging error
                view().showNetworkError();
            }
        }
    }

    private void processRoomsOnNext(ServerResult<List<Room>> result) {
        if (result == null) {
            throw new RuntimeException("Server result is null");
        } else if ((result.getStatus() != null) &&
                result.getStatus().equalsIgnoreCase(ServerStatus.SUCCESS.toString())) {
            if (result.getData() == null || result.getData().isEmpty()) {
                if (mCurrentPage == 0) {
                    view().showEmptyList();
                } else {
                    view().removeLastRoom();
                }
            } else {
                if (mCurrentPage != 0) {
                    model.remove(model.size() - 1);
                    view().removeLastRoom();
                }

                List<Room> rooms = result.getData();
                rooms.add(new Room.Builder().setId(-1).build()); //ProgressBar view

                view().addRoomsToRecycler(result.getData());
                view().showContent();
                model.addAll(result.getData());
            }
        }
    }

    @Override
    public void refreshRooms() {
        mCurrentPage = 0;
        view().clearRooms();
        loadNextRooms();
    }

    @Override
    public void onBottomReached(MvpViewHolder holder) {
        mCurrentPage++;
        loadNextRooms();
    }
}
