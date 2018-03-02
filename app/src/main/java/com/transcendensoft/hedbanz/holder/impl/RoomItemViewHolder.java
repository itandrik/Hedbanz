package com.transcendensoft.hedbanz.holder.impl;
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

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.transcendensoft.hedbanz.holder.MvpViewHolder;
import com.transcendensoft.hedbanz.holder.RoomItemView;
import com.transcendensoft.hedbanz.presenter.impl.vhpresener.RoomItemPresenterImpl;

/**
 * View holder realization for concrete room.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class RoomItemViewHolder extends MvpViewHolder<RoomItemPresenterImpl> implements RoomItemView {
    private TextView mTvName;
    private ImageView mIvIcon;
    private ImageView mIvProtected;
    private TextView mTvCurAndMaxPlayers;

    public RoomItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setIcon(int icon) {

    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void setCurAndMaxPlayers(int currentPlayers, int maxPlayers) {

    }

    @Override
    public void setIsProtected(boolean isProtected) {

    }
}
