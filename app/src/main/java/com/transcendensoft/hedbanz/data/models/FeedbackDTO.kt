package com.transcendensoft.hedbanz.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.transcendensoft.hedbanz.domain.entity.User

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
 * DTO for feedback entity.
 * All feedback properties received from server described here
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
data class FeedbackDTO(var feedbackText: String,
                       @SerializedName("user") @Expose var userDto: UserDTO?,
                       var deviceVersion: Int?,
                       var deviceName: String?,
                       var deviceModel: String?,
                       var deviceManufacturer: String?,
                       var product: String?)