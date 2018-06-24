package com.transcendensoft.hedbanz.presentation.userdetails

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.User
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_user_details.*
import javax.inject.Inject
import kotlin.reflect.KProperty

/**
 * Copyright 2017. Andrii Chernysh
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
 * View that shows user details. Made as dialog fragment.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class UserDetailsDialogFragment @Inject constructor() : DialogFragment(),
        HasSupportFragmentInjector, UserDetailsContract.View {
    @Inject
    lateinit var mPresenter: UserDetailsPresenter
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    var user: User? by UserDelegate()
    lateinit var mProgressDialog: ProgressDialog

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
            fragmentDispatchingAndroidInjector

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        initProgressDialog()
    }

    private fun initProgressDialog() {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage(getString(R.string.action_loading))
        mProgressDialog.setCancelable(false)
        mProgressDialog.setIndeterminate(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_details, container, false)

        if (dialog != null && dialog.window != null) {
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        }

        initCloseClick()
        initAddFriendClick()

        return view
    }

    private fun initCloseClick() {
        btnClose.setOnClickListener { dismiss() }
    }

    private fun initAddFriendClick() {
        btnAddFriend.setOnClickListener { mPresenter.addFriend() }
    }

    override fun showLoadingDialog() {
        mProgressDialog.show()
    }

    override fun hideLoadingDialog() {
        mProgressDialog.hide()
    }

    override fun showAddFriendSuccess() {
        hideLoadingDialog()
        btnAddFriend.text = getString(R.string.user_details_friend_request_sent)
        btnAddFriend.isEnabled = false
        btnAddFriend.setBackgroundResource(R.drawable.word_setted_background)
    }

    override fun showInternetError() {
        hideLoadingDialog()
        Toast.makeText(context, R.string.error_network, Toast.LENGTH_LONG).show()
    }

    override fun showServerError() {
        hideLoadingDialog()
        Toast.makeText(context, R.string.error_server, Toast.LENGTH_LONG).show()
    }

    override fun showAlreadySentRequest() {
        hideLoadingDialog()
        Toast.makeText(context, "Вы уже прислали запрос на дружбу", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mProgressDialog.hide()
    }

    private inner class UserDelegate {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): User? {
            return user
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: User?) {
            value?.let {
                mPresenter.setModel(it)
                ivUserIcon.setImageResource(R.drawable.logo)
                tvUserLogin.text = it.login
                if(it.isFriend){
                    tvFriendship.visibility = View.VISIBLE
                    ivFriendship.visibility = View.VISIBLE
                    btnAddFriend.visibility = View.INVISIBLE
                } else {
                    tvFriendship.visibility = View.GONE
                    ivFriendship.visibility = View.GONE
                    btnAddFriend.visibility = View.VISIBLE
                }
            }
        }
    }
}