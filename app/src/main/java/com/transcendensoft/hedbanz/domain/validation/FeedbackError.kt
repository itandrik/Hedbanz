package com.transcendensoft.hedbanz.domain.validation

import android.support.annotation.StringRes
import com.transcendensoft.hedbanz.R

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
 * Enum class that describes all errors that user
 * can receive from server while submitting feedback
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
enum class FeedbackError(val id: Int, @StringRes val messageId: Int) {
    EMPTY_FEEDBACK(1, R.string.login_error_empty_field),
    UNDEFINED_ERROR(100501, R.string.error_undefined_error);

    companion object {
        fun getErrorByCode(code: Int): FeedbackError {
            for (feedbackError in FeedbackError.values()) {
                if (feedbackError.id == code) {
                    return feedbackError
                }
            }
            return UNDEFINED_ERROR
        }
    }
}