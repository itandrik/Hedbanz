package com.transcendensoft.hedbanz.presentation.roomcreation.models

import androidx.annotation.DrawableRes
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.utils.extension.random

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
 * Enum class that binds room backgrounds and their server ids
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
enum class StickerIcons(@DrawableRes val resId: Int, val serverId: Int) {
    STICKER_01(R.drawable.sticker_blue, 1),
    STICKER_02(R.drawable.sticker_brown, 2),
    STICKER_03(R.drawable.sticker_green, 3),
    STICKER_04(R.drawable.sticker_light_blue, 4),
    STICKER_05(R.drawable.sticker_opurple, 5),
    STICKER_06(R.drawable.sticker_orange, 6),
    STICKER_07(R.drawable.sticker_pink, 7),
    STICKER_08(R.drawable.sticker_red, 8),
    STICKER_09(R.drawable.sticker_yellow, 9);

    companion object {
        fun getIconByServerId(serverId: Int) = StickerIcons.values()
                .firstOrNull { it.serverId == serverId }?.resId ?: StickerIcons.STICKER_09.resId

        fun getRandomId() = (0..StickerIcons.values().size).random()
    }
}