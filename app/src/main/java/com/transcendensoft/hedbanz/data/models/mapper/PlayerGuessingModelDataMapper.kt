package com.transcendensoft.hedbanz.data.models.mapper

import com.transcendensoft.hedbanz.data.models.PlayerGuessingDTO
import com.transcendensoft.hedbanz.domain.entity.PlayerGuessing
import javax.inject.Inject

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
 * Mapper class used to transform {@link com.transcendensoft.hedbanz.data.models.PlayerGuessingDTO}
 * (in the data layer) to {@link com.transcendensoft.hedbanz.domain.entity.PlayerGuessing} in the
 * domain layer.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class PlayerGuessingModelDataMapper @Inject constructor(
        val userModelDataMapper: UserModelDataMapper) {
    fun convert(playerGuessing: PlayerGuessing?): PlayerGuessingDTO? {
        var playerGuessingDTO: PlayerGuessingDTO? = null
        if (playerGuessing != null) {
            playerGuessingDTO = PlayerGuessingDTO.Builder()
                    .setPlayer(userModelDataMapper.convert(playerGuessing.player))
                    .setAttempts(playerGuessing.attempts)
                    .setQuestionId(playerGuessing.questionId)
                    .build()
        }
        return playerGuessingDTO
    }

    fun convert(playerGuessingDTO: PlayerGuessingDTO?): PlayerGuessing? {
        var playerGuessing: PlayerGuessing? = null
        if (playerGuessingDTO != null) {
            playerGuessing = PlayerGuessing.Builder()
                    .player(userModelDataMapper.convert(playerGuessingDTO.player))
                    .attempts(playerGuessingDTO.attempts)
                    .questionId(playerGuessingDTO.questionId)
                    .build()
        }
        return playerGuessing
    }
}