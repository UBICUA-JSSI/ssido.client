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
 * A WebAuthn Relying Party may require <a href="https://www.w3.org/TR/webauthn/#user-verification">user
 * verification</a> for some of its operations but not for others, and may use this type to express its needs.
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#enumdef-userverificationrequirement">§5.10.6. User
 * Verification Requirement Enumeration (enum UserVerificationRequirement)</a>
 */
@JsonSerialize(using = JsonStringSerializer.class)
public enum UserVerificationRequirement implements JsonStringSerializable {
    /**
     * This value indicates that the Relying Party does not want user verification employed during the operation (e.g.,
     * in the interest of minimizing disruption to the user interaction flow).
     */
    DISCOURAGED("discouraged"), /**
     * This value indicates that the Relying Party prefers user verification for the operation if possible, but will not
     * fail the operation if the response does not have the {@link AuthenticatorDataFlags#UV} flag set.
     */
    PREFERRED("preferred"), /**
     * Indicates that the Relying Party requires user verification for the operation and will fail the operation if the
     * response does not have the {@link AuthenticatorDataFlags#UV} flag set.
     */
    REQUIRED("required");
    @NonNull
    private final String id;

    private static Optional<UserVerificationRequirement> fromString(@NonNull String id) {
        Optional result = Optional.absent();
        for(UserVerificationRequirement item : values()){
            if(item.id.equals(id)){
                result = Optional.of(item);
            }
        }
        return result;
    }

    @JsonCreator
    private static UserVerificationRequirement fromJsonString(@NonNull String id) {
        Optional<UserVerificationRequirement> result = fromString(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new IllegalArgumentException(String.format("Unknown %s value: %s", UserVerificationRequirement.class.getSimpleName(), id));
    }

    @Override
    public String toJsonString() {
        return id;
    }

    UserVerificationRequirement(@NonNull final String id) {
        this.id = id;
    }
}
