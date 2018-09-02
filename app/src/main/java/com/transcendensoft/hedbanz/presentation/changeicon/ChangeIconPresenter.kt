package com.transcendensoft.hedbanz.presentation.changeicon

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager
import com.transcendensoft.hedbanz.domain.entity.User
import com.transcendensoft.hedbanz.domain.entity.UserIcon
import com.transcendensoft.hedbanz.domain.interactor.user.UpdateUserInteractor
import com.transcendensoft.hedbanz.presentation.base.BasePresenter
import com.transcendensoft.hedbanz.presentation.changeicon.list.SelectableIcon
import io.reactivex.Observable
import timber.log.Timber
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
 * Implementation of change icon presenter.
 * Here are work with server to updating user info
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class ChangeIconPresenter @Inject constructor(
        private val updateUserInteractor: UpdateUserInteractor,
        private val preferenceManager: PreferenceManager
) : BasePresenter<User, ChangeIconContract.View>(), ChangeIconContract.Presenter {
    lateinit var selectedUserIcon: UserIcon

    override fun updateView() {
        selectedUserIcon = preferenceManager.user.iconId

        val icons = UserIcon.values().map {
            val isSelected = selectedUserIcon.id == it.id
            SelectableIcon(isSelected, it.id, it.resId)
        }
        view()?.setImages(icons)
    }

    override fun destroy() {
        updateUserInteractor.dispose()
    }

    override fun processIconSelected(clickObservable: Observable<Int>) {
        addDisposable(
                clickObservable.subscribe(
                        {
                            selectedUserIcon = UserIcon.getUserIconById(it)
                            view()?.selectIconWithId(it)
                        },
                        {
                            Timber.e("Error while select icon." +
                                    " It is not related to server. Message : ${it.message}")
                        })
        )
    }

    override fun updateUserIcon() {
        val currentUser = preferenceManager.user
        currentUser.iconId = selectedUserIcon

        val params: UpdateUserInteractor.Params = UpdateUserInteractor.Params()
                .setUpdateOldPassword(false)
                .setUser(currentUser)

        view()?.showLoadingDialog()
        updateUserInteractor.execute(params,
                {
                    preferenceManager.user = currentUser
                    view()?.showSuccessUpdateUserIcon()
                    view()?.hideLoadingDialog()
                },
                {
                    view()?.showErrorUpdateUserIcon()
                    view()?.hideLoadingDialog()
                })
    }
}