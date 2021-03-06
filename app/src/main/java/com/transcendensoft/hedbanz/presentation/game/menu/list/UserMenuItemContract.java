package com.transcendensoft.hedbanz.presentation.game.menu.list;
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

import android.support.annotation.DrawableRes;

import com.transcendensoft.hedbanz.domain.entity.PlayerStatus;
import com.transcendensoft.hedbanz.domain.entity.User;

import io.reactivex.Observable;

/**
 * View and Presenter interfaces contract for room item presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public interface UserMenuItemContract {
    interface View {
        void setIcon(@DrawableRes int icon);

        void setName(String name);

        void setStatus(PlayerStatus playerStatus);

        void setIsWinner(boolean isWinner);

        void setWord(User user);

        void setWordVisible(User user);

        Observable<User> getClickObservable(User user);
    }

    interface Presenter {
    }
}
