package com.transcendensoft.hedbanz.presentation.invite

import com.transcendensoft.hedbanz.data.source.DataPolicy
import com.transcendensoft.hedbanz.domain.entity.Friend
import com.transcendensoft.hedbanz.domain.entity.Invite
import com.transcendensoft.hedbanz.domain.entity.Room
import com.transcendensoft.hedbanz.domain.interactor.friends.GetInviteFriends
import com.transcendensoft.hedbanz.domain.interactor.rooms.InviteToRoomInteractor
import com.transcendensoft.hedbanz.presentation.base.BasePresenter
import timber.log.Timber
import java.net.ConnectException
import javax.inject.Inject

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
 * Presenter from MVP pattern, that contains
 * methods show user details. Sending friend request
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class InvitePresenter @Inject constructor(
        private val getInviteFriendsInteractor: GetInviteFriends,
        private val inviteToRoomInteractor: InviteToRoomInteractor
) : BasePresenter<List<Friend>, InviteContract.View>(),
        InviteContract.Presenter {
    var room: Room? = null

    override fun updateView() {
        if (model.isEmpty()) {
            loadInvitingFriends()
        } else {
            view()?.setFriends(model)
        }
    }

    override fun destroy() {
        getInviteFriendsInteractor.dispose()
        inviteToRoomInteractor.dispose()
    }


    override fun loadInvitingFriends() {
        view()?.showLoading()
        getInviteFriendsInteractor.execute(DataPolicy.API,
                { friends ->
                    if (friends.isEmpty()) {
                        view()?.showEmptyFriendsList()
                    } else {
                        view()?.setFriends(friends)
                        view()?.showContent()
                    }
                }, this::processOnError)
    }

    private fun processOnError(err: Throwable) {
        Timber.e(err)
        if (err is ConnectException) {
            view()?.showNetworkError()
        } else {
            view()?.showServerError()
        }
    }

    override fun inviteSelectedUsers(selectedFriends: List<Friend>) {
        if (selectedFriends.isEmpty()) {
            view()?.showNoUsersSelected()
        } else {
            view()?.showLoadingDialog()
            val invite = Invite.Builder()
                    .invitedUserIds(selectedFriends.map { it.id })
                    .password(room?.password ?: "")
                    .roomId(room?.id ?: 0L)
                    .build()

            inviteToRoomInteractor.execute(
                    invite,
                    {},
                    { err ->
                        view()?.showInviteError()
                        Timber.e(err)
                    },
                    {
                        view()?.showInviteSuccess()
                        loadInvitingFriends()
                    })
        }
    }

}