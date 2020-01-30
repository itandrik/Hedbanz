package com.transcendensoft.hedbanz.presentation.restorepwd

import android.os.Bundle
import androidx.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
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
 * Fragment that shows view to reset password.
 * It contains 2 fields: password and password confirmation.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class ResetPasswordFragment @Inject constructor() : BaseFragment() {
    lateinit var mPresenter: RestorePasswordContract.Presenter
    @BindView(R.id.etPassword) lateinit var etPassword: EditText
    @BindView(R.id.tvErrorPassword) lateinit var tvPasswordError: TextView
    @BindView(R.id.etConfirmPassword) lateinit var etConfirmPassword: EditText
    @BindView(R.id.tvErrorConfirmPassword) lateinit var tvConfirmPasswordError: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)

        ButterKnife.bind(this, view)

        return view
    }

    @OnClick(R.id.btnSubmit)
    fun onSubmitClicked() {
        mPresenter.resetPassword(
                etPassword.text.toString(),
                etConfirmPassword.text.toString())
    }

    fun showPasswordError(@StringRes messageId: Int) {
        tvPasswordError.visibility = View.VISIBLE
        tvPasswordError.text = getString(messageId)
    }

    fun showConfirmPasswordError(@StringRes messageId: Int) {
        tvConfirmPasswordError.visibility = View.VISIBLE
        tvConfirmPasswordError.text = getString(messageId)
    }

    override fun showLoading() {
        // Stub
    }

    override fun showContent() {
        // Stub
    }

    override fun hideAll() {
        hideLoadingDialog()
        tvPasswordError.visibility = View.GONE
        tvConfirmPasswordError.visibility = View.GONE
    }
}