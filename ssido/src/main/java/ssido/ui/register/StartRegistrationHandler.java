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

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ssido.Ssido;
import ssido.model.StartRegistrationRequest;
import ssido.model.User;
import ssido.util.JacksonCodecs;
import ssido.wss.WSClient;
import ssido.wss.WSService;
import ssido.wss.event.Connected;
import ssido.wss.event.Disconnected;
import ssido.wss.event.Event;
import ssido.wss.event.StringMessage;
import jssi.did.Did;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ITON Solutions on 07/10/2019.
 */
public class StartRegistrationHandler {

    private static final String TAG = StartRegistrationHandler.class.getName();

    private Ssido app;
    private String sessionId;
    private String action;

    public StartRegistrationHandler(@NonNull Ssido app, @NonNull String sessionId, @NonNull String action){
        this.app = app;
        this.sessionId = sessionId;
        this.action = action;
    }

    public void handle(StartRegistrationListener listener){

        Request.Builder builder = new Request.Builder()
                .get()
                .url(action);

        OkHttpClient client = new WSClient().getClient();
        WSService ws = new WSService(client, builder.build());

        ws.open().subscribe(new Observer<Event>() {

            StartRegistrationRequest request;

            @Override
            public void onSubscribe(Disposable disposable) {
                Log.d(TAG, "Received SUBSCRIBED event");
            }

            @Override
            public void onNext(Event event) {

                Log.d(TAG, String.format("Received %s" , event.toString()));
                if(event instanceof Connected){

                    Did did = app.getService().getStoreService().getDid();
                    String username = app.getService().getStoreService().getUsername();

                    try {
                        User user = new User(sessionId, username, "Vladimir", did.did);
                        ObjectMapper mapper = JacksonCodecs.json();
                        String json = mapper.writeValueAsString(user);
                        ws.sendMessage(event.sender(), json).subscribe();
                    } catch (JsonProcessingException e) {
                        Log.e(TAG, String.format("Error: %s", e.getMessage()));
                    }

                } else if(event instanceof StringMessage){

                    try {
                        ObjectMapper mapper = JacksonCodecs.json();
                        request = mapper.readValue(((StringMessage) event).message(), StartRegistrationRequest.class);
                        if(request.isSuccess()){
                            event.sender().close(1000, "Start registration request close");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, String.format("Error: %s", e.getMessage()));
                        event.sender().close(2000, e.getMessage());
                    }
                } else if(event instanceof Disconnected){
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, String.format("Error: %s", e.getMessage()));
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Received COMPLETED event");
                if(request != null) {
                    listener.onStart(request.getAction(), request.getRequest());
                }
            }
        });
    }
}
