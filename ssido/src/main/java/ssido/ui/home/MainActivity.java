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

package ssido.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ssido.R;
import ssido.Ssido;
import ssido.core.DeleteRegistrationHandler;
import ssido.core.DeleteRegistrationListener;
import ssido.service.SsidoService;
import ssido.ui.authenticate.AuthenticateActivity;
import ssido.ui.register.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private Button delete;
    private Button register;
    private Button authenticate;
    private Ssido app;
    private SsidoService service;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        app = (Ssido) getApplication();
        service  = app.getService();

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new OnDeleteListener());

        register = findViewById(R.id.register);
        register.setOnClickListener(new OnRegisterListener());

        authenticate = findViewById(R.id.authenticate);
        authenticate.setOnClickListener(new OnAuthenticateListener());


    }

    class OnDeleteListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            DeleteRegistrationHandler handler = new DeleteRegistrationHandler(app,
                    "vladimir",
                    "https://192.168.1.32:8443/webauthn/api/v1/delete-account");
            handler.handle(new DeleteRegistrationListener() {
                @Override
                public void onDelete(String response) {
                    Log.d(TAG, String.format("Response: %s", response));
                }
            });
        }
    }

    class OnRegisterListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            startActivityAndFinish(new Intent(getBaseContext(), RegisterActivity.class));
        }
    }

    class OnAuthenticateListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            startActivityAndFinish(new Intent(getBaseContext(), AuthenticateActivity.class));
        }
    }

    private void startActivityAndFinish(Intent intent) {
        startActivity(intent);
        finish();
    }
}
