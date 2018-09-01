package com.transcendensoft.hedbanz.presentation.invite.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.domain.entity.Friend
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
 * Adapter for invite friends recycler
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class InviteAdapter @Inject constructor() : RecyclerView.Adapter<InviteViewHolder>() {
    var items: MutableList<Friend> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteViewHolder =
            InviteViewHolder(parent.context, LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_friend_invite, parent, false))

    override fun onBindViewHolder(holder: InviteViewHolder, position: Int) {
        val friend = items[position]

        holder.bindFriendIcon(friend.iconId)
        holder.bindFriendName(friend.login)
        holder.bindFlags(friend.isInGame, friend.isInvited)
        holder.bindOnClick(friend)
    }

    fun getSelectedFriends(): List<Friend> = items.filter { it.isSelected }
}