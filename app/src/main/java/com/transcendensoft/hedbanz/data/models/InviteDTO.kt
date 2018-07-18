package com.transcendensoft.hedbanz.data.models

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
 * DTO class that describes invite friend to game data
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
data class InviteDTO(
        var senderId: Long? = 0L,
        var roomId: Long? = 0L,
        var invitedUserIds: List<Long>? = emptyList(),
        var password: String? = "") {

    private constructor(builder: Builder) : this(
            builder.senderId, builder.roomId, builder.invitedUserIds, builder.password)

    class Builder {
        var senderId: Long = 0L
            private set
        var roomId: Long = 0L
            private set
        var invitedUserIds: List<Long> = emptyList()
            private set
        var password: String = ""
            private set

        fun senderId(senderId: Long) = apply { this.senderId = senderId }
        fun roomId(roomId: Long) = apply { this.roomId = roomId }
        fun invitedUserIds(invitedUserIds: List<Long>) = apply { this.invitedUserIds = invitedUserIds }
        fun password(password: String) = apply { this.password = password }

        fun build() = InviteDTO(this)
    }
}