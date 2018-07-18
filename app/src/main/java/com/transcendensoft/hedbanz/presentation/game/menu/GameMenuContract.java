package com.transcendensoft.hedbanz.presentation.game.menu;
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

import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BaseView;
import com.transcendensoft.hedbanz.presentation.game.models.RxUser;

import java.util.List;

import io.reactivex.Observable;

/**
 * View and Presenter interfaces contract for game menu presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface GameMenuContract {
    interface View extends BaseView {
        void setRoomName(String roomName);
        void clearAndAddPlayers(List<RxUser> rxUsers);
        void addPlayer(RxUser rxUser);
        void removePlayer(RxUser rxUser);
        void setCurrentPlayersCount(int currentPlayersCount);
        void setMaxPlayersCount(int maxPlayersCount);
        void onPlayerClicked(User user);
    }

    interface Presenter {
        void processPlayerClickListener(Observable<User> playerClickObservable);
        Room getRoom();
    }
}
