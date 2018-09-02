package com.transcendensoft.hedbanz.presentation.userdetails

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.User
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

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
    @Inject lateinit var mPresenter: UserDetailsPresenter
    @Inject lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @BindView(R.id.btnAddFriend) lateinit var btnAddFriend: Button
    @BindView(R.id.ivUserIcon) lateinit var ivUserIcon: ImageView
    @BindView(R.id.tvUserLogin) lateinit var tvUserLogin: TextView
    @BindView(R.id.ivFriendship) lateinit var ivFriendship: ImageView
    @BindView(R.id.tvFriendship) lateinit var tvFriendship: TextView

    var user: User? = null
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
        mProgressDialog.isIndeterminate = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_details, container, false)

        if (dialog != null && dialog.window != null) {
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        }

        ButterKnife.bind(this, view)
        initUserView()

        return view
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            // retrieve display dimensions
            val displayRectangle = Rect()
            val window = dialog.window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)

            dialog.window.setLayout((displayRectangle.width() * 0.8f).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.bindView(this)

    }

    override fun onPause() {
        super.onPause()
        mPresenter.unbindView()

    }

    private fun initUserView() {
        user?.let {
            mPresenter.setModel(it)
            ivUserIcon.setImageResource(it.iconId.resId)
            tvUserLogin.text = it.login
            if (it.isFriend) {
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

    @OnClick(R.id.btnClose)
    fun onCloseClick() {
        dismiss()
    }

    @OnClick(R.id.btnAddFriend)
    fun onAddFriendClick() {
        mPresenter.addFriend()
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
        mPresenter.destroy()
    }
}