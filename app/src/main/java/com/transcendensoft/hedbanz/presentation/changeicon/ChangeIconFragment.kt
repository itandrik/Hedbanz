package com.transcendensoft.hedbanz.presentation.changeicon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.appcompat.app.AlertDialog
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.User
import com.transcendensoft.hedbanz.presentation.base.BaseFragment
import com.transcendensoft.hedbanz.presentation.changeicon.list.ChangeIconAdapter
import com.transcendensoft.hedbanz.presentation.changeicon.list.SelectableIcon
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity
import com.transcendensoft.hedbanz.utils.extension.setupNavigationToolbar
import kotlinx.android.synthetic.main.fragment_change_user_icon.*
import kotlinx.android.synthetic.main.toolbar_main.tvToolbarTitle
import kotlinx.android.synthetic.main.toolbar_main.view.*
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
class ChangeIconFragment : BaseFragment(), ChangeIconContract.View {

    @Inject
    lateinit var presenter: ChangeIconPresenter
    @Inject
    lateinit var adapter: ChangeIconAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_change_user_icon, null, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.setModel(User())
        initRecycler()
        initToolbar()
        initClickListeners()
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
        val layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 3,
                androidx.recyclerview.widget.GridLayoutManager.VERTICAL, false)

        rvUserIcons.layoutManager = layoutManager
        rvUserIcons.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        rvUserIcons.adapter = adapter

        presenter.processIconSelected(adapter.clickSubject)
    }

    private fun initToolbar() {
        setupNavigationToolbar((requireActivity() as MainActivity).toolbar, getString(R.string.change_icon_choose_avatar))
    }

    private fun initClickListeners() {
        btnSaveUserIcon.setOnClickListener {
            presenter.updateUserIcon()
        }
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
        AlertDialog.Builder(requireContext())
                .setPositiveButton(getString(R.string.action_ok)) { dialog, _ -> dialog.dismiss() }
                .setIcon(d)
                .setTitle(getString(R.string.change_icon_success_title))
                .setMessage(getString(R.string.change_icon_success_message))
                .show()
    }

    override fun showErrorUpdateUserIcon() {
        val d = VectorDrawableCompat.create(resources, R.drawable.ic_unhappy, null)
        AlertDialog.Builder(requireContext())
                .setPositiveButton(getString(R.string.action_ok)) { dialog, _ -> dialog.dismiss() }
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