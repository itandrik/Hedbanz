package com.transcendensoft.hedbanz.domain.interactor.friends;
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

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.CompletableUseCase;
import com.transcendensoft.hedbanz.domain.repository.FriendsDataRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for deleting specific
 * {@link com.transcendensoft.hedbanz.domain.entity.Friend} for current
 * {@link com.transcendensoft.hedbanz.domain.entity.User}
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RemoveFriend extends CompletableUseCase<RemoveFriend.Param> {
    private FriendsDataRepository mRepository;
    private PreferenceManager mPreferenceManger;

    @Inject
    public RemoveFriend(CompletableTransformer schedulersTransformer,
                     CompositeDisposable compositeDisposable,
                     FriendsDataRepository mRepository,
                     PreferenceManager mPreferenceManger) {
        super(schedulersTransformer, compositeDisposable);
        this.mRepository = mRepository;
        this.mPreferenceManger = mPreferenceManger;
    }

    @Override
    protected Completable buildUseCaseCompletable(RemoveFriend.Param params) {
        return mRepository.removeFriend(mPreferenceManger.getUser().getId(),
                params.getFriendId(), params.getDataPolicy());

    }

    public static final class Param {
        private DataPolicy mDataPolicy;
        private long mFriendId;

        public Param(DataPolicy mDataPolicy, long friendId) {
            this.mDataPolicy = mDataPolicy;
            this.mFriendId = friendId;
        }

        public DataPolicy getDataPolicy() {
            return mDataPolicy;
        }

        public void setDataPolicy(DataPolicy mDataPolicy) {
            this.mDataPolicy = mDataPolicy;
        }

        public long getFriendId() {
            return mFriendId;
        }

        public void setFriendId(long friendId) {
            this.mFriendId = friendId;
        }
    }
}
