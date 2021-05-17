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

import android.content.Context;

import androidx.annotation.DrawableRes;


public class Permission {
    public static PermissionRequest getPermission(Context context,
                                                  String[] permissions,
                                                  int requestCode,
                                                  String notificationTitle,
                                                  String notificationText,
                                                  @DrawableRes int notificationIcon){
        return new PermissionRequest(context, permissions, requestCode,
                notificationTitle, notificationText, notificationIcon);
    }
}
