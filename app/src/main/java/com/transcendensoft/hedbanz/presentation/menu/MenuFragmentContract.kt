package com.transcendensoft.hedbanz.presentation.menu

import com.transcendensoft.hedbanz.presentation.base.BaseView

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
 * View and Presenter interfaces contract for main menu presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
interface MenuFragmentContract {
    interface View : BaseView {
        fun showLogoutServerError()
        fun showLogoutNetworkError()
        fun showLogoutSuccess()
    }

    interface Presenter {
        fun bindFirebaseToken()
        fun unbindFirebaseToken()
    }
}