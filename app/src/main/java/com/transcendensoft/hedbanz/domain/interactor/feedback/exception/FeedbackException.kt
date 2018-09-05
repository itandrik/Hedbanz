package com.transcendensoft.hedbanz.domain.interactor.feedback.exception

import com.transcendensoft.hedbanz.domain.validation.FeedbackError
import com.transcendensoft.hedbanz.domain.validation.PasswordResetError

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
 * Exceptions, that can be thrown from interactor in order to
 * show feedback error
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class FeedbackException(): RuntimeException() {
    val feedbackErrors: MutableList<FeedbackError> = mutableListOf()

    constructor(feedbackError: FeedbackError) : this(){
        feedbackErrors.add(feedbackError)
    }

    fun addError(feedbackError: FeedbackError) {
        feedbackErrors.add(feedbackError)
    }
}