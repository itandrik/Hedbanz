package com.transcendensoft.hedbanz.utils.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.transcendensoft.hedbanz.R
import kotlinx.android.synthetic.main.toolbar_main.view.*

/**
 * Created by Andrii Chernysh on 2019-09-23
 * If you have any questions, please write: andrii.chernysh@uptech.team
 */
fun Fragment.finish(@IdRes destinationId: Int? = null, inclusive: Boolean = false) {
  with((parentFragment as NavHostFragment).navController) {
    if (destinationId != null) {
      popBackStack(destinationId, inclusive)
    } else {
      popBackStack()
    }
  }
}

inline fun NavHostFragment.navigateTo(@IdRes destinationId: Int, block: Bundle.() -> Unit = {}) {
  navController.navigate(destinationId, Bundle().apply { block() })
}

inline fun <reified T : Activity> Fragment.openActivity(initializer: Intent.() -> Unit = {}) {
  startActivity(Intent(requireContext(), T::class.java).apply { initializer() })
}

inline fun <reified T : Activity> Fragment.openActivityForResult(requestCode: Int, options: Bundle? = null, block: Intent.() -> Unit = {}) {
  startActivityForResult(Intent(requireContext(), T::class.java).apply { block.invoke(this) }, requestCode, options)
}

fun Fragment.setupNavigationToolbar(toolbar: Toolbar, block: Toolbar.() -> Unit = {}) {
  val arrowBackDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_arrow_back)

  toolbar.apply {
    setupWithNavController(findNavController())
    navigationIcon = arrowBackDrawable

    groupGeneralToolbarWidgets.visibility = View.GONE
    groupSearchRoom.visibility = View.GONE
    ivAppIcon.visibility = View.GONE
    groupToolbarTitle.visibility = View.VISIBLE

    block.invoke(this)
  }
}

fun Fragment.setupMainScreenToolbar(toolbar: Toolbar, title: String) {
  toolbar.apply {
    navigationIcon = null

    groupGeneralToolbarWidgets.visibility = View.VISIBLE
    ivAppIcon.visibility = View.VISIBLE

    tvToolbarTitle.text = title
  }
}
