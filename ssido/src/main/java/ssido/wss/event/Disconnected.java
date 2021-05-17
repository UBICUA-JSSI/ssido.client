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

package ssido.wss.event;

import androidx.annotation.NonNull;

import okhttp3.WebSocket;

/**
 * Created by ITON Solutions on 19/09/2019.
 */
public class Disconnected extends Event {

    @NonNull
    private Throwable exception;

    public Disconnected(@NonNull WebSocket sender, @NonNull Throwable exception) {
        super(sender);
        this.exception = exception;
    }

    /**
     * Exception that caused disconnection.
     * If server requested disconnection it will be {@link ssido.wss.ServerRequestedCloseException}
     * If server returned incorrect initialization response it will be {@link ssido.wss.ServerHttpException}
     * If other unknown exception
     *
     * @return exception that caused disconnection
     */
    @NonNull
    public Throwable exception() {
        return exception;
    }

    @Override
    public String toString() {
        return String.format("DISCONNECTED event: %s", exception.getMessage());
    }
}
