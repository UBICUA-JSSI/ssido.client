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

package ssido.core;

import android.util.Log;

import androidx.annotation.NonNull;

import ssido.Ssido;
import ssido.http.HTTPClient;
import ssido.http.HTTPService;
import ssido.http.event.Event;
import ssido.http.event.Failure;
import ssido.http.event.Result;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ITON Solutions on 07/10/2019.
 */
public class DeleteRegistrationHandler {

    private static final String TAG = DeleteRegistrationHandler.class.getName();

    private Ssido app;
    private String username;
    private String action;

    public DeleteRegistrationHandler(@NonNull Ssido app, @NonNull String username, @NonNull String action){
        this.app = app;
        this.username = username;
        this.action = action;
    }

    public void handle(DeleteRegistrationListener listener){

        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .build();

        Request.Builder builder = new Request.Builder()
                .url(action)
                .post(body);

        OkHttpClient client = new HTTPClient().getClient();
        HTTPService http = new HTTPService(client, builder.build());

        http.send().subscribe(new Observer<Event>() {

            String response;

            @Override
            public void onSubscribe(Disposable disposable) {
                Log.d(TAG, "Received SUBSCRIBED event");
            }

            @Override
            public void onNext(Event event) {

                Log.d(TAG, String.format("Received %s" , event.toString()));
                if(event instanceof Result){
                    response = ((Result) event).response();
                } else if(event instanceof Failure){
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, String.format("Error: %s", e.getMessage()));
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Received COMPLETED event");
                if(response != null) {
                    listener.onDelete(response);
                }
            }
        });
    }
}
