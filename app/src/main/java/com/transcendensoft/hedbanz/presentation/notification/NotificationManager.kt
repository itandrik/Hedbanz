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
import com.transcendensoft.hedbanz.presentation.friends.FriendsActivity
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
class  NotificationManager @Inject constructor(@ApplicationContext val mContext: Context) {
    companion object {
        private const val MESSAGE_NOTIFICATION_ID = 1
        private const val SET_WORD_NOTIFICATION_ID = 2
        private const val GUESS_WORD_NOTIFICATION_ID = 3
        private const val FRIEND_NOTIFICATION_ID = 4
        private const val INVITE_NOTIFICATION_ID = 5
        private const val KICK_NOTIFICATION_ID = 6
        private const val GAME_OVER_NOTIFICATION_ID = 7
        private const val LAST_USER_NOTIFICATION_ID = 8
        private const val ASKING_QUESTION_NOTIFICATION_ID = 9

        private const val MESSAGE_NOTIFICATION_REQUEST_CODE = 100
        private const val SET_WORD_NOTIFICATION_REQUEST_CODE = 101
        private const val GUESS_WORD_NOTIFICATION_REQUEST_CODE = 102
        private const val FRIEND_NOTIFICATION_REQUEST_CODE = 103
        private const val INVITE_NOTIFICATION_REQUEST_CODE = 104
        private const val KICK_NOTIFICATION_REQUEST_CODE = 105
        private const val GAME_OVER_REQUEST_CODE = 106
        private const val LAST_USER_REQUEST_CODE = 107
        private const val ASKING_QUESTION_REQUEST_CODE = 108

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

        val text = mContext.getString(R.string.game_notification_kick_warning_message, message.roomName
                ?: "")
        val notification = notification(GAME_CHANNEL_ID) {
            setContentTitle(spannedNotificationTitle)
            setContentText(text)
            setStyle(NotificationCompat.BigTextStyle().bigText(text))

            val pendingIntent = getPendingIntentForGame(message, KICK_NOTIFICATION_REQUEST_CODE)
            setContentIntent(pendingIntent)
        }

        notify(KICK_NOTIFICATION_ID, notification)
    }

    fun notifyKick(message: NotificationMessage) {
        createNotificationChannel(
                GAME_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_message))

        val notification = notification(GAME_CHANNEL_ID) {
            setContentTitle(mContext.getString(R.string.game_notification_kick_title, message.roomName
                    ?: ""))
            val text = mContext.getString(R.string.game_notification_kick_message)
            setContentText(text)
            setStyle(NotificationCompat.BigTextStyle().bigText(text))

            val intent = Intent(mContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            val pendingIntent = PendingIntent.getActivity(
                    mContext, KICK_NOTIFICATION_REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT)
            setContentIntent(pendingIntent)
        }

        notify(KICK_NOTIFICATION_ID, notification)
    }

    fun notifyFriendRequest(message: NotificationMessage) {
        createNotificationChannel(
                FRIEND_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_friend))

        val notification = notification(FRIEND_CHANNEL_ID) {
            val title = (mContext
                    .getString(R.string.game_notification_friend_title))
            val spannableTitle = SpannableString(title)
            spannableTitle.setSpan(StyleSpan(android.graphics.Typeface.BOLD),
                    0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setContentTitle(spannableTitle)

            val text = mContext.getString(R.string.game_notification_friend_message, message.senderName)
            val spannedNotificationText = SpannableString(text)
            spannedNotificationText.spanWith(message.senderName ?: "") {
                what = StyleSpan(android.graphics.Typeface.BOLD)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }
            setContentText(text)

            val intent = Intent(mContext, FriendsActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                    mContext, FRIEND_NOTIFICATION_REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT)
            setContentIntent(pendingIntent)
        }

        notify(FRIEND_NOTIFICATION_ID, notification)
    }

    fun notifySetWord(message: NotificationMessage) {
        createNotificationChannel(
                GAME_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_message))

        val text = mContext.getString(R.string.game_notification_set_word_message,
                message.roomName, message.senderName)
        var spannedNotificationText = SpannableString(text)
        if (message.roomName != null && message.senderName != null) {
            spannedNotificationText.spanWith(message.senderName) {
                what = StyleSpan(android.graphics.Typeface.BOLD)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }
            spannedNotificationText.spanWith(message.roomName) {
                what = StyleSpan(android.graphics.Typeface.ITALIC)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }
        } else {
            spannedNotificationText = SpannableString(
                    mContext.getString(R.string.game_notification_set_word_message_error))
        }

