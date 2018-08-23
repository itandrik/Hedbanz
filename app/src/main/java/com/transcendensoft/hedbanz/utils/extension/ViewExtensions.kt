package com.transcendensoft.hedbanz.utils.extension

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.transcendensoft.hedbanz.R

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
 * Kotlin extension functions for views
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
fun View.setupKeyboardHiding(activity: Activity) {

    // Set up touch listener for non-text box views to hide keyboard.
    if (this !is EditText && this.id != R.id.ivEmoji && this.id != R.id.ivSend) {
        this.setOnTouchListener { _, _ ->
            com.transcendensoft.hedbanz.utils.KeyboardUtils.hideSoftInput(activity)
            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            val innerView = this.getChildAt(i)
            innerView.setupKeyboardHiding(activity)
        }
    }
}