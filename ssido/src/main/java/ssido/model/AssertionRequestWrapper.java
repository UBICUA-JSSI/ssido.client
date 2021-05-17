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
package ssido.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;

import ssido.util.ByteArray;


public final class AssertionRequestWrapper {
    @NonNull
    private final ByteArray requestId;
    @NonNull
    private final PublicKeyCredentialRequestOptions publicKeyCredentialRequestOptions;
    @NonNull
    private final Optional<String> username;
    @NonNull
    @JsonIgnore
    private final transient AssertionRequest request;

    @JsonCreator
    public AssertionRequestWrapper(
            @JsonProperty("requestId") @NonNull ByteArray requestId,
            @JsonProperty("request") @NonNull AssertionRequest request) {
        this.requestId = requestId;
        this.publicKeyCredentialRequestOptions = request.getPublicKeyCredentialRequestOptions();
        this.username = request.getUsername();
        this.request = request;
    }

    @NonNull
    public ByteArray getRequestId() {
        return this.requestId;
    }

    @NonNull
    public PublicKeyCredentialRequestOptions getPublicKeyCredentialRequestOptions() {
        return this.publicKeyCredentialRequestOptions;
    }

    @NonNull
    public Optional<String> getUsername() {
        return this.username;
    }

    @NonNull
    public AssertionRequest getRequest() {
        return this.request;
    }
}
