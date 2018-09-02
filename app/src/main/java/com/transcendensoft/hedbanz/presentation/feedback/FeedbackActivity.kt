package com.transcendensoft.hedbanz.presentation.feedback

import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.presentation.base.BaseActivity
import com.transcendensoft.hedbanz.utils.AndroidUtils
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
class FeedbackActivity: BaseActivity(), FeedbackContract.View {
    @BindView(R.id.ivSmileGif) lateinit var ivSmileGif: ImageView
    @BindView(R.id.etFeedback) lateinit var etFeedback: EditText
    @BindView(R.id.tvErrorFeedback) lateinit var tvFeedbackError: TextView

    @Inject lateinit var presenter: FeedbackPresenter

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feedback)
        ButterKnife.bind(this, this)
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
    @OnClick(R.id.btnSubmitFeedback)
    fun onSubmitFeedbackClicked(){
        hideError()
        presenter.submitFeedback(etFeedback.text.toString())
    }

    @OnClick(R.id.btnConfidentiality)
    fun onConfidentialityClicked(){
        //TODO open confidentiality in browser
    }

    @OnClick(R.id.ivBack)
    fun onBackClicked(){
        onBackPressed()
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
        AndroidUtils.showShortToast(this, R.string.error_network)
    }

    override fun showFeedbackSuccess() {
        hideLoadingDialog()
        val d = VectorDrawableCompat.create(resources, R.drawable.ic_win_happy, null)
        AlertDialog.Builder(this)
                .setPositiveButton(getString(R.string.action_ok)) { dialog, which -> dialog.dismiss() }
                .setOnDismissListener { etFeedback.setText("") }
                .setIcon(d)
                .setTitle(getString(R.string.feedback_success_title))
                .setMessage(getString(R.string.feedback_success_message))
                .show()
    }

    override fun showFeedbackError(message: Int) {
        tvFeedbackError.text = getString(message)
        tvFeedbackError.visibility = View.VISIBLE
    }

    override fun hideError() {
        tvFeedbackError.text = ""
        tvFeedbackError.visibility = View.GONE
    }

    override fun showSubmitError() {
        AndroidUtils.showLongToast(this, getString(R.string.error_server))
    }
}