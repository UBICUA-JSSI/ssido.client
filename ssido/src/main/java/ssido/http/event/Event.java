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

package ssido.http.event;

import androidx.annotation.NonNull;

import okhttp3.Call;

/**
 * Created by ITON Solutions on 19/09/2019.
 */
public abstract class Event {

    @NonNull
    final Call call;

    public Event(@NonNull Call call) {
        this.call = call;
    }

    /**
     * A call is a request that has been prepared for execution. A call can be canceled. As this object
     * represents a single request/response pair (stream), it cannot be executed twice.
     */
    @NonNull
    public Call call() {
        return call;
    }
}
