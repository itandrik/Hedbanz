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

import com.transcendensoft.hedbanz.data.models.UserDTO;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;
import com.transcendensoft.hedbanz.domain.entity.PlayerStatus;
import com.transcendensoft.hedbanz.domain.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Mapper class used to transform {@link com.transcendensoft.hedbanz.data.models.UserDTO}
 * (in the data layer) to {@link com.transcendensoft.hedbanz.domain.entity.User} in the
 * domain layer.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ApplicationScope
public class UserModelDataMapper {
    @Inject
    public UserModelDataMapper() {
    }

    public User convert(UserDTO userDTO){
        User userResult = null;
        if(userDTO != null){
            long id;
            if(userDTO.getUserId() == null){
                id = userDTO.getId();
            } else {
                id = userDTO.getUserId();
            }
            userResult = new User.Builder()
                    .setId(id)
                    .setEmail(userDTO.getEmail())
                    .setLogin(userDTO.getLogin())
                    .setMoney(userDTO.getMoney())
                    .setPassword(userDTO.getPassword())
                    .setRegistrationDate(userDTO.getRegistrationDate())
                    .setPlayerStatus(PlayerStatus.getStatusByCode(userDTO.getStatus()))
                    .setIsFriend(userDTO.isFriend())
                    .setWord(userDTO.getWord())
                    .setIsWinner(userDTO.isWinner())
                    .setAttempts(userDTO.getAttempts())
                    .build();
        }
        return userResult;
    }

    public UserDTO convert(User user){
        UserDTO userResult = null;
        if(user != null){
            userResult = new UserDTO.Builder()
                    .setId(user.getId())
                    .setEmail(user.getEmail())
                    .setLogin(user.getLogin())
                    .setMoney(user.getMoney())
                    .setPassword(user.getPassword())
                    .setRegistrationDate(user.getRegistrationDate())
                    .setStatus(user.getPlayerStatus() != null ?
                            user.getPlayerStatus().getCode() : PlayerStatus.UNDEFINED.getCode())
                    .setIsFriend(user.isFriend())
                    .setWord(user.getWord())
                    .setAttempts(user.getAttempts())
                    .setIsWinner(user.isWinner())
                    .build();
        }
        return userResult;
    }

    public List<User> convertToUsers(Collection<UserDTO> userDTOCollection) {
        if(userDTOCollection == null){
            return null;
        }

        final List<User> usersResult = new ArrayList<>(20);
        for (UserDTO userDTO : userDTOCollection) {
            final User user = convert(userDTO);
            if (user != null) {
                usersResult.add(user);
            }
        }
        return usersResult;
    }

    public List<UserDTO> convertToDtoUsers(Collection<User> userCollection) {
        if(userCollection == null) {
            return null;
        }

        final List<UserDTO> usersResult = new ArrayList<>(20);
        for (User user : userCollection) {
            final UserDTO userDTO = convert(user);
            if (userDTO != null) {
                usersResult.add(userDTO);
            }
        }
        return usersResult;
    }
}
