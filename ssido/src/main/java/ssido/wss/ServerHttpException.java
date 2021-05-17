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

import androidx.annotation.NonNull;
import java.io.IOException;
import okhttp3.Response;

/**
 * Created by ITON Solutions on 19/09/2019.
 */
public class ServerHttpException extends IOException {
    @NonNull
    private final Response response;

    public ServerHttpException(@NonNull Response response) {
        super(String.format("Http server error code: %d message: %s", response.code(), response.message()));
        this.response = response;
    }

    /**
     * Response from server
     * @return response why connection couldn't be established
     */
    @NonNull
    public Response response() {
        return response;
    }

}
