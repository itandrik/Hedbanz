package com.transcendensoft.hedbanz.presentation.menu

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager
import com.transcendensoft.hedbanz.domain.entity.User
import com.transcendensoft.hedbanz.domain.interactor.firebase.FirebaseBindTokenInteractor
import com.transcendensoft.hedbanz.domain.interactor.firebase.FirebaseUnbindTokenInteractor
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
 * Implementation of main menu fragment presenter.
 * Here are work with firebase tokens and so on.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class MenuFragmentPresenter @Inject constructor(
        private val mFirebaseBindTokenInteractor: FirebaseBindTokenInteractor,
        private val mFirebaseUnbindTokenInteractor: FirebaseUnbindTokenInteractor,
        private val mPreferenceManager: PreferenceManager
) : BasePresenter<User, MenuFragment>(), MenuFragmentContract.Presenter {

    override fun updateView() {
        if (model != null && model.login.isNullOrEmpty()) {
            model = mPreferenceManager.user
        }

        bindFirebaseToken()
    }

    override fun destroy() {
        mFirebaseBindTokenInteractor.dispose()
        mFirebaseUnbindTokenInteractor.dispose()
    }

    override fun bindFirebaseToken() {
        val token = mPreferenceManager.firebaseToken
        val firebaseTokenBinded = mPreferenceManager.firebaseTokenBinded

        if (!firebaseTokenBinded && !token.isNullOrEmpty()) {
            mFirebaseBindTokenInteractor.execute(token,
                    { mPreferenceManager.firebaseTokenBinded = true },
                    { mPreferenceManager.firebaseTokenBinded = false })
        }
    }

    override fun unbindFirebaseToken() {
        val firebaseTokenBinded = mPreferenceManager.firebaseTokenBinded

        if(firebaseTokenBinded){
            view()?.showLoadingDialog()
            mFirebaseUnbindTokenInteractor.execute(null,
                    {view()?.showLogoutSuccess()},
                    this::processCreateRoomOnError)
        } else {
            view()?.showLogoutSuccess()
        }
    }

    private fun processCreateRoomOnError(err: Throwable) {
        if (err is ConnectException) {
            view()?.showLogoutNetworkError()
        } else {
            Timber.e(err)
            view()?.showLogoutServerError()
        }
    }

}