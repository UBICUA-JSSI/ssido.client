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

import android.content.pm.PackageManager;

public class PermissionResponse {
    String[] permission;
    int [] grantResult;
    int requestCode;

    public PermissionResponse(String[] permission, int[] grantResult, int requestCode) {
        this.permission = permission;
        this.grantResult = grantResult;
        this.requestCode = requestCode;
    }

    public boolean isGranted(){
        return grantResult != null && grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED;
    }

    public String[] getPermission() {
        return permission;
    }

    public int[] getGrantResult() {
        return grantResult;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
