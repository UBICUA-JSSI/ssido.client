/*
 *
 *  * Copyright 2021 UBICUA.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ssido.ui.permission;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.os.ResultReceiver;

import androidx.core.app.NotificationCompat;


class NotificationHelper {
    private static final int REQUEST_CODE_PUSH = 77;

    static void sendNotification(Context context,
                                        String[] permissions,
                                        int requestCode,
                                        String notificationTitle,
                                        String notificationText,
                                        int notificationIcon,
                                        ResultReceiver receiver) {

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(Constants.REQUEST_CODE, requestCode);
        intent.putExtra(Constants.PERMISSIONS_ARRAY, permissions);
        intent.putExtra(Constants.RESULT_RECEIVER, receiver);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        PendingIntent pending = PendingIntent.getActivity(context, REQUEST_CODE_PUSH, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(notificationIcon)
                .setContentTitle(notificationTitle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setContentText(notificationText)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[0])
                .setContentIntent(pending);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(requestCode, builder.build());
    }
}
