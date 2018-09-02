package com.transcendensoft.hedbanz.data.models.mapper

import com.transcendensoft.hedbanz.data.models.FeedbackDTO
import com.transcendensoft.hedbanz.data.models.InviteDTO
import com.transcendensoft.hedbanz.domain.entity.Feedback
import com.transcendensoft.hedbanz.domain.entity.Invite
import javax.inject.Inject

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
 * Mapper class used to transform {@link com.transcendensoft.hedbanz.data.models.FeedbackDTO}
 * (in the data layer) to {@link com.transcendensoft.hedbanz.domain.entity.Feedback} in the
 * domain layer.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class FeedbackModelDataMapper @Inject constructor(val userModelDataMapper: UserModelDataMapper) {
    fun convert(feedback: Feedback?): FeedbackDTO? {
        var feedbackDTO: FeedbackDTO? = null
        if (feedback != null) {
            feedbackDTO = FeedbackDTO(
                    feedbackText = feedback.feedbackText,
                    userDto = userModelDataMapper.convert(feedback.user),
                    deviceVersion = feedback.deviceVersion,
                    deviceName = feedback.deviceName,
                    deviceModel = feedback.deviceModel,
                    deviceManufacturer = feedback.deviceManufacturer,
                    product = feedback.deviceModel
            )
        }
        return feedbackDTO
    }

    fun convert(feedbackDTO: FeedbackDTO?): Feedback? {
        var feedback: Feedback? = null
        if (feedbackDTO != null) {
            feedback = Feedback(
                    feedbackText = feedbackDTO.feedbackText,
                    user = userModelDataMapper.convert(feedbackDTO.userDto),
                    deviceVersion = feedbackDTO.deviceVersion,
                    deviceName = feedbackDTO.deviceName,
                    deviceModel = feedbackDTO.deviceModel,
                    deviceManufacturer = feedbackDTO.deviceManufacturer,
                    product = feedbackDTO.deviceModel
            )
        }
        return feedback
    }
}