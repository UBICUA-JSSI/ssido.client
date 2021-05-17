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
 * Defines the valid credential types.
 * <p>
 * It is an extensions point; values may be added to it in the future, as more credential types are defined. The values
 * of this enumeration are used for versioning the Authentication Assertion and attestation structures according to the
 * type of the authenticator.
 * </p>
 * <p>
 * Currently one credential type is defined, namely {@link #PUBLIC_KEY}.
 * </p>
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#enumdef-publickeycredentialtype">§5.10.2. Credential Type Enumeration
 * (enum PublicKeyCredentialType)</a>
 */
@JsonSerialize(using = JsonStringSerializer.class)
public enum PublicKeyCredentialType implements JsonStringSerializable {
    PUBLIC_KEY("public-key");
    @NonNull
    private final String id;

    private static Optional<PublicKeyCredentialType> fromString(@NonNull String id) {
        Optional<PublicKeyCredentialType> result = Optional.absent();
        for(PublicKeyCredentialType item : values()){
            if(item.id.equals(id)){
                result = Optional.of(item);
            }
        }
        return result;
    }

    @JsonCreator
    private static PublicKeyCredentialType fromJsonString(@NonNull String id) {
        Optional<PublicKeyCredentialType> result = fromString(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new IllegalArgumentException(String.format("Unknown %s value: %s", PublicKeyCredentialType.class.getSimpleName(), id));
    }

    @Override
    public String toJsonString() {
        return id;
    }

    PublicKeyCredentialType(@NonNull final String id) {
        this.id = id;
    }
}
