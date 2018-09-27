package com.transcendensoft.hedbanz.domain.entity

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
 * Enum class that describes advertise type
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
enum class Advertise(private val code: Int) {
    BANNER(1),
    VIDEO(2),
    DEVELOPERS(3),
    UNDEFINED(100);

    companion object {
        fun getTypeById(id: Int): Advertise {
            Advertise.values().forEach {
                if (id == it.code) return it
            }
            return Advertise.UNDEFINED
        }
    }
}