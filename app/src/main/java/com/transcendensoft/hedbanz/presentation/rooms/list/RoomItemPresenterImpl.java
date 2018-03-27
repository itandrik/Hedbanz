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

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.game.GameActivity;

/**
 * Presenter from MVP pattern that processes concrete
 * room view item from list of rooms on main screen.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class RoomItemPresenterImpl extends BasePresenter<Room, RoomItemContract.View>
        implements RoomItemContract.Presenter{
    @Override
    protected void updateView() {
        if(model != null){
            if(model.getId()!= -1) {
                view().setCurAndMaxPlayers(model.getCurrentPlayersNumber(), model.getMaxPlayers());
                view().setName(model.getName());
                view().setIsProtected(!TextUtils.isEmpty(model.getPassword()));
                //TODO change icon of room
                view().setIcon(R.drawable.ic_room);
                view().showCard();
            } else {
                view().showLoadingItem();
            }
        }
    }

    @Override
    public void destroy() {
        // Stub
    }

    public void onClickRoom(){
        Context context = view().provideContext();
        if(context != null) {
            Intent intent = new Intent(context, GameActivity.class);
            intent.putExtra(context.getString(R.string.bundle_room_id), model.getId());
            context.startActivity(intent);
        }
    }
}
