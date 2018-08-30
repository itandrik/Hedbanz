package com.transcendensoft.hedbanz.data.models.mapper;
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

import com.transcendensoft.hedbanz.data.models.FriendDTO;
import com.transcendensoft.hedbanz.domain.entity.Friend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Mapper class used to transform {@link com.transcendensoft.hedbanz.data.models.FriendDTO}
 * (in the data layer) to {@link com.transcendensoft.hedbanz.domain.entity.Friend} in the
 * domain layer.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class FriendModelDataMapper {
    @Inject
    public FriendModelDataMapper() {
    }

    public Friend convert(FriendDTO friendDTO) {
        Friend friendResult = null;
        if (friendDTO != null) {
            friendResult = new Friend.Builder()
                    .setId(friendDTO.getId())
                    .setEmail(friendDTO.getEmail())
                    .setLogin(friendDTO.getLogin())
                    .setMoney(friendDTO.getMoney())
                    .setRegistrationDate(friendDTO.getRegistrationDate())
                    .setIsAccepted(friendDTO.isAccepted())
                    .setIsPending(friendDTO.isPending())
                    .setIsFriend(friendDTO.isFriend())
                    .setWord(friendDTO.getWord())
                    .setIsWinner(friendDTO.isWinner())
                    .setIsInGame(friendDTO.isInGame())
                    .setIsInvited(friendDTO.isInvited())
                    .setGamesNumber(friendDTO.getGamesNumber())
                    .setFriendsNumber(friendDTO.getFriendsNumber())
                    .build();
        }
        return friendResult;
    }

    public List<Friend> convert(Collection<FriendDTO> friendDTOCollection) {
        if (friendDTOCollection == null) {
            return null;
        }

        final List<Friend> friends = new ArrayList<>(20);
        for (FriendDTO friendDTO : friendDTOCollection) {
            final Friend friend = convert(friendDTO);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }
}
