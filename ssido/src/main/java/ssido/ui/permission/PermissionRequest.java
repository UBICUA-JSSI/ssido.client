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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.os.ResultReceiver;

public class PermissionRequest {

    private Context context;
    private String[] permissions;
    private int requestCode;
    private String notificationTitle;
    private String notificationText;
    private int icon;

    private PermissionResponse response;

    PermissionRequest(Context context, String[] permissions, int requestCode, String notificationTitle, String notificationText, int icon) {

        this.context           = context;
        this.permissions       = permissions;
        this.requestCode       = requestCode;
        this.notificationTitle = notificationTitle;
        this.notificationText  = notificationText;
        this.icon              = icon;
    }

    public PermissionResponse call() throws InterruptedException {
        if (permissions.length != 0) {
            final Object lock = new Object();
            NotificationHelper.sendNotification(context, permissions, requestCode,
                    notificationTitle, notificationText, icon, new ResultReceiver(new Handler(Looper.getMainLooper())) {
                        @SuppressLint("RestrictedApi")
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            super.onReceiveResult(resultCode, resultData);
                            int[] grantResult    = resultData.getIntArray(Constants.GRANT_RESULT);
                            String[] permissions = resultData.getStringArray(Constants.PERMISSIONS_ARRAY);
                            response = new PermissionResponse(permissions, grantResult, resultCode);

                            synchronized (lock) {
                                lock.notifyAll();
                            }
                        }
                    });
            synchronized (lock) {
                lock.wait();
            }
        } else {
            response = new PermissionResponse(permissions, new int[]{PackageManager.PERMISSION_GRANTED}, requestCode);
        }
        return response;
    }

    public void enqueue(final PermissionResultCallback callback) {
        if (permissions.length != 0) {
            NotificationHelper.sendNotification(context, permissions, requestCode,
                    notificationTitle, notificationText, icon, new ResultReceiver(new Handler()) {
                        @Override
                        @SuppressLint("RestrictedApi")
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            super.onReceiveResult(resultCode, resultData);
                            int[] grantResult = resultData.getIntArray(Constants.GRANT_RESULT);
                            String[] permissions = resultData.getStringArray(Constants.PERMISSIONS_ARRAY);
                            response = new PermissionResponse(permissions, grantResult, resultCode);
                            callback.onComplete(new PermissionResponse(permissions, grantResult, resultCode));
                        }
                    });
        } else {
            callback.onComplete(new PermissionResponse(permissions, new int[]{PackageManager.PERMISSION_GRANTED}, requestCode));
        }
    }
}
