package com.transcendensoft.hedbanz.presentation.restorepwd

import android.os.Bundle
import androidx.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.presentation.base.BaseFragment
import javax.inject.Inject

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
 * Fragment that shows view to send email
 * in order to get secret key
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class ForgotPasswordFragment @Inject constructor() : BaseFragment() {
    lateinit var mPresenter: RestorePasswordContract.Presenter
    @Inject lateinit var mActivity: RestorePasswordActivity

    @BindView(R.id.ivSmileGif) lateinit var ivSmileGif: ImageView
    @BindView(R.id.etLogin) lateinit var etLogin: EditText
    @BindView(R.id.tvErrorLogin) lateinit var tvErrorLogin: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        ButterKnife.bind(this, view)

        return view
    }

    override fun onResume() {
        super.onResume()
        mPresenter.initAnimEditTextListener(etLogin)
    }

    override fun onPause() {
        stopSmileAnimation()
        super.onPause()
    }

    @OnClick(R.id.btnForgotPassword)
    fun onSubmitClicked() {
        mPresenter.forgotPassword(etLogin.text.toString())
    }

    @OnClick(R.id.ivBack)
    fun onBackClicked(){
        mActivity.onBackPressed()
    }

    fun showLoginError(@StringRes messageId: Int){
        hideAll()
        tvErrorLogin.visibility = View.VISIBLE
        tvErrorLogin.text = getString(messageId)
    }

    override fun showLoading() {
        // Stub
    }

    override fun showContent() {
        // Stub
    }

    override fun hideAll() {
        hideLoadingDialog()
        tvErrorLogin.visibility = View.GONE
    }

    fun startSmileAnimation() {
        mActivity.runOnUiThread { Glide.with(this)
                .asGif().load(R.raw.smile_gif_new)
                .into(ivSmileGif) }
    }

    fun stopSmileAnimation() {
        mActivity.runOnUiThread {
            Glide.with(this)
                    .load(R.drawable.logo_for_anim)
                    .into(ivSmileGif) }
    }
}
