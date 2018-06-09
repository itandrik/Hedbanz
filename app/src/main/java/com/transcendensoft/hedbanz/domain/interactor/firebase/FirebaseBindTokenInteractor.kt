package com.transcendensoft.hedbanz.domain.interactor.firebase

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager
import com.transcendensoft.hedbanz.data.source.DataPolicy
import com.transcendensoft.hedbanz.domain.CompletableUseCase
import com.transcendensoft.hedbanz.domain.repository.FirebaseIdDataRepository
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.disposables.CompositeDisposable
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
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for binding FCM token
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class FirebaseBindTokenInteractor @Inject constructor(
        schedulersTransformer: CompletableTransformer,
        compositeDisposable: CompositeDisposable,
        private val mFirebaseIdDataRepository: FirebaseIdDataRepository,
        private val mPreferenceManager: PreferenceManager) :
        CompletableUseCase<String>(schedulersTransformer, compositeDisposable) {

    override fun buildUseCaseCompletable(token: String?): Completable =
            if(token != null) {
                mFirebaseIdDataRepository.bindFirebaseId(
                        mPreferenceManager.user.id, token, DataPolicy.API)
            } else {
                Completable.error(Throwable("firebase token is null, you cant bind"))
            }
}