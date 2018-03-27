package com.transcendensoft.hedbanz.presentation.rooms.list;
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

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.presentation.base.MvpRecyclerListAdapter;
import com.transcendensoft.hedbanz.presentation.rooms.RoomsPresenter;

/**
 * Recycler view adapter, that shows all rooms.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RoomsAdapter extends MvpRecyclerListAdapter<Room,RoomItemPresenterImpl,RoomItemViewHolder> {
    private RoomsPresenter mCallbackPresenter;

    public RoomsAdapter(RoomsPresenter mCallbackPresenter) {
        this.mCallbackPresenter = mCallbackPresenter;
    }

    @NonNull
    @Override
    protected RoomItemPresenterImpl createPresenter(@NonNull Room model) {
        RoomItemPresenterImpl presenter = new RoomItemPresenterImpl();
        presenter.setModel(model);
        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull Room model) {
        return model.getId();
    }

    @Override
    public RoomItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomItemViewHolder(parent.getContext(), itemView, mCallbackPresenter);
    }
}
