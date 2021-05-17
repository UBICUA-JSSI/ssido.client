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

/**
 * Created by ITON Solutions on 19/09/2019.
 */
public class ServerRequestedCloseException extends IOException {

    private final int code;
    @NonNull
    private final String reason;

    public ServerRequestedCloseException(int code, @NonNull String reason) {
        super(String.format("Server requested connection to close, code: %d, reason: %s", code, reason));
        this.code = code;
        this.reason = reason;
    }

    /**
     * Code why close requested
     * @return code why close requested
     */
    public int code() {
        return code;
    }

    /**
     * Reason why close requested
     * @return reason why close requested or empty string
     */
    @NonNull
    public String reason() {
        return reason;
    }

}
