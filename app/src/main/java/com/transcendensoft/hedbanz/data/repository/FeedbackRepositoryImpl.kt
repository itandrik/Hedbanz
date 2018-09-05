package com.transcendensoft.hedbanz.data.repository

import com.transcendensoft.hedbanz.data.models.mapper.FeedbackModelDataMapper
import com.transcendensoft.hedbanz.data.network.source.FeedbackApiDataSource
import com.transcendensoft.hedbanz.data.network.source.FirebaseIdApiDataSource
import com.transcendensoft.hedbanz.domain.entity.Feedback
import com.transcendensoft.hedbanz.domain.repository.FeedbackRepository
import com.transcendensoft.hedbanz.domain.repository.FirebaseIdDataRepository
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
 * Interface that represents a Repository (or Gateway)
 * for binding and unbinding firebase id
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class FeedbackRepositoryImpl @Inject constructor(
        private val feedbackApiDataSource: FeedbackApiDataSource,
        private val feedbackModelDataMapper: FeedbackModelDataMapper
) : FeedbackRepository {

    override fun submitFeedback(feedback: Feedback) =
            feedbackApiDataSource.submitFeedback(feedbackModelDataMapper.convert(feedback)!!)

}