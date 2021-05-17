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

package ssido;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;

import ssido.service.SsidoBinder;
import ssido.service.SsidoService;
import ssido.util.SsidoUtil;

/**
 * Created by ITON Solutions on 26/09/2019.
 */
public class Ssido extends Application {

    private static final String TAG = Application.class.getName();

    private static Application app;
    private SsidoService service;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Log.d(TAG, "Create Ssido application");

        Intent intent = new Intent(this, SsidoService.class);
        bindService(intent, new ServiceListener(), Context.BIND_AUTO_CREATE);
    }

    private class ServiceListener implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            Log.d(TAG,"Ssido service started");
            service = ((SsidoBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG,"Ssido service destroyed");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        try {
            SsidoUtil.checkDisplaySize(getApplicationContext(), config);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public SsidoService getService() {
        return service;
    }
}
