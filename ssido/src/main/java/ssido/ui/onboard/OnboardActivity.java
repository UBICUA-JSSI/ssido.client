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

package ssido.ui.onboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import ssido.R;
import ssido.Ssido;
import ssido.service.SsidoService;
import ssido.ui.register.RegisterActivity;
import jssi.did.Did;
import jssi.wallet.record.WalletRecord;
import org.libsodium.jni.SodiumException;

import java.io.IOException;

public class OnboardActivity extends AppCompatActivity {

    private static final String TAG = OnboardActivity.class.getName();

    private Button done;
    private EditText username;
    private EditText display_name;
    private Ssido app;
    private SsidoService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Ssido) getApplication();
        service  = app.getService();
        setContentView(R.layout.activity_onboard);
        username = findViewById(R.id.username);
        display_name = findViewById(R.id.display_name);
        done = findViewById(R.id.button);
        done.setOnClickListener(new OnDoneListener());
    }

    class OnDoneListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            service.getStoreService().setUsername(username.getText().toString());

            try {
                WalletRecord record = service.getWalletService().getWallet().findRecord(Did.TYPE, "EjABoD8BV1mxQhfTccCKw4");
                Did did = new ObjectMapper().readValue(record.getValue(), Did.class);
                service.getStoreService().setDid(did);
                Log.d(TAG, String.format("Store Did: {did: %s, verkey: %s}", did.did, did.verkey));
                startActivityAndFinish(new Intent(getBaseContext(), RegisterActivity.class));
            } catch (SodiumException | IOException e){
                Log.e(TAG, String.format("Error: %s", e.getCause().getMessage()));
            }
        }
    }

    private void startActivityAndFinish(Intent intent) {
        startActivity(intent);
        finish();
    }
}
