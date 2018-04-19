package com.transcendensoft.hedbanz.domain.interactor.game;
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

import android.support.annotation.NonNull;

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.PaginationState;
import com.transcendensoft.hedbanz.domain.PaginationUseCase;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.repository.MessagesDataRepository;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for retrieving data related to a specific list of
 * {@link com.transcendensoft.hedbanz.domain.entity.Message}.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class GetMessagesInteractor extends PaginationUseCase<Message, GetMessagesInteractor.Param, Void>{
    private MessagesDataRepository mDataRepository;
    private PreferenceManager mPreferenceManager;

    @Inject
    public GetMessagesInteractor(ObservableTransformer mSchedulersTransformer,
                                 CompositeDisposable mCompositeDisposable,
                                 MessagesDataRepository mDataRepository,
                                 PreferenceManager mPreferenceManager) {
        super(mSchedulersTransformer, mCompositeDisposable);
        this.mDataRepository = mDataRepository;
        this.mPreferenceManager = mPreferenceManager;
    }

    @Override
    protected Observable<PaginationState<Message>> buildUseCaseObservable(GetMessagesInteractor.Param param) {
        return mDataRepository.getMessages(param.getRoomId(), mCurrentPage, DataPolicy.API)
                .map(messages -> mapSetMessageUserAndType(param, messages))
                .flatMap(this::convertEntitiesToPagingResult)
                .doOnError(err -> mCurrentPage--)
                .onErrorReturn(this::mapPaginationStateBasedOnError);
    }

    @NonNull
    private List<Message> mapSetMessageUserAndType(Param param, List<Message> messages) {
        for (Message message:messages) {
            if(message.getMessageType() == MessageType.SIMPLE_MESSAGE){
                if(mPreferenceManager.getUser().equals(message.getUserFrom())){
                    message.setMessageType(MessageType.SIMPLE_MESSAGE_THIS_USER);
                } else {
                    message.setMessageType(MessageType.SIMPLE_MESSAGE_OTHER_USER);
                }
            }
            User fullUser = getUserWithId(param.getUsers(), message.getUserFrom().getId());
            if(fullUser != null){
                message.setUserFrom(fullUser);
            }
        }
        return messages;
    }

    @Nullable
    private User getUserWithId(List<User> users, long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public static class Param{
        private Long roomId;
        private List<User> users;

        public Param(Long roomId, List<User> users) {
            this.roomId = roomId;
            this.users = users;
        }

        public Long getRoomId() {
            return roomId;
        }

        public void setRoomId(Long roomId) {
            this.roomId = roomId;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }
}
