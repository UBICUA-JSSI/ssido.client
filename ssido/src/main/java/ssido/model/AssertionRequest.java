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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;

import ssido.util.ByteArray;

/**
 * A combination of a {@link PublicKeyCredentialRequestOptions} and, optionally, a {@link #getUsername() username}.
 */
public final class AssertionRequest {
    /**
     * An object that can be serialized to JSON and passed as the <code>publicKey</code> argument to
     * <code>navigator.credentials.get()</code>.
     */
    @NonNull
    private final PublicKeyCredentialRequestOptions publicKeyCredentialRequestOptions;
    /**
     * The username of the user to authenticate, if the user has already been identified.
     * <p>
     * If this is absent, this indicates that this is a request for an assertion by a <a
     * href="https://www.w3.org/TR/webauthn/#client-side-resident-public-key-credential-source">client-side-resident
     * credential</a>, and identification of the user has been deferred until the response is received.
     * </p>
     */
    @NonNull
    private final Optional<String> username;
    private final ByteArray requestId;

    @JsonCreator
    private AssertionRequest(
            @JsonProperty("requestId") @NonNull ByteArray requestId,
            @JsonProperty("publicKeyCredentialRequestOptions") @NonNull PublicKeyCredentialRequestOptions publicKeyCredentialRequestOptions,
            @JsonProperty("username") String username) {
        this.requestId = requestId;
        this.username = Optional.of(username);
        this.publicKeyCredentialRequestOptions = publicKeyCredentialRequestOptions;

    }

    /**
     * An object that can be serialized to JSON and passed as the <code>publicKey</code> argument to

     * <code>navigator.credentials.get()</code>.
     * @return 
     */
    @NonNull
    public PublicKeyCredentialRequestOptions getPublicKeyCredentialRequestOptions() {
        return publicKeyCredentialRequestOptions;
    }

    public ByteArray getRequestId() {
        return requestId;
    }

    /**
     * The username of the user to authenticate, if the user has already been identified.
     * <p>
     * If this is absent, this indicates that this is a request for an assertion by a <a
     * href="https://www.w3.org/TR/webauthn/#client-side-resident-public-key-credential-source">client-side-resident
     * credential</a>, and identification of the user has been deferred until the response is received.
     * </p>
     * @return
     */
    @NonNull
    public Optional<String> getUsername() {
        return this.username;
    }
}
