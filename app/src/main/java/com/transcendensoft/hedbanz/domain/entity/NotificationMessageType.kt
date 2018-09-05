package com.transcendensoft.hedbanz.domain.entity

/**
 * Copyright 2017. Andrii Chernysh
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
 * Enum with notification message types.
 * Id of message type must be the same as in server.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
enum class NotificationMessageType(val id: Int) {
    MESSAGE(1),
    SET_WORD(2),
    GUESS_WORD(3),
    FRIEND(4),
    INVITE(5),
    KICK_WARNING(6),
    KICKED(7),
    GAME_OVER(8),
    NEW_ROOM_CREATED(9),
    LAST_PLAYER(10),
    APP_NEW_VERSION(20),
    GLOBAL(21),
    UNDEFINED(100500);

    companion object {
        fun getTypeById(id: Int): NotificationMessageType {
            NotificationMessageType.values().forEach {
                if (id == it.id) return it
            }
            return UNDEFINED
        }
    }
}