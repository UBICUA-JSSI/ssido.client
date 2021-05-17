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

package ssido.ui.authenticate;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.bitcoinj.core.Base58;
import ssido.Ssido;
import ssido.model.AssertionRequest;
import ssido.ui.register.FinishRegistrationListener;
import ssido.wss.WSClient;
import ssido.wss.WSService;
import ssido.wss.event.Connected;
import ssido.wss.event.Event;
import jssi.crypto.Keys;
import jssi.did.Did;
import jssi.wallet.record.WalletRecord;
import org.libsodium.jni.SodiumException;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ITON Solutions on 07/10/2019.
 */
public class FinishAuthenticationHandler {

    private static final String TAG = FinishAuthenticationHandler.class.getName();

    private AssertionRequest request;
    private Ssido app;
    private String action;


    public FinishAuthenticationHandler(@NonNull Ssido app, @NonNull AssertionRequest request, @NonNull String action){
        this.app = app;
        this.request = request;
        this.action = action;
    }

    public void handle(FinishRegistrationListener listener) {

        Request.Builder builder = new Request.Builder()
                .get()
                .url(action);


        OkHttpClient client = new WSClient().getClient();
        WSService ws = new WSService(client, builder.build());

        ws.open().subscribe(new Observer<Event>() {

            String origin = action.substring(0, action.lastIndexOf("/"));

            @Override
            public void onSubscribe(Disposable disposable) {
                Log.d(TAG, "Received SUBSCRIBED event");
            }

            @Override
            public void onNext(Event event) {

                Log.d(TAG, String.format("Received %s", event.toString()));
                if (event instanceof Connected) {
                    try {
                        AuthenticationResponse response = new AuthenticationResponse(app, request);
                        Did did = app.getService().getStoreService().getDid();
                        WalletRecord record = app.getService().getWalletService().getWallet().findRecord(Keys.TYPE, did.verkey);
                        Keys keys = new ObjectMapper().readValue(record.getValue(), Keys.class);
                        Log.d(TAG, String.format("Keys: verkey: %s signkey: %s", keys.verkey, keys.signkey));
                        String json = response.finish(Base58.decode(keys.verkey), Base58.decode(keys.signkey), origin);
                        ws.sendMessage(event.sender(), json).subscribe();
                        event.sender().close(1000, "Finish authentication request close");
                    } catch (SodiumException | IOException e) {
                        Log.e(TAG, String.format("Error: %s", e.getCause().getMessage()));
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Received COMPLETED event");
                listener.onFinish();
            }
        });
    }
}
