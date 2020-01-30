package com.transcendensoft.hedbanz.presentation.feedback

import androidx.annotation.StringRes
import android.widget.EditText
import com.transcendensoft.hedbanz.presentation.base.BaseView
import com.transcendensoft.hedbanz.presentation.changeicon.list.SelectableIcon
import io.reactivex.Observable

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
/**
 * View and Presenter interfaces contract for
 * feedback presentation
 *
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
interface FeedbackContract {
    interface View : BaseView {
        fun showFeedbackSuccess()
        fun showFeedbackError(@StringRes message: Int)
        fun showSubmitError()
        fun hideError()
        fun startSmileAnimation()
        fun stopSmileAnimation()
    }

    interface Presenter {
        fun submitFeedback(feedbackText: String)
        fun initAnimEditTextListener(editText: EditText)
    }
}