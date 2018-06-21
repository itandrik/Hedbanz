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
 * Class that describes data that app receives when some
 * players try to guess his word
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
data class PlayerGuessing(val player: User?,
                          val attempts: Int,
                          val questionId: Long): Message() {

    private constructor(builder: Builder) : this(builder.player,
            builder.attempts, builder.questionId)

    class Builder {
        var player: User? = null
            private set

        var questionId: Long = 0L
            private set

        var attempts: Int = 0
            private set

        fun questionId(questionId: Long) = apply { this.questionId = questionId }

        fun player(player: User?) = apply { this.player = player }

        fun attempts(attempts: Int) = apply { this.attempts = attempts }

        fun build() = PlayerGuessing(this)
    }
}