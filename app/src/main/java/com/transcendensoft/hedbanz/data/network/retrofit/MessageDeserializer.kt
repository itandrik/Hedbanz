package com.transcendensoft.hedbanz.data.network.retrofit

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.transcendensoft.hedbanz.data.models.MessageDTO
import com.transcendensoft.hedbanz.data.models.QuestionDTO
import com.transcendensoft.hedbanz.domain.entity.MessageType
import java.lang.reflect.Type
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
 * Deserializer for GSON. Needed in order to
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class MessageDeserializer @Inject constructor(): JsonDeserializer<MessageDTO>{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): MessageDTO? {
        val gson = Gson()
        val obj = json?.asJsonObject
        val type = obj?.get("type")?.asString
        if (type.equals("${MessageType.GUESS_WORD_THIS_USER.id}", ignoreCase = true)) {
            return gson.fromJson(json, QuestionDTO::class.java)
        } else {
            return gson.fromJson(json, MessageDTO::class.java)
        }
    }
}