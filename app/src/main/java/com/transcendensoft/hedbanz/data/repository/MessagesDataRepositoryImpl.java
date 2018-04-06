package com.transcendensoft.hedbanz.data.repository;
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

import com.transcendensoft.hedbanz.data.models.mapper.MessageModelDataMapper;
import com.transcendensoft.hedbanz.data.network.source.MessagesApiDataSource;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.data.source.MessagesDataSource;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.repository.MessagesDataRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Interface that represents a Repository (or Gateway)
 * for getting {@link com.transcendensoft.hedbanz.domain.entity.Message} related data.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class MessagesDataRepositoryImpl implements MessagesDataRepository {
    private MessagesDataSource mDataSource;
    private MessageModelDataMapper mDataMapper;

    @Inject
    public MessagesDataRepositoryImpl(MessagesApiDataSource mDataSource,
                                      MessageModelDataMapper mDataMapper) {
        this.mDataSource = mDataSource;
        this.mDataMapper = mDataMapper;
    }

    @Override
    public Observable<List<Message>> getMessages(long roomId, int page, DataPolicy dataPolicy) {
        if(dataPolicy == DataPolicy.API) {
            return mDataSource.getMessages(roomId, page).map(mDataMapper::convert);
        } else if(dataPolicy == DataPolicy.DB){
            return Observable.error(new UnsupportedOperationException());
        }
        return Observable.error(new UnsupportedOperationException());
    }
}
