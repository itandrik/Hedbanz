package com.transcendensoft.hedbanz.presentation.feedback

import android.os.Build
import android.text.TextUtils
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import com.transcendensoft.hedbanz.data.network.retrofit.NoConnectivityException
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager
import com.transcendensoft.hedbanz.domain.entity.Feedback
import com.transcendensoft.hedbanz.domain.entity.User
import com.transcendensoft.hedbanz.domain.interactor.feedback.FeedbackInteractor
import com.transcendensoft.hedbanz.domain.interactor.feedback.exception.FeedbackException
import com.transcendensoft.hedbanz.domain.interactor.user.UpdateUserInteractor
import com.transcendensoft.hedbanz.domain.validation.FeedbackError
import com.transcendensoft.hedbanz.presentation.base.BasePresenter
import com.transcendensoft.hedbanz.presentation.changeicon.ChangeIconContract
import com.transcendensoft.hedbanz.utils.RxUtils
import timber.log.Timber
import java.net.ConnectException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

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
 *  Implementation of feedback presenter.
 * Here are work with server to submit user feedback
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class FeedbackPresenter @Inject constructor(
        private val feedbackInteractor: FeedbackInteractor,
        private val preferenceManager: PreferenceManager
) : BasePresenter<User, FeedbackContract.View>(), FeedbackContract.Presenter {
    override fun updateView() {
        // Stub
    }

    override fun destroy() {
        feedbackInteractor.dispose()
    }

    override fun submitFeedback(feedbackText: String) {
        view()?.showLoadingDialog()
        val feedback = Feedback(
                feedbackText = feedbackText,
                user = preferenceManager.user,
                deviceVersion = Build.VERSION.SDK_INT,
                deviceName = Build.DEVICE,
                deviceModel = Build.MODEL,
                deviceManufacturer = Build.MANUFACTURER,
                product = Build.PRODUCT
        )
        feedbackInteractor.execute(feedback,
                { view()?.showFeedbackSuccess() },
                { processFeedbackError(it) })
    }

    private fun processFeedbackError(err: Throwable) {
        view()?.hideLoadingDialog()
        Timber.e(err)
        when (err) {
            is NoConnectivityException -> view()?.showNetworkError()
            is FeedbackException -> processFeedbackException(err)
            else -> view()?.showServerError()
        }
    }

    private fun processFeedbackException(feedbackException: FeedbackException) {
        for (error in feedbackException.feedbackErrors) {
            if (error == FeedbackError.EMPTY_FEEDBACK) {
                view()?.showFeedbackError(error.messageId)
            } else {
                view()?.showServerError()
            }
        }
    }
}