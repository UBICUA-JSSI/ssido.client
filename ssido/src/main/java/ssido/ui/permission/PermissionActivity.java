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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import ssido.R;


public class PermissionActivity extends AppCompatActivity {

    private static final String TAG = PermissionActivity.class.getName();

    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle boundle) {
        super.onCreate(boundle);
        setContentView(R.layout.activity_permission);

        if(getIntent() != null) {
            resultReceiver = getIntent().getParcelableExtra(Constants.RESULT_RECEIVER);
            String[] permissions = getIntent().getStringArrayExtra(Constants.PERMISSIONS_ARRAY);
            int requestCode = getIntent().getIntExtra(Constants.REQUEST_CODE, Constants.DEFAULT_REQUEST_CODE);
            if(!hasPermissions(permissions)) {
                ActivityCompat.requestPermissions(this, permissions, requestCode);
            }else {
                onComplete(requestCode, permissions, new int[]{PackageManager.PERMISSION_GRANTED});
            }
        }else {
            finish();
        }
    }

    @SuppressLint("RestrictedApi")
    private void onComplete(int requestCode, String[] permissions, int[] grantResults) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(Constants.PERMISSIONS_ARRAY, permissions);
        bundle.putIntArray(Constants.GRANT_RESULT, grantResults);
        bundle.putInt(Constants.REQUEST_CODE, requestCode);
        resultReceiver.send(requestCode, bundle);
        finish();

    }

    private boolean hasPermissions(String[] permissions) {
        boolean result = true;
        for(String permission: permissions) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onComplete(requestCode, permissions, grantResults);
    }
}
