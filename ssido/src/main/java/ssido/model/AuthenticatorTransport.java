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
 * Authenticators may communicate with Clients using a variety of transports. This enumeration defines a hint as to how
 * Clients might communicate with a particular Authenticator in order to obtain an assertion for a specific credential.
 * Note that these hints represent the Relying Party's best belief as to how an Authenticator may be reached. A Relying
 * Party may obtain a list of transports hints from some attestation statement formats or via some out-of-band
 * mechanism; it is outside the scope of this specification to define that mechanism.
 * <p>
 * Authenticators may implement various transports for communicating with clients. This enumeration defines hints as to
 * how clients might communicate with a particular authenticator in order to obtain an assertion for a specific
 * credential. Note that these hints represent the WebAuthn Relying Party's best belief as to how an authenticator may
 * be reached. A Relying Party may obtain a list of transports hints from some attestation statement formats or via some
 * out-of-band mechanism; it is outside the scope of the Web Authentication specification to define that mechanism.
 * </p>
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#enumdef-authenticatortransport">§5.10.4. Authenticator
 * Transport Enumeration (enum AuthenticatorTransport)</a>
 */
@JsonSerialize(using = JsonStringSerializer.class)
public enum AuthenticatorTransport implements JsonStringSerializable {
    /**
     * Indicates the respective authenticator can be contacted over removable USB.
     */
    USB("usb"),
    /**
     * Indicates the respective authenticator can be contacted over Near Field Communication (NFC).
     */
    NFC("nfc"),
    /**
     * Indicates the respective authenticator can be contacted over Bluetooth Smart (Bluetooth Low Energy / BLE).
     */
    BLE("ble"),
    /**
     * Indicates the respective authenticator is contacted using a client device-specific transport. These
     * authenticators are not removable from the client device.
     */
    INTERNAL("internal");
    @NonNull
    private final String id;

    private static Optional<AuthenticatorTransport> fromString(@NonNull String id) {
        Optional<AuthenticatorTransport> result = Optional.absent();
        for(AuthenticatorTransport item : values()){
            if(item.id.equals(id)){
                result = Optional.of(item);
            }
        }
        return result;
    }

    @JsonCreator
    private static AuthenticatorTransport fromJsonString(@NonNull String id) {
        Optional<AuthenticatorTransport> result = fromString(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new IllegalArgumentException(String.format("Unknown %s value: %s", AuthenticatorTransport.class.getSimpleName(), id));
    }

    @Override
    public String toJsonString() {
        return id;
    }

    AuthenticatorTransport(@NonNull final String id) {
        this.id = id;
    }
}
