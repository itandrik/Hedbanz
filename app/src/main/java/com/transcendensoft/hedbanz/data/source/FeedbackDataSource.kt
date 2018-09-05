package com.transcendensoft.hedbanz.data.source

import com.transcendensoft.hedbanz.data.models.FeedbackDTO
import com.transcendensoft.hedbanz.data.network.source.ApiDataSource
import com.transcendensoft.hedbanz.data.network.source.RoomsApiDataSource

import dagger.Binds
import dagger.Module
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Base interface for remote and local data that
 * describes methods of submitting feedback of specific user
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
interface FeedbackDataSource{
    fun submitFeedback(feedback: FeedbackDTO): Observable<Boolean>
}
