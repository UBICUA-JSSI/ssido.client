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

package ssido.ui.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import ssido.R;
import ssido.Ssido;
import ssido.service.SsidoService;
import ssido.store.StoreService;
import ssido.ui.home.MainActivity;
import ssido.ui.onboard.OnboardActivity;
import jssi.crypto.Keys;
import jssi.did.Did;
import jssi.wallet.record.WalletRecord;
import org.libsodium.jni.SodiumException;

import java.io.IOException;

public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = LauncherActivity.class.getName();

    public static final String BROADCAST_ACTION = "WALLET_SERVICE";
    private ProgressBar progress;
    private ServiceBroadCastReceiver broadcast;
    private IntentFilter filter;
    private Ssido app = null;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        app = (Ssido) getApplication();
        setContentView(R.layout.activity_launcher);
        progress = findViewById(R.id.progress);
        broadcast = new ServiceBroadCastReceiver();
        filter = new IntentFilter();
        filter.addAction(BROADCAST_ACTION);
    }

    private class ServiceBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Wallet service started");
            progress.setVisibility(View.INVISIBLE);

            try {

                SsidoService service  = app.getService();
                StoreService store = service.getStoreService();
                if(store.isFirstLaunch()){
                    startActivityAndFinish(new Intent(getBaseContext(), OnboardActivity.class));
                } else {
                    Did did = store.getDid();
                    WalletRecord record = service.getWalletService().getWallet().findRecord(Keys.TYPE, did.verkey);
                    Keys keys = new ObjectMapper().readValue(record.getValue(), Keys.class);
                    Log.d(TAG, String.format("Keys: verkey: %s signkey: %s", keys.verkey, keys.signkey));
//                    startActivityAndFinish(new Intent(getBaseContext(), QRCodeActivity.class));
                    startActivityAndFinish(new Intent(getBaseContext(), MainActivity.class));
                }

            } catch(SodiumException | IOException e){
                Log.e(TAG, String.format("Error: %s", e.getCause().getMessage()));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcast, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast);
    }

    private void startActivityAndFinish(Intent intent) {
        startActivity(intent);
        finish();
    }
}
