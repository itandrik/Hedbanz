package com.transcendensoft.hedbanz.presentation.language

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager
import com.transcendensoft.hedbanz.domain.entity.Language
import com.transcendensoft.hedbanz.domain.entity.SelectableLanguage
import com.transcendensoft.hedbanz.presentation.StartActivity
import com.transcendensoft.hedbanz.presentation.base.BaseActivity
import com.transcendensoft.hedbanz.presentation.language.list.LanguageAdapter
import java.util.*
import javax.inject.Inject

class LanguageActivity : BaseActivity() {
    @Inject lateinit var preferenceManager: PreferenceManager
    @Inject lateinit var languageAdapter: LanguageAdapter

    @BindView(R.id.rvLanguages) lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    @BindView(R.id.tvToolbarTitle) lateinit var toolbarTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        ButterKnife.bind(this, this)

        preferenceManager.setIsLanguageShowed(true)

        initToolbar()
        initRecycler()
    }

    private fun initToolbar(){
        toolbarTitle.text = getString(R.string.change_language_toolbar)
    }

    private fun initRecycler() {
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = languageAdapter
        recyclerView.overScrollMode = androidx.recyclerview.widget.RecyclerView.OVER_SCROLL_NEVER

        addLanguagesToAdapter()
    }

    private fun addLanguagesToAdapter() {
        val currentLanguage = Language.getLanguageByCode(this, preferenceManager.locale)

        val languages = Language.values().mapIndexed { idx, lang ->
            val isSelected = currentLanguage == lang
            SelectableLanguage(idx, isSelected, lang)
        }

        languageAdapter.items = languages.toMutableList()
    }

    @OnClick(R.id.ivBack)
    protected fun onBackClicked() {
        onBackPressed()
    }

    @OnClick(R.id.btnSubmit)
    protected fun onSubmitClicked() {
        changeLanguageConfig(languageAdapter.getSelectedLanguage())
    }

    override fun onBackPressed() {
        if(intent.getBooleanExtra(getString(R.string.bundle_is_language_after_start), false)){
            finish()
            startActivity(Intent(this, StartActivity::class.java))
        } else {
            super.onBackPressed()
        }
    }

    private fun changeLanguageConfig(language: Language) {
        val localeCode = getString(language.localeCode)
        if(preferenceManager.locale != localeCode ||
                intent.getBooleanExtra(getString(R.string.bundle_is_language_after_start), false)) {
            preferenceManager.locale = localeCode

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(this, language)
            }
            updateResourcesLegacy(this, language)

            val i = baseContext.packageManager
                    .getLaunchIntentForPackage(baseContext.packageName)
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
            startActivity(i)
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: Language): Context {
        val locale = Locale(getString(language.localeCode), getString(language.countryCode))
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: Language): Context {
        val locale = Locale(getString(language.localeCode), getString(language.countryCode))
        Locale.setDefault(locale)

        val resources = context.resources

        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
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
