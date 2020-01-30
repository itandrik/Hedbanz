package com.transcendensoft.hedbanz.domain.entity

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.transcendensoft.hedbanz.R

/**
 * Copyright 2017. Andrii Chernysh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Class that enumerates all languages that an application supports
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
enum class Language(@StringRes val localeCode: Int,
                    @StringRes val countryCode: Int,
                    @StringRes val textRes: Int,
                    @DrawableRes val iconRes: Int) {
    ENGLISH(R.string.lang_code_en, R.string.country_code_en, R.string.language_english, R.drawable.ic_england),
    DEUTSCH(R.string.lang_code_de, R.string.country_code_de, R.string.language_german, R.drawable.ic_germany),
    RUSSIAN(R.string.lang_code_ru, R.string.country_code_ru, R.string.language_russian, R.drawable.ic_russia),
    UKRAINIAN(R.string.lang_code_uk, R.string.country_code_uk, R.string.language_ukrainian, R.drawable.ic_ukraine);

    companion object {
        fun getLanguageByCode(context: Context, code: String): Language? {
            Language.values().forEach {
                if (context.getString(it.localeCode) == code) return it
            }
            return null
        }
    }
}