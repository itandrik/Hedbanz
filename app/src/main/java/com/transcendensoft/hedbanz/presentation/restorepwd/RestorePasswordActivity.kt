package com.transcendensoft.hedbanz.presentation.restorepwd

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.view.WindowManager
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.presentation.base.BaseActivity
import com.transcendensoft.hedbanz.utils.KeyboardUtils
import timber.log.Timber
import javax.inject.Inject

class RestorePasswordActivity : BaseActivity(), RestorePasswordContract.View {
    @Inject lateinit var mPresenter: RestorePasswordPresenter
    @Inject lateinit var mForgotPasswordFragment: ForgotPasswordFragment
    @Inject lateinit var mCheckKeywordFragment: CheckKeywordFragment
    @Inject lateinit var mResetPasswordFragment: ResetPasswordFragment

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryLight)
        }

        setContentView(R.layout.activity_restore_password)
        initPresenterForFragments()
        goToForgotPasswordFragment()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.bindView(this)
    }

    override fun onPause() {
        super.onPause()
        mPresenter.unbindView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.destroy()
    }

    /*------------------------------------*
     *------------ Navigation ------------*
     *------------------------------------*/
    private fun initPresenterForFragments() {
        mForgotPasswordFragment.mPresenter = this.mPresenter
        mCheckKeywordFragment.mPresenter = this.mPresenter
        mResetPasswordFragment.mPresenter = this.mPresenter
    }

    override fun goToForgotPasswordFragment() {
        val tag = getString(R.string.tag_fragment_forgot_password)
        supportFragmentManager.beginTransaction()
                .replace(R.id.flRestorePasswordFragmentContainer,
                        mForgotPasswordFragment, tag)
                .commit()

    }

    override fun goToCheckKeywordFragment() {
        mForgotPasswordFragment.hideAll()
        val tag = getString(R.string.tag_fragment_check_keyword)
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.login_page_left_in, R.anim.login_page_left_out,
                        R.anim.login_page_right_in, R.anim.login_page_right_out)
                .replace(R.id.flRestorePasswordFragmentContainer,
                        mCheckKeywordFragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    override fun goToResetPasswordFragment() {
        mCheckKeywordFragment.hideAll()
        val tag = getString(R.string.tag_fragment_reset_password)
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.login_page_left_in, R.anim.login_page_left_out,
                        R.anim.login_page_right_in, R.anim.login_page_right_out)
                .replace(R.id.flRestorePasswordFragmentContainer,
                        mResetPasswordFragment, tag)
                .commit()
    }

    override fun showPasswordResendSuccessful() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.restore_pwd_keyword_resent_title))
                .setMessage(getString(R.string.restore_pwd_keyword_resent_message))
                .setIcon(R.drawable.ic_win_happy)
                .setPositiveButton(getString(R.string.action_ok)) { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .show()
    }

    override fun finishResetingPassword() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.restore_password_finish_title))
                .setMessage(getString(R.string.restore_password_finish_message))
                .setIcon(R.drawable.thumbs_up)
                .setPositiveButton(getString(R.string.action_ok)) { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .setOnCancelListener {
                    KeyboardUtils.hideSoftInput(this)
                    it.dismiss()
                    finish()
                }
                .setOnDismissListener {
                    KeyboardUtils.hideSoftInput(this)
                    it.dismiss()
                    finish()
                }
                .show()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    override fun showServerError() {
        super.showServerError()
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.menu_logout_error_title))
                .setMessage(getString(R.string.menu_logout_server_error))
                .setIcon(R.drawable.ic_dialog_server_error)
                .setPositiveButton(getString(R.string.action_ok)) { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .show()
    }

    override fun showNetworkError() {
        super.showNetworkError()
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.menu_logout_error_title))
                .setMessage(getString(R.string.menu_logout_network_error))
                .setIcon(R.drawable.ic_dialog_network_error)
                .setPositiveButton(getString(R.string.action_ok)) { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .show()
    }

    override fun showLoading() {
        // Stub
    }

    override fun showContent() {
        // Stub
    }

    override fun hideAll() {
        // Stub
    }

    override fun showLoginError(stringRes: Int) {
        mForgotPasswordFragment.hideAll()
        mForgotPasswordFragment.showLoginError(stringRes)
    }

    override fun showPasswordError(stringRes: Int) {
        mResetPasswordFragment.hideAll()
        mResetPasswordFragment.showPasswordError(stringRes)
    }

    override fun showPasswordConfirmationError(stringRes: Int) {
        mResetPasswordFragment.hideAll()
        mResetPasswordFragment.showConfirmPasswordError(stringRes)
    }

    override fun showKeywordError(stringRes: Int) {
        mCheckKeywordFragment.hideAll()
        mCheckKeywordFragment.showKeywordError(stringRes)
    }

    override fun showLocaleError(stringRes: Int) {
        Timber.e(getString(stringRes))
        showServerError()
    }

    override fun startSmileAnimation() {
        mForgotPasswordFragment.startSmileAnimation()
    }

    override fun stopSmileAnimation() {
        mForgotPasswordFragment.stopSmileAnimation()
    }
}
