package com.transcendensoft.hedbanz.data.models.mapper

import com.transcendensoft.hedbanz.data.models.InviteDTO
import com.transcendensoft.hedbanz.domain.entity.Invite
import javax.inject.Inject

/**
 * Copyright 2018. Andrii Chernysh
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
 * Mapper class used to transform {@link com.transcendensoft.hedbanz.data.models.InviteDTO}
 * (in the data layer) to {@link com.transcendensoft.hedbanz.domain.entity.Invite} in the
 * domain layer.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class InviteModelDataMapper @Inject constructor() {
    fun convert(invite: Invite?): InviteDTO? {
        var inviteDTO: InviteDTO? = null
        if (invite != null) {
            inviteDTO = InviteDTO(
                    senderId = invite.senderId,
                    roomId = invite.roomId,
                    invitedUserIds = invite.invitedUserIds,
                    password = invite.password
            )
        }
        return inviteDTO
    }

    fun convert(inviteDTO: InviteDTO?): Invite? {
        var invite: Invite? = null
        if (inviteDTO != null) {
            invite = Invite(
                    senderId = inviteDTO.senderId,
                    roomId = inviteDTO.roomId,
                    invitedUserIds = inviteDTO.invitedUserIds,
                    password = inviteDTO.password
            )
        }
        return invite
    }
}