package com.transcendensoft.hedbanz.presentation.userdetails

import com.transcendensoft.hedbanz.data.exception.HedbanzApiException
import com.transcendensoft.hedbanz.data.source.DataPolicy
import com.transcendensoft.hedbanz.domain.entity.User
import com.transcendensoft.hedbanz.domain.interactor.friends.AddFriend
import com.transcendensoft.hedbanz.presentation.base.BasePresenter
import timber.log.Timber
import java.net.ConnectException
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
        val mAddFriendInteractor: AddFriend
) : BasePresenter<User, UserDetailsContract.View>(),
        UserDetailsContract.Presenter {

    override fun updateView() {
        // Stub
    }

    override fun destroy() {
        mAddFriendInteractor.dispose()
    }

    override fun addFriend() {
        model?.let {
            val addFriendParam = AddFriend.Param(DataPolicy.API, model.id)

            mAddFriendInteractor.execute(addFriendParam,
                    view()::showAddFriendSuccess,
                    this::processAddFriendError)
        }
    }

    private fun processAddFriendError(err: Throwable?) {
        when (err) {
            is ConnectException -> view().showInternetError()
            is HedbanzApiException -> Timber.e(
                    "Code: ${err.serverErrorCode}." +
                            " Message: ${err.serverErrorMessage}")
            else -> view().showServerError()
        }
    }

}