        val title = mContext.getString(R.string.game_notification_set_word_title)
        val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(StyleSpan(android.graphics.Typeface.BOLD),
                0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val notification = notification(GAME_CHANNEL_ID) {
            setContentTitle(spannableTitle)
            setContentText(spannedNotificationText)

            setStyle(NotificationCompat.BigTextStyle().bigText(text))

            val pendingIntent = getPendingIntentForGame(message, SET_WORD_NOTIFICATION_REQUEST_CODE)
            setContentIntent(pendingIntent)
        }

        notify(SET_WORD_NOTIFICATION_ID, notification)
    }

    fun notifyGuessWord(message: NotificationMessage){
        createNotificationChannel(
                GAME_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_message))

        val text = mContext.getString(R.string.game_notification_guess_word_message, message.roomName)
        var spannedNotificationText = SpannableString(text)
        if (message.roomName != null) {
            spannedNotificationText.spanWith(message.roomName) {
                what = StyleSpan(android.graphics.Typeface.ITALIC)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }
        } else {
            spannedNotificationText = SpannableString(
                    mContext.getString(R.string.game_notification_guess_word_message_error))
        }

        val title = mContext.getString(R.string.game_notification_guess_word_title)
        val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(StyleSpan(android.graphics.Typeface.BOLD),
                0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val notification = notification(GAME_CHANNEL_ID) {
            setContentTitle(spannableTitle)
            setContentText(spannedNotificationText)

            setStyle(NotificationCompat.BigTextStyle().bigText(text))

            val pendingIntent = getPendingIntentForGame(message, GUESS_WORD_NOTIFICATION_REQUEST_CODE)
            setContentIntent(pendingIntent)
        }

        notify(GUESS_WORD_NOTIFICATION_ID, notification)
    }

    fun notifyAskingQuestion(message: NotificationMessage){
        createNotificationChannel(
                GAME_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_message))

        val text = mContext.getString(R.string.game_notification_asking_question_message, message.roomName)
        var spannedNotificationText = SpannableString(text)
        if (message.roomName != null) {
            spannedNotificationText.spanWith(message.roomName) {
                what = StyleSpan(android.graphics.Typeface.ITALIC)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }
        } else {
            spannedNotificationText = SpannableString(
                    mContext.getString(R.string.game_notification_asking_question_message_error))
        }

        val title = mContext.getString(R.string.game_notification_asking_question_title, message.senderName)
        var spannedNotificationTitle = SpannableString(title)
        if (!message.senderName.isNullOrEmpty()) {
            spannedNotificationTitle.spanWith(message.senderName!!) {
                what = StyleSpan(android.graphics.Typeface.BOLD)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }
        } else {
            spannedNotificationTitle = SpannableString(
                    mContext.getString(R.string.game_notification_asking_question_title, ""))
        }
        /*val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(StyleSpan(android.graphics.Typeface.BOLD),
                0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)*/

        val notification = notification(GAME_CHANNEL_ID) {
            setContentTitle(spannedNotificationTitle)
            setContentText(spannedNotificationText)

            setStyle(NotificationCompat.BigTextStyle().bigText(text))

            val pendingIntent = getPendingIntentForGame(message, ASKING_QUESTION_REQUEST_CODE)
            setContentIntent(pendingIntent)
        }

        notify(ASKING_QUESTION_NOTIFICATION_ID, notification)
    }

    fun notifyInviteToGame(message: NotificationMessage){
        createNotificationChannel(
                INVITE_CHANNEL_ID,
                mContext.getString(R.string.game_invite_notification_channel_title_message))

        val text = mContext.getString(R.string.game_notification_invite_message,
                message.senderName, message.roomName)
        var spannedNotificationText = SpannableString(text)
        if (message.roomName != null && message.senderName != null) {
            spannedNotificationText.spanWith(message.roomName) {
                what = StyleSpan(android.graphics.Typeface.ITALIC)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }

            spannedNotificationText.spanWith(message.senderName) {
                what = StyleSpan(android.graphics.Typeface.BOLD)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }
        } else {
            spannedNotificationText = SpannableString(
                    mContext.getString(R.string.game_notification_invite_message_error))
        }

        val title = mContext.getString(R.string.game_notification_invite_title)
        val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(StyleSpan(android.graphics.Typeface.BOLD),
                0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val notification = notification(INVITE_CHANNEL_ID) {
            setContentTitle(spannableTitle)
            setContentText(spannedNotificationText)

            setStyle(NotificationCompat.BigTextStyle().bigText(text))

            val pendingIntent = getPendingIntentForGame(message, INVITE_NOTIFICATION_REQUEST_CODE)
            setContentIntent(pendingIntent)
        }

        notify(INVITE_NOTIFICATION_ID, notification)
    }

    fun notifyGameOver(message: NotificationMessage){
        createNotificationChannel(
                GAME_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_message))

        val text = mContext.getString(R.string.game_notification_game_over_message, message.roomName)
        var spannedNotificationText = SpannableString(text)
        if (message.roomName != null && message.senderName != null) {
            spannedNotificationText.spanWith(message.roomName) {
                what = StyleSpan(android.graphics.Typeface.ITALIC)
                flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            }
        } else {
            spannedNotificationText = SpannableString(
                    mContext.getString(R.string.game_notification_game_over_message_error))
        }

        val title = mContext.getString(R.string.game_notification_game_over_title)
        val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(StyleSpan(android.graphics.Typeface.BOLD),
                0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val notification = notification(INVITE_CHANNEL_ID) {
            setContentTitle(spannableTitle)
            setContentText(spannedNotificationText)

            setStyle(NotificationCompat.BigTextStyle().bigText(text))

            val pendingIntent = getPendingIntentForGame(message, GAME_OVER_REQUEST_CODE)
            setContentIntent(pendingIntent)
        }

        notify(GAME_OVER_NOTIFICATION_ID, notification)
    }

    fun notifyLastUser(message: NotificationMessage){
        createNotificationChannel(
                GAME_CHANNEL_ID,
                mContext.getString(R.string.game_notification_channel_title_message))

        val title = mContext.getString(R.string.game_notification_last_user_title, message.roomName)
        val spannableTitle= SpannableString(title)
        spannableTitle.setSpan(StyleSpan(android.graphics.Typeface.BOLD),
                0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val text = mContext.getString(R.string.game_notification_last_user_message)

        val notification = notification(INVITE_CHANNEL_ID) {
            setContentTitle(spannableTitle)
            setContentText(text)

            setStyle(NotificationCompat.BigTextStyle().bigText(text))

            val intent = Intent(mContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(
                    mContext, LAST_USER_REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT)
            setContentIntent(pendingIntent)
        }

        notify(LAST_USER_NOTIFICATION_ID, notification)
    }

    private fun getPendingIntentForGame(message: NotificationMessage, requestCode: Int): PendingIntent? {
        val stackBuilder = TaskStackBuilder.create(mContext)
        val parentIntent = Intent(mContext, MainActivity::class.java)
        parentIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK);
        stackBuilder.addNextIntentWithParentStack(parentIntent)

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
                .setLargeIcon(BitmapFactory.decodeResource(mContext.resources,
                        R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setVibrate(longArrayOf(0L, 100L, 300L, 100L))
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
            val attrs = AudioAttributes.Builder()
            attrs.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            attrs.setUsage(AudioAttributes.USAGE_NOTIFICATION)

            notificationChannel.setSound(soundUri, attrs.build())
            notificationChannel.shouldVibrate()
            notificationChannel.vibrationPattern = longArrayOf(0L, 100L, 300L, 100L)
            notificationChannel.lightColor = Color.argb(100, 250, 185, 5)

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun cancelKickNotification(){
        cancelNotification(KICK_NOTIFICATION_ID)
    }

    fun cancelGameOverNotification(){
        cancelNotification(GAME_OVER_NOTIFICATION_ID)
    }

    fun cancelMessageNotifications(){
        cancelNotification(MESSAGE_NOTIFICATION_ID)
    }

    fun cancelAllLeaveFromRoomNotifications(){
        cancelNotification(MESSAGE_NOTIFICATION_ID)
        cancelNotification(GAME_OVER_NOTIFICATION_ID)
        cancelNotification(KICK_NOTIFICATION_ID)
        cancelNotification(GUESS_WORD_NOTIFICATION_ID)
        cancelNotification(SET_WORD_NOTIFICATION_ID)
        cancelNotification(ASKING_QUESTION_NOTIFICATION_ID)
    }

    private fun cancelNotification(notificationId: Int){
        val notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}