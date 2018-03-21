package com.transcendensoft.hedbanz.data.network.service.firebase;
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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

/**
 * Service that handles Firebase push notifications.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class HedbanzFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = HedbanzFirebaseMessagingService.class.getName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.tag(TAG);

        Timber.e("From: %s", remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Timber.i("Notification Body: " + remoteMessage.getNotification().getBody() +
                    "; Title : " + remoteMessage.getNotification().getTitle());
        }
    }
}
