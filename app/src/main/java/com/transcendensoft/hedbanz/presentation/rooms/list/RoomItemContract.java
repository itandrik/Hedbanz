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
import android.support.annotation.DrawableRes;

/**
 * View and Presenter interfaces contract for room item presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface RoomItemContract {

    interface View {
        void setIcon(@DrawableRes int icon);

        void setName(String name);

        void setCurAndMaxPlayers(int currentPlayers, int maxPlayers);

        void setIsProtected(boolean isProtected);

        void showLoadingItem();

        void showCard();

        void showErrorServer();

        void showErrorNetwork();

        View getItemView();

        Context provideContext();
    }

    interface Presenter {
        void onClickRoom();
    }
}
