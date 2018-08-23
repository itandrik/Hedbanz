package com.transcendensoft.hedbanz.presentation.invite

import com.transcendensoft.hedbanz.domain.entity.Friend
import com.transcendensoft.hedbanz.domain.entity.Room

/**
 * Copyright 2018. Andrii Chernysh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * View and Presenter interfaces contract for
 * friend invitation presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
interface InviteContract {
    interface View {
        fun showLoading()
        fun showContent()
        fun hideAll()
        fun showServerError()
        fun showNetworkError()
        fun setFriends(friends: List<Friend>)
        fun showNoUsersSelected()
        fun showEmptyFriendsList()
        fun showLoadingDialog()
        fun hideLoadingDialog()
        fun showInviteSuccess()
        fun showInviteError()
        fun setRoom(room: Room)
    }

    interface Presenter {
        fun loadInvitingFriends()
        fun inviteSelectedUsers(selectedFriends : List<Friend>)
    }
}