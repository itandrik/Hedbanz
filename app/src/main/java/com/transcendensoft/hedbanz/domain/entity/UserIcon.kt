package com.transcendensoft.hedbanz.domain.entity

import android.support.annotation.DrawableRes
import com.transcendensoft.hedbanz.R

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
 * This is enum class that binds
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
enum class UserIcon(val id: Int, @DrawableRes val resId: Int) {
    LOGO_DEFAULT(0, R.drawable.logo),
    LOGO_01(1, R.drawable.logo_01),
    LOGO_02(2, R.drawable.logo_02),
    LOGO_03(3, R.drawable.logo_03);

    companion object {
        fun getResourceById(id: Int): Int {
            UserIcon.values().forEach {
                if (id == it.id) return it.resId
            }
            return UserIcon.LOGO_DEFAULT.resId
        }

        fun getIdByResource(@DrawableRes id: Int): Int {
            UserIcon.values().forEach {
                if (id == it.resId) return it.id
            }
            return UserIcon.LOGO_DEFAULT.id
        }

        fun getUserIconById(id: Int): UserIcon {
            UserIcon.values().forEach {
                if (id == it.id) return it
            }
            return UserIcon.LOGO_DEFAULT
        }
    }
}