package com.transcendensoft.hedbanz.presentation.changeicon

import com.transcendensoft.hedbanz.presentation.base.BaseView
import com.transcendensoft.hedbanz.presentation.changeicon.list.SelectableIcon
import io.reactivex.Observable

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
 * change user icon presentation
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
interface ChangeIconContract{
    interface View : BaseView {
        fun setImages(icons: List<SelectableIcon>)
        fun selectIconWithId(iconId: Int)
        fun showSuccessUpdateUserIcon()
        fun showErrorUpdateUserIcon()
    }

    interface Presenter {
        fun updateUserIcon()
        fun processIconSelected(clickObservable: Observable<Int>)
    }
}