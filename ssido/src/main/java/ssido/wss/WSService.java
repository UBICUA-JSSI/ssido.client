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

package ssido.wss;

/**
 * Created by ITON Solutions on 19/09/2019.
 */

import android.util.Log;

import androidx.annotation.NonNull;

import ssido.wss.event.BinaryMessage;
import ssido.wss.event.Connected;
import ssido.wss.event.Disconnected;
import ssido.wss.event.Event;
import ssido.wss.event.StringMessage;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * This class allows to retrieve messages from websocket
 */
public class WSService {

    private static final String TAG = WSService.class.getName();

    @NonNull
    private final OkHttpClient client;
    @NonNull
    private final Request request;

    /**
     * Create instance of {@link WSService}
     *
     * @param client  {@link OkHttpClient} instance
     * @param request request to connect to websocket
     */
    public WSService(@NonNull OkHttpClient client, @NonNull Request request) {
        this.client = client;
        this.request = request;
    }

    /**
     * Returns observable that connected to a websocket and returns {@link Event}'s
     *
     * @return Observable that connects to websocket
     */
    @NonNull
    public Observable<Event> open() {
        return Observable.create(new ObservableOnSubscribe<Event>() {

            @Override
            public void subscribe(final ObservableEmitter<Event> emitter) {

                client.newWebSocket(request, new WebSocketListener() {
                    @Override
                    public void onOpen(@NonNull WebSocket sender, @NonNull Response response) {
                        emitter.onNext(new Connected(sender));
                    }

                    @Override
                    public void onMessage(@NonNull WebSocket sender, @NonNull String message) {
                        emitter.onNext(new StringMessage(sender, message));
                    }

                    @Override
                    public void onMessage(@NonNull WebSocket sender, @NonNull ByteString bytes) {
                        emitter.onNext(new BinaryMessage(sender, bytes.toByteArray()));
                    }

                    @Override
                    public void onClosing(@NonNull WebSocket sender, int code, @NonNull String reason) {
                        super.onClosing(sender, code, reason);
                        final ServerRequestedCloseException exception = new ServerRequestedCloseException(code, reason);
                        emitter.onNext(new Disconnected(sender, exception));
                        emitter.onComplete();
                    }

                    @Override
                    public void onClosed(@NonNull WebSocket sender, int code, @NonNull String reason) {
                        final ServerRequestedCloseException exception = new ServerRequestedCloseException(code, reason);
                        emitter.onNext(new Disconnected(sender, exception));
                        emitter.onComplete();
                    }

                    @Override
                    public void onFailure(@NonNull WebSocket sender, @NonNull Throwable throwable, Response response) {
                        if (response != null) {
                            final ServerHttpException exception = new ServerHttpException(response);
                            emitter.onNext(new Disconnected(sender, exception));
                        } else {
                            emitter.onNext(new Disconnected(sender, throwable));
                        }
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

