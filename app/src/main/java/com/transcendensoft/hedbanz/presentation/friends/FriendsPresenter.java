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

import com.transcendensoft.hedbanz.domain.entity.Friend;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of friends presenter.
 * Getting and deleting friends from server and DB.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class FriendsPresenter extends BasePresenter<List<Friend>, FriendsContract.View>
        implements FriendsContract.Presenter{

    @Inject
    public FriendsPresenter() {
    }

    @Override
    protected void updateView() {
        if(model == null || model.isEmpty()){
            getFriends();
        } else {
            view().clearFriends();
            view().addFriendsToRecycler(model);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void deleteFriendWithId(long id) {

    }

    @Override
    public void getFriends() {

    }
}
