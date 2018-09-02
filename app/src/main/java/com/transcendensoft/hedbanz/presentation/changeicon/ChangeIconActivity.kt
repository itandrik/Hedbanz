package com.transcendensoft.hedbanz.presentation.changeicon

import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.User
import com.transcendensoft.hedbanz.presentation.base.BaseActivity
import com.transcendensoft.hedbanz.presentation.changeicon.list.ChangeIconAdapter
import com.transcendensoft.hedbanz.presentation.changeicon.list.SelectableIcon
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
 * Activity that represents changing icon for some user.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class ChangeIconActivity : BaseActivity(), ChangeIconContract.View {
    @BindView(R.id.rvUserIcons) lateinit var rvUserIcons: RecyclerView
    @BindView(R.id.tvToolbarTitle) lateinit var tvToolbarTitle: TextView

    @Inject lateinit var presenter: ChangeIconPresenter
    @Inject lateinit var adapter: ChangeIconAdapter

    /*------------------------------------*
     *-------- Activity lifecycle --------*
     *------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_change_user_icon)
        ButterKnife.bind(this, this)

        presenter.setModel(User())
        initRecycler()
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
     *---------- Initialization ----------*
     *------------------------------------*/
    private fun initRecycler() {
        val layoutManager = GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false)
        rvUserIcons.layoutManager = layoutManager
        rvUserIcons.itemAnimator = DefaultItemAnimator()
        rvUserIcons.adapter = adapter

        presenter.processIconSelected(adapter.clickSubject)
    }

    private fun initToolbar() {
        tvToolbarTitle.text = getString(R.string.change_icon_choose_avatar)
    }

    /*------------------------------------*
     *-------- On click listeners --------*
     *------------------------------------*/
    @OnClick(R.id.btnSaveUserIcon)
    fun saveIconClicked() {
        presenter.updateUserIcon()
    }

    @OnClick(R.id.ivBack)
    fun onBackClicked(){
        onBackPressed()
    }

    /*------------------------------------*
     *--------- Set data to view ---------*
     *------------------------------------*/
    override fun setImages(icons: List<SelectableIcon>) {
        adapter.items = icons.toMutableList()
    }

    override fun selectIconWithId(iconId: Int) {
        adapter.items.map {
            it.isSelected = it.iconId == iconId
            it
        }
        adapter.notifyDataSetChanged()
    }

    /*------------------------------------*
     *-------- Error and loading ---------*
     *------------------------------------*/
    override fun showSuccessUpdateUserIcon() {
        val d = VectorDrawableCompat.create(resources, R.drawable.ic_win_happy, null)
        AlertDialog.Builder(this)
                .setPositiveButton(getString(R.string.action_ok)) { dialog, which -> dialog.dismiss() }
                .setIcon(d)
                .setTitle(getString(R.string.change_icon_success_title))
                .setMessage(getString(R.string.change_icon_success_message))
                .show()
    }

    override fun showErrorUpdateUserIcon() {
        val d = VectorDrawableCompat.create(resources, R.drawable.ic_unhappy, null)
        AlertDialog.Builder(this)
                .setPositiveButton(getString(R.string.action_ok)) { dialog, which -> dialog.dismiss() }
                .setIcon(d)
                .setTitle(getString(R.string.change_icon_error_title))
                .setMessage(getString(R.string.change_icon_error_message))
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
}