package com.transcendensoft.hedbanz.presentation.userdetails

import com.transcendensoft.hedbanz.data.exception.HedbanzApiException
import com.transcendensoft.hedbanz.data.network.retrofit.NoConnectivityException
import com.transcendensoft.hedbanz.data.source.DataPolicy
import com.transcendensoft.hedbanz.domain.entity.Friend
import com.transcendensoft.hedbanz.domain.interactor.friends.AddFriend
import com.transcendensoft.hedbanz.domain.interactor.friends.GetFriendForUser
import com.transcendensoft.hedbanz.presentation.base.BasePresenter
import timber.log.Timber
import javax.inject.Inject

/**
 * Copyright 2017. Andrii Chernysh
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
class UserDetailsPresenter @Inject constructor(
        val mAddFriendInteractor: AddFriend,
        val getFriendForUserInteractor: GetFriendForUser
) : BasePresenter<Friend, UserDetailsContract.View>(),
        UserDetailsContract.Presenter {

    override fun updateView() {
        if (model.login.isNullOrEmpty()) {
            getFriendInfo()
        } else {
            processFriendState()
        }
    }

    override fun destroy() {
        mAddFriendInteractor.dispose()
    }

    private fun getFriendInfo() {
        view()?.showLoadingDialog()
        getFriendForUserInteractor.execute(model.id,
                {
                    view()?.hideLoadingDialog()
                    model.isAccepted = it.isAccepted
                    model.isPending = it.isPending
                    processFriendState()
                },
                {
                    view()?.hideLoadingDialog()
                    Timber.e(it)
                })
    }

    private fun processFriendState() {
        if (!model.isAccepted && !model.isPending) {
            view()?.showIsFriend(false)
        } else if (model.isAccepted && !model.isPending) {
            view()?.showIsFriend(true)
        } else if (!model.isAccepted && model.isPending) {
            view()?.showSentFriendRequest()
        } else {
            view()?.showIsFriend(false)
        }
    }

    override fun addFriend() {
        model?.let {
            val addFriendParam = AddFriend.Param(DataPolicy.API, model.id)
            view()?.showLoadingDialog()
            mAddFriendInteractor.execute(addFriendParam,
                    view()::showAddFriendSuccess,
                    this::processAddFriendError)
        }
    }

    private fun processAddFriendError(err: Throwable?) {
        when (err) {
            is NoConnectivityException -> view().showInternetError()
            is HedbanzApiException -> Timber.e(
                    "Code: ${err.serverErrorCode}." +
                            " Message: ${err.serverErrorMessage}")
            else -> view().showServerError()
        }
    }
}