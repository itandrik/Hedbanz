package com.transcendensoft.hedbanz.presentation.feedback

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.google.android.gms.internal.zzahf.runOnUiThread
import com.google.firebase.analytics.FirebaseAnalytics
import com.transcendensoft.hedbanz.BuildConfig
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager
import com.transcendensoft.hedbanz.domain.entity.CONFIDENTIALITY_BUTTON
import com.transcendensoft.hedbanz.domain.entity.FEEDBACK_SUBMIT_BUTTON
import com.transcendensoft.hedbanz.domain.entity.TELEGRAM_BUTTON
import com.transcendensoft.hedbanz.presentation.base.BaseFragment
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity
import com.transcendensoft.hedbanz.utils.AndroidUtils
import com.transcendensoft.hedbanz.utils.extension.setupKeyboardHiding
import com.transcendensoft.hedbanz.utils.extension.setupNavigationToolbar
import kotlinx.android.synthetic.main.fragment_feedback.*
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
 * Activity that represents submitting some user feedback
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class FeedbackFragment : BaseFragment(), FeedbackContract.View {

    @Inject lateinit var presenter: FeedbackPresenter
    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_feedback, container, false);

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentLayout.setupKeyboardHiding(this)
        initClickListeners()
        initToolbar()
    }

    override fun onResume() {
        super.onResume()
        presenter.bindView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unbindView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    private fun initToolbar() {
        setupNavigationToolbar((requireActivity() as MainActivity).toolbar, getString(R.string.feedback_title))
    }

    private fun initClickListeners() {
        btnSubmitFeedback.setOnClickListener {
            hideError()
            presenter.submitFeedback(etFeedback.text.toString())
            firebaseAnalytics.logEvent(FEEDBACK_SUBMIT_BUTTON, null)
        }

        btnConfidentiality.setOnClickListener{
            val url = "${BuildConfig.HOST_LINK}${BuildConfig.PORT_API}" +
                    "privacy-policies?lang=${preferenceManager.locale}"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            firebaseAnalytics.logEvent(CONFIDENTIALITY_BUTTON, null)
        }

        btnTelegram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM_LINK)))
            firebaseAnalytics.logEvent(TELEGRAM_BUTTON, null)
        }
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    override fun showLoading() {
        // Stub
    }

    override fun showContent() {
        // Stub
    }

    override fun hideAll() {
        // Stub
    }

    override fun showNetworkError() {
        super.showNetworkError()
        AndroidUtils.showShortToast(requireContext(), R.string.error_network)
    }

    override fun showServerError() {
        super.showServerError()
        AndroidUtils.showShortToast(requireContext(), R.string.error_server)
    }

    override fun showFeedbackSuccess() {
        hideLoadingDialog()
        val d = VectorDrawableCompat.create(resources, R.drawable.ic_win_happy, null)
        AlertDialog.Builder(requireContext())
                .setPositiveButton(getString(R.string.action_ok)) { dialog, _ -> dialog.dismiss() }
                .setOnDismissListener { etFeedback.setText("") }
                .setIcon(d)
                .setTitle(getString(R.string.feedback_success_title))
                .setMessage(getString(R.string.feedback_success_message))
                .show()
    }

    override fun showFeedbackError(message: Int) {
        tvErrorFeedback.text = getString(message)
        tvErrorFeedback.visibility = View.VISIBLE
    }

    override fun hideError() {
        tvErrorFeedback.text = ""
        tvErrorFeedback.visibility = View.GONE
    }

    override fun showSubmitError() {
        AndroidUtils.showLongToast(requireContext(), getString(R.string.error_server))
    }

    companion object {
        val TELEGRAM_LINK = "https://t.me/joinchat/DxRXGlBRkYwwxCbkX1UiBg"
    }
}