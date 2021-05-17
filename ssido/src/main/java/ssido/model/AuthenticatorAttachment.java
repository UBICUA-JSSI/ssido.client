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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;

import ssido.util.JsonStringSerializable;
import ssido.util.JsonStringSerializer;

/**
 * This enumeration’s values describe authenticators' <a href="https://www.w3.org/TR/webauthn/#authenticator-attachment-modality">attachment
 * modalities</a>. Relying Parties use this for two purposes:
 *
 * <ul>
 * <li>
 * to express a preferred authenticator attachment modality when calling <code>navigator.credentials.create()</code> to
 * create a credential, and
 * </li>
 *
 * <li>
 * to inform the client of the Relying Party's best belief about how to locate the managing authenticators of the
 * credentials listed in {@link PublicKeyCredentialRequestOptions#allowCredentials} when calling
 * <code>navigator.credentials.get()</code>.
 * </li>
 * </ul>
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#enumdef-authenticatorattachment">§5.4.5. Authenticator
 * Attachment Enumeration (enum AuthenticatorAttachment)
 * </a>
 */
@JsonSerialize(using = JsonStringSerializer.class)
public enum AuthenticatorAttachment implements JsonStringSerializable {
    /**
     * Indicates <a href="https://www.w3.org/TR/webauthn/#cross-platform-attachment">cross-platform
     * attachment</a>.
     * <p>
     * Authenticators of this class are removable from, and can "roam" among, client platforms.
     * </p>
     */
    CROSS_PLATFORM("cross-platform"), /**
     * Indicates <a href="https://www.w3.org/TR/webauthn/#platform-attachment">platform
     * attachment</a>.
     * <p>
     * Usually, authenticators of this class are not removable from the platform.
     * </p>
     */
    PLATFORM("platform");
    @NonNull
    private final String id;


    private static Optional<AuthenticatorAttachment> fromString(@NonNull String id) {
        Optional<AuthenticatorAttachment> result = Optional.absent();
        for(AuthenticatorAttachment item : values()){
            if(item.id.equals(id)){
                result = Optional.of(item);
            }
        }
        return result;
    }

    @JsonCreator
    private static AuthenticatorAttachment fromJsonString(@NonNull String id) {
        Optional<AuthenticatorAttachment> result = fromString(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new IllegalArgumentException(String.format("Unknown %s value: %s", AuthenticatorAttachment.class.getSimpleName(), id));
    }

    @Override
    public String toJsonString() {
        return id;
    }

    AuthenticatorAttachment(@NonNull final String id) {
        this.id = id;
    }
}
