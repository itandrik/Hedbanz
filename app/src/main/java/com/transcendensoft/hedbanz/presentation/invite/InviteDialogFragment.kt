package com.transcendensoft.hedbanz.presentation.invite

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.Friend
import com.transcendensoft.hedbanz.domain.entity.Room
import com.transcendensoft.hedbanz.presentation.game.GameActivity
import com.transcendensoft.hedbanz.presentation.invite.list.InviteAdapter
import com.transcendensoft.hedbanz.utils.AndroidUtils
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
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
 * Dialog fragment class that shows list of uninvited friends
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class InviteDialogFragment @Inject constructor() : DialogFragment(),
        HasSupportFragmentInjector, InviteContract.View {
    @Inject lateinit var mPresenter: InvitePresenter
    @Inject lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var mAdapter: InviteAdapter
    @Inject lateinit var mActivity: GameActivity

    private lateinit var mProgressDialog: ProgressDialog
    lateinit var mRoom: Room

    @BindView(R.id.rvInviteFriends) lateinit var mRecycler: RecyclerView
    @BindView(R.id.rlEmptyListContainer) lateinit var mRlEmptyListContainer: RelativeLayout
    @BindView(R.id.rlErrorNetwork) lateinit var mRlErrorNetwork: ConstraintLayout
    @BindView(R.id.rlErrorServer) lateinit var mRlErrorServer: ConstraintLayout
    @BindView(R.id.pbLoading) lateinit var mPbLoading: ProgressBar
    @BindView(R.id.btnInvite) lateinit var mBtnInvite: Button

    /*------------------------------------*
     *-------- Fragment lifecycle --------*
     *------------------------------------*/
    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_invite, container, false)

        if (dialog != null && dialog.window != null) {
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        }

        ButterKnife.bind(this, view)

        initProgressDialog()
        initPresenter()
        initRecycler()

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
                    (displayRectangle.height() * 0.5f).toInt())
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

    /*------------------------------------*
     *---------- Initialization ----------*
     *------------------------------------*/
    private fun initProgressDialog() {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage(getString(R.string.action_loading))
        mProgressDialog.setCancelable(false)
        mProgressDialog.isIndeterminate = true
    }

    private fun initRecycler(){
        mRecycler.itemAnimator = DefaultItemAnimator()
        mRecycler.layoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false)
        mRecycler.adapter = mAdapter
    }

    private fun initPresenter(){
        mPresenter.room = this.mRoom
        mPresenter.setModel(listOf())
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
            fragmentDispatchingAndroidInjector

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.btnInvite)
    fun onInviteClicked(){
        mPresenter.inviteSelectedUsers(mAdapter.getSelectedFriends())
    }

    @OnClick(R.id.ivClose)
    fun onCloseClicked(){
        dismiss()
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    override fun setFriends(friends: List<Friend>) {
        mAdapter.items = friends.toMutableList()
    }

    override fun setRoom(room: Room) {
        mRoom = room
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    override fun showLoading() {
        hideAll()
        mPbLoading.visibility = View.VISIBLE
    }

    override fun showContent() {
        hideAll()
        mRecycler.visibility = View.VISIBLE
        mBtnInvite.visibility = View.VISIBLE
    }

    override fun showServerError() {
        hideAll()
        mRlErrorServer.visibility = View.VISIBLE
    }

    override fun showNetworkError() {
        hideAll()
        mRlErrorNetwork.visibility = View.VISIBLE
    }

    override fun showNoUsersSelected() {
        AndroidUtils.showLongToast(mActivity, R.string.invite_no_users_selected)
    }

    override fun showEmptyFriendsList() {
        hideAll()
        mRlEmptyListContainer.visibility = View.VISIBLE
    }

    override fun showLoadingDialog() {
        mProgressDialog.show()
    }

    override fun hideLoadingDialog() {
        mProgressDialog.hide()
    }

    override fun hideAll() {
        hideLoadingDialog()
        mRecycler.visibility = View.GONE
        mBtnInvite.visibility = View.GONE
        mRlEmptyListContainer.visibility = View.GONE
        mRlErrorNetwork.visibility = View.GONE
        mRlErrorServer.visibility = View.GONE
        mPbLoading.visibility = View.GONE
    }

    override fun showInviteSuccess() {
        hideLoadingDialog()
        AndroidUtils.showShortToast(mActivity, "Успешно пригласили друзей в комнату")
    }

    override fun showInviteError() {
        hideLoadingDialog()
        AndroidUtils.showShortToast(mActivity, "Не удалось пригласить друзей в комнату")
    }
}