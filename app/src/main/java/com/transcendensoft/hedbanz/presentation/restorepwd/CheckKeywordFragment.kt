package com.transcendensoft.hedbanz.presentation.restorepwd

import android.os.Bundle
import android.support.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.infideap.blockedittext.BlockEditText
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.presentation.base.BaseFragment
import com.transcendensoft.hedbanz.utils.KeyboardUtils
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
 * Fragment that shows view to check keyword,
 * that was sent to user email
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class CheckKeywordFragment @Inject constructor(): BaseFragment() {
    lateinit var mPresenter: RestorePasswordContract.Presenter
    @BindView(R.id.betKeyword) lateinit var betKeyword: BlockEditText
    @BindView(R.id.tvErrorKeyword) lateinit var tvErrorKeyword: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_check_keyword, container, false)

        ButterKnife.bind(this, view)
        initBlockEditText()

        return view
    }

    private fun initBlockEditText(){
        betKeyword.requestFocus()
        betKeyword.setTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(betKeyword.text?.length == NUMBER_OF_BLOCKS){
                    mPresenter.checkKeyword(betKeyword.text ?: "")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    @OnClick(R.id.btnSubmit)
    fun onSubmitClicked() {
        mPresenter.checkKeyword(betKeyword.text)
        KeyboardUtils.hideSoftInput(activity)
    }

    @OnClick(R.id.tvResendPassword)
    fun onResendPasswordClicked() {
        mPresenter.resendKeyword()
    }

    fun showKeywordError(@StringRes messageId: Int){
        hideAll()
        tvErrorKeyword.visibility = View.VISIBLE
        tvErrorKeyword.text = getString(messageId)
    }

    override fun showLoading() {
        // Stub
    }

    override fun showContent() {
        // Stub
    }

    override fun hideAll() {
        hideLoadingDialog()
        tvErrorKeyword.visibility = View.GONE
    }

    companion object {
        private const val NUMBER_OF_BLOCKS = 5
    }
}