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
 * Enum class that binds room icons and their server ids
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
enum class RoomIcons(@DrawableRes val resId: Int, val serverId: Int) {
    ROOM_01(R.drawable.ic_room_01, 1),
    ROOM_02(R.drawable.ic_room_02, 2),
    ROOM_03(R.drawable.ic_room_03, 3),
    ROOM_04(R.drawable.ic_room_04, 4),
    ROOM_05(R.drawable.ic_room_05, 5),
    ROOM_06(R.drawable.ic_room_06, 6),
    ROOM_07(R.drawable.ic_room_07, 7),
    ROOM_08(R.drawable.ic_room_08, 8),
    ROOM_09(R.drawable.ic_room_09, 9),
    ROOM_10(R.drawable.ic_room_10, 10),
    ROOM_11(R.drawable.ic_room_11, 11),
    ROOM_12(R.drawable.ic_room_12, 12),
    ROOM_13(R.drawable.ic_room_13, 13),
    ROOM_14(R.drawable.ic_room_14, 14),
    ROOM_15(R.drawable.ic_room_15, 15),
    ROOM_16(R.drawable.ic_room_16, 16),
    ROOM_17(R.drawable.ic_room_17, 17),
    ROOM_18(R.drawable.ic_room_18, 18),
    ROOM_19(R.drawable.ic_room_19, 19);

    companion object {

        fun getIconByServerId(serverId: Int) = RoomIcons.values()
                .firstOrNull { it.serverId == serverId }?.resId ?: ROOM_13.resId

        fun getRandomId() = (0..RoomIcons.values().size).random()
    }
}