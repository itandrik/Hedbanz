package com.transcendensoft.hedbanz.presentation.notiification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.di.qualifier.ApplicationContext
import com.transcendensoft.hedbanz.domain.entity.Message
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
 * Class for sending Notifications
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class NotificationManager @Inject constructor(@ApplicationContext val mContext: Context){
    companion object {
        private const val MESSAGE_NOTIFICAION_ID = 1;
        private const val SET_WORD_NOTIFICATION_ID = 2
        private const val GUESS_WORD_NOTIFICATION_ID = 3
        private const val FRIEND_NOTIFICATION_ID = 4;
        private const val INVITE_NOTIFICATION_ID = 5;

        private const val GAME_CHANNEL_ID = "GAME_CHANNEL"
        private const val FRIEND_CHANNEL_ID = "FRIENDS_CHANNEL"
        private const val INVITE_CHANNEL_ID = "INVITE_CHANNEL"
    }

    fun notifyMessage(message: Message){
        createNotificationChannel(
                GAME_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_message))

        val notification = notification(GAME_CHANNEL_ID) {
            setContentTitle(mContext.getString(
                    R.string.game_notification_title,
                    message.userFrom.login))
            setContentText(message.message)
        }

        notify(MESSAGE_NOTIFICAION_ID, notification)
    }

    private fun notify(notificationId: Int, notification: Notification){
        val notificationManager = mContext.getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    private fun notification(channelId: String,
                             block: NotificationCompat.Builder.() -> Unit): Notification{
        val notificationBuilder = NotificationCompat.Builder(mContext,channelId)

        val soundUri = Uri.parse("android.resource://com.transcendensoft.hedbanz/" + R.raw.notification)
        notificationBuilder.setSound(soundUri, AudioManager.STREAM_NOTIFICATION)
                .setLights(Color.argb(100, 250, 185, 5), 2000, 700)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_logo_notification)
                .setAutoCancel(false)
                .apply(block)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel(channelId: String, channelTitle: String){
        val notificationManager = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId,
                    channelTitle, NotificationManager.IMPORTANCE_HIGH)

            // Configure the notification channel.
            notificationChannel.enableLights(true)
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}