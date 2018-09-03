package com.transcendensoft.hedbanz.presentation.feedback

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.presentation.base.BaseActivity
import com.transcendensoft.hedbanz.utils.AndroidUtils
import javax.inject.Inject
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.widget.ScrollView
import com.transcendensoft.hedbanz.BuildConfig
import com.transcendensoft.hedbanz.utils.extension.setupKeyboardHiding


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
class FeedbackActivity : BaseActivity(), FeedbackContract.View {
    @BindView(R.id.ivSmileGif)
    lateinit var ivSmileGif: ImageView
    @BindView(R.id.etFeedback)
    lateinit var etFeedback: EditText
    @BindView(R.id.tvErrorFeedback)
    lateinit var tvFeedbackError: TextView
    @BindView(R.id.parentLayout)
    lateinit var parentLayout: ScrollView

    @Inject
    lateinit var presenter: FeedbackPresenter

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryLight)
        }

        setContentView(R.layout.activity_feedback)
        ButterKnife.bind(this, this)
        parentLayout.setupKeyboardHiding(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.bindView(this)
        presenter.initAnimEditTextListener(etFeedback)
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
    fun onSubmitFeedbackClicked() {
        hideError()
        presenter.submitFeedback(etFeedback.text.toString())
    }

    @OnClick(R.id.btnConfidentiality)
    fun onConfidentialityClicked() {
        startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("${BuildConfig.HOST_LINK}/privacy-policies")))
    }

    @OnClick(R.id.btnTelegram)
    fun onTelegramClicked() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM_LINK)))
    }

    @OnClick(R.id.ivBack)
    fun onBackClicked() {
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

    override fun showServerError() {
        super.showServerError()
        AndroidUtils.showShortToast(this, R.string.error_server)
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

    override fun startSmileAnimation() {
        runOnUiThread { Glide.with(this).asGif().load(R.raw.smile_gif_new).into(ivSmileGif) }
    }

    override fun stopSmileAnimation() {
        runOnUiThread {
            Glide.with(this).load(R.drawable.logo_for_anim).into(ivSmileGif)
        }
    }

    companion object {
        val TELEGRAM_LINK = "https://t.me/joinchat/DxRXGlBRkYwwxCbkX1UiBg"
    }
}