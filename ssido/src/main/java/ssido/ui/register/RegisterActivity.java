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

package ssido.ui.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import ssido.R;
import ssido.Ssido;
import ssido.ui.home.MainActivity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class RegisterActivity extends Activity implements ZXingScannerView.ResultHandler  {

    private static final String TAG = RegisterActivity.class.getName();

    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZXingScannerView scannerView;
    private boolean flash;
    private boolean autoFocus;
    private ArrayList<Integer> selectedIndices;
    private int cameraId = -1;
    private Ssido app;

   @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_register);
        app = (Ssido) getApplication();
        ViewGroup contentFrame = findViewById(R.id.window);
        scannerView = new ZXingScannerView(this);
        contentFrame.addView(scannerView);

        if(state != null) {
            flash           = state.getBoolean(FLASH_STATE, false);
            autoFocus       = state.getBoolean(AUTO_FOCUS_STATE, true);
            selectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            cameraId        = state.getInt(CAMERA_ID, -1);
        } else {
            flash           = false;
            autoFocus       = true;
            selectedIndices = null;
            cameraId        = -1;
        }
        setupFormats();
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera(cameraId);
        scannerView.setFlash(flash);
        scannerView.setAutoFocus(autoFocus);
    }

    @Override
    public void handleResult(Result result) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(app, notification);
            ringtone.play();
            ceremony(result.getText());
        } catch (URISyntaxException e) {
            Log.e(TAG, String.format("Error: %s", e.getMessage()));
        }
    }

    public static boolean hasCamera(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void ceremony(String message) throws URISyntaxException {
        URI uri = new URI(message);
        if (!uri.getScheme().equals("wss")) {
            return;
        }

        String sessionId = message.substring(message.lastIndexOf("/") + 1);
        String action = message.replace(sessionId, "register");
        Log.d(TAG, String.format("Received sessionId: %s from: %s", sessionId, uri.getAuthority()));

        StartRegistrationHandler start = new StartRegistrationHandler(app, sessionId, action);
        start.handle((register, request) -> {
            Log.d(TAG, String.format("Received action: %s", register));
            FinishRegistrationHandler finish = new FinishRegistrationHandler(app, request, register);
            finish.handle(() -> startActivityAndFinish(new Intent(getBaseContext(), MainActivity.class)));
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<>();
        if(selectedIndices == null || selectedIndices.isEmpty()) {
            selectedIndices = new ArrayList<>();
            for(int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                selectedIndices.add(i);
            }
        }

        for(int index : selectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if(scannerView != null) {
            scannerView.setFormats(formats);
        }
    }

    private void startActivityAndFinish(Intent intent) {
        startActivity(intent);
        finish();
    }
}
