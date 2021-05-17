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

package ssido.http;

/**
 * Created by ITON Solutions on 19/09/2019.
 */

import android.util.Log;

import androidx.annotation.NonNull;

import ssido.http.event.Event;
import ssido.http.event.Failure;
import ssido.http.event.Result;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * This class allows to retrieve messages from websocket
 */
public class HTTPService {

    private static final String TAG = HTTPService.class.getName();

    @NonNull
    private final OkHttpClient client;
    @NonNull
    private final Request request;

    /**
     * Create instance of {@link HTTPService}
     *
     * @param client  {@link OkHttpClient} instance
     * @param request request to connect to websocket
     */
    public HTTPService(@NonNull OkHttpClient client, @NonNull Request request) {
        this.client = client;
        this.request = request;
    }

    /**
     * Returns observable that connected to a http socket and returns {@link Event}'s
     *
     * @return Observable that connects to http socket
     */
    @NonNull
    public Observable<Event> send() {
        return Observable.create(new ObservableOnSubscribe<Event>() {

            @Override
            public void subscribe(final ObservableEmitter<Event> emitter) {

                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response){
                        if(response.isSuccessful()) {
                            try {
                                emitter.onNext(new Result(call, response.body().string()));
                            } catch(IOException e){}
                        } else {
                            emitter.onNext(new Failure(call, new IOException(String.format("Unexpected error %d", response.code()))));
                        }
                        emitter.onComplete();
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        emitter.onNext(new Failure(call, e));
                        emitter.onComplete();
                    }
                });
            }
        });
    }

    /**
     * Enqueue message to send
     *
     * @param sender  connection event that is used to send message
     * @param message message to send
     * @return Single that returns true if message was enqueued
     */
    @NonNull
    public Single<Boolean> sendMessage(final @NonNull WebSocket sender, final @NonNull String message) {
        return Single.fromCallable(() -> {
            Log.d(TAG, String.format("Send message: %s", message));
            return sender.send(message);
        });
    }
}

