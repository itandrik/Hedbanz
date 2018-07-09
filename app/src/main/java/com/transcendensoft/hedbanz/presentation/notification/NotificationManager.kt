package com.transcendensoft.hedbanz.presentation.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import com.crashlytics.android.Crashlytics
import com.transcendensoft.hedbanz.R
import com.transcendensoft.hedbanz.di.qualifier.ApplicationContext
import com.transcendensoft.hedbanz.domain.entity.NotificationMessage
import com.transcendensoft.hedbanz.presentation.game.GameActivity
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity
import com.transcendensoft.hedbanz.utils.extension.spanWith
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
class NotificationManager @Inject constructor(@ApplicationContext val mContext: Context) {
    companion object {
        private const val MESSAGE_NOTIFICATION_ID = 1;
        private const val SET_WORD_NOTIFICATION_ID = 2
        private const val GUESS_WORD_NOTIFICATION_ID = 3
        private const val FRIEND_NOTIFICATION_ID = 4;
        private const val INVITE_NOTIFICATION_ID = 5;
        private const val KICK_NOTIFICATION_ID = 6;

        private const val MESSAGE_NOTIFICATION_REQUEST_CODE = 100;
        private const val SET_WORD_NOTIFICATION_REQUEST_CODE = 101
        private const val GUESS_WORD_NOTIFICATION_REQUEST_CODE = 102
        private const val FRIEND_NOTIFICATION_REQUEST_CODE = 103;
        private const val INVITE_NOTIFICATION_REQUEST_CODE = 104;
        private const val KICK_NOTIFICATION_REQUEST_CODE = 105;

        private const val GAME_CHANNEL_ID = "GAME_CHANNEL"
        private const val FRIEND_CHANNEL_ID = "FRIENDS_CHANNEL"
        private const val INVITE_CHANNEL_ID = "INVITE_CHANNEL"
    }

    fun notifyMessage(message: NotificationMessage) {
        createNotificationChannel(
                GAME_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_message))

        val text = mContext.getString(R.string.game_notification_title,
                message.senderName, message.roomName)
        var spannedNotificationTitle = SpannableString(text)
        if (message.roomName != null && message.senderName != null) {
            spannedNotificationTitle.spanWith(message.senderName) {
                what = StyleSpan(android.graphics.Typeface.BOLD)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }
            spannedNotificationTitle.spanWith(message.roomName) {
                what = StyleSpan(android.graphics.Typeface.ITALIC)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }
        } else {
            spannedNotificationTitle = SpannableString(
                    mContext.getString(R.string.game_notification_title_error))
        }

        val notification = notification(GAME_CHANNEL_ID) {
            setContentTitle(spannedNotificationTitle)
            setContentText(message.text)

            val pendingIntent = getPendingIntentForGame(message, MESSAGE_NOTIFICATION_REQUEST_CODE)
            setContentIntent(pendingIntent)
        }

        notify(MESSAGE_NOTIFICATION_ID, notification)
    }

    fun notifyKickWarning(message: NotificationMessage) {
        createNotificationChannel(
                GAME_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_message))

        val title = mContext.getString(R.string.game_notification_kick_warning_title)
        val spannedNotificationTitle = SpannableString(title)
        spannedNotificationTitle.spanWith(title.substringBefore(delimiter = "!")) {
            what = StyleSpan(android.graphics.Typeface.BOLD)
            flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        }

        val text = mContext.getString(R.string.game_notification_kick_warning_message, message.roomName ?: "")
        val notification = notification(GAME_CHANNEL_ID) {
            setContentTitle(spannedNotificationTitle)
            setContentText(text)
            setStyle(NotificationCompat.BigTextStyle().bigText(text))

            val pendingIntent = getPendingIntentForGame(message, KICK_NOTIFICATION_REQUEST_CODE)
            setContentIntent(pendingIntent)
        }

        notify(MESSAGE_NOTIFICATION_ID, notification)
    }

    fun notifyKick(message: NotificationMessage) {
        createNotificationChannel(
                GAME_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_message))

        val notification = notification(GAME_CHANNEL_ID) {
            setContentTitle(mContext.getString(R.string.game_notification_kick_title, message.roomName ?: ""))
            val text = mContext.getString(R.string.game_notification_kick_message)
            setContentText(text)
            setStyle(NotificationCompat.BigTextStyle().bigText(text))

            val intent = Intent(mContext, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                    mContext, KICK_NOTIFICATION_REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT)
            setContentIntent(pendingIntent)
        }

        notify(MESSAGE_NOTIFICATION_ID, notification)
    }

    private fun getPendingIntentForGame(message: NotificationMessage, requestCode: Int): PendingIntent? {
        val stackBuilder = TaskStackBuilder.create(mContext)
        stackBuilder.addParentStack(MainActivity::class.java)

        val gameIntent = Intent(mContext, GameActivity::class.java)
        gameIntent.putExtra(mContext.getString(R.string.bundle_room_id), message.roomId)
        stackBuilder.addNextIntent(gameIntent)

        return stackBuilder.getPendingIntent(
                requestCode, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun notify(notificationId: Int, notification: Notification) {
        val notificationManager = mContext.getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    private fun notification(channelId: String,
                             block: NotificationCompat.Builder.() -> Unit): Notification {
        val notificationBuilder = NotificationCompat.Builder(mContext, channelId)

        val soundUri = Uri.parse("android.resource://com.transcendensoft.hedbanz/" + R.raw.notification)
        notificationBuilder.setSound(soundUri, AudioManager.STREAM_NOTIFICATION)
                .setLights(Color.argb(100, 250, 185, 5), 2000, 700)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(false)
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                        R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setVibrate(longArrayOf(0L, 600L))
                .apply(block)

        try {
            notificationBuilder.setSmallIcon(R.drawable.ic_logo_notification)
        } catch (remoteServiceException: RuntimeException) {
            val e = RuntimeException("Can`t set image icon notification")
            e.stackTrace = remoteServiceException.stackTrace
            Crashlytics.logException(e)
        }


        return notificationBuilder.build()
    }

    private fun createNotificationChannel(channelId: String, channelTitle: String) {
        val notificationManager = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId,
                    channelTitle, NotificationManager.IMPORTANCE_HIGH)

            // Configure the notification channel.
            notificationChannel.enableLights(true)
            val soundUri = Uri.parse("android.resource://com.transcendensoft.hedbanz/" + R.raw.notification)
            val attrs = AudioAttributes.Builder();
            attrs.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            attrs.setUsage(AudioAttributes.USAGE_NOTIFICATION);

            notificationChannel.setSound(soundUri, attrs.build())
            notificationChannel.shouldVibrate()
            notificationChannel.vibrationPattern = longArrayOf(0L, 600L)
            notificationChannel.lightColor = Color.argb(100, 250, 185, 5)

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}