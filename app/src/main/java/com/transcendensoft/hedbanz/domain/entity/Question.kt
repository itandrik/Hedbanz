package com.transcendensoft.hedbanz.domain.entity

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
 * Entity that describes question.
 * This entity user processes when guess words.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
data class Question(
        var questionId: Long = 0L,
        var yesVoters: List<User> = listOf(),
        var noVoters: List<User> = listOf(),
        var vote: Vote? = null,
        var allUsersCount: Int = 0,
        var messageParent: Message = Message()
) : Message(messageParent.id, messageParent.message, messageParent.userFrom,
        messageParent.messageType, messageParent.createDate, messageParent.clientMessageId), Cloneable {

    private constructor(builder: Builder) : this(builder.questionId, builder.yesVoters,
            builder.noVoters, builder.vote, builder.allUsersCount, builder.message)

    override fun clone(): Question {
        super.clone()
        return copy(questionId = this.questionId,
                yesVoters = this.yesVoters, noVoters = this.noVoters,
                vote = this.vote, allUsersCount = this.allUsersCount,
                messageParent = this.messageParent)
    }

    class Builder {
        var questionId: Long = 0L
            private set

        var yesVoters: List<User> = listOf()
            private set

        var noVoters: List<User> = listOf()
            private set

        var vote: Vote? = null
            private set

        var allUsersCount: Int = 0
            private set

        var message: Message = Message()
            private set

        fun questionId(questionId: Long) = apply { this.questionId = questionId }

        fun yesVoters(yesVoters: List<User>?) = apply { yesVoters?.let { this.yesVoters = yesVoters } }

        fun noVoters(noVoters: List<User>?) = apply { noVoters?.let { this.noVoters = noVoters } }

        fun vote(vote: Vote?) = apply { this.vote = vote }

        fun allUsersCount(allUsersCount: Int) = apply { this.allUsersCount = allUsersCount }

        fun message(message: Message) = apply { this.message = message }

        fun build() = Question(this)
    }

    enum class Vote(val id: Int) {
        NO(0),
        YES(1),
        UNDEFINED(-1);

        companion object {
            fun getVoteById(id: Int): Vote {
                Vote.values().forEach {
                    if (id == it.id) return it
                }
                return UNDEFINED
            }
        }
    }
}