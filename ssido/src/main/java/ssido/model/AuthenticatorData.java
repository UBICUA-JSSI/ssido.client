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

import android.util.Base64;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.upokecenter.cbor.CBORObject;

import ssido.util.BinaryUtil;
import org.libsodium.api.Crypto_hash_sha256;
import org.libsodium.jni.SodiumException;

import java.io.IOException;
import com.google.common.base.Optional;

/**
 * The authenticator data structure is a byte array of 37 bytes or more. This
 * class presents the authenticator data decoded as a high-level object.
 *
 * <p>
 * The authenticator data structure encodes contextual bindings made by the
 * authenticator. These bindings are controlled by the authenticator itself, and
 * derive their trust from the WebAuthn Relying Party's assessment of the
 * security properties of the authenticator. In one extreme case, the
 * authenticator may be embedded in the client, and its bindings may be no more
 * trustworthy than the client data. At the other extreme, the authenticator may
 * be a discrete entity with high-security hardware and software, connected to
 * the client over a secure channel. In both cases, the Relying Party receives
 * the authenticator data in the same format, and uses its knowledge of the
 * authenticator to make trust decisions.
 * </p>
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#sec-authenticator-data">§6.1.
 * Authenticator Data</a>
 */

@JsonSerialize(using = AuthenticatorData.JsonSerializer.class)
public final class AuthenticatorData {

    /**
     * The original raw byte array that this object is decoded from. This is a
     * byte array of 37 bytes or more.
     *
     * @see
     * <a href="https://www.w3.org/TR/webauthn/#sec-authenticator-data">§6.1.
     * Authenticator Data</a>
     */
    @NonNull
    private final byte[] rpId;
    /**
     * The 32-bit unsigned signature counter.
     *
     * @return
     */
    private byte[] counter;

    /**
     * The flags bit field.
     */
    @NonNull
    private transient AuthenticatorDataFlags flags;
    /**
     * Attested credential data, if present.
     *
     * <p>
     * This member is present if and only if the
     * {@link AuthenticatorDataFlags#AT} flag is set.
     * </p>
     *
     * @see #flags
     */
    @NonNull
    private transient AttestedCredentialData attestedCredentialData;
    
    @NonNull
    private Optional<CBORObject> extensions;

    private AuthenticatorData(@NonNull byte[] rpId, @NonNull AttestedCredentialData attestedCredentialData) {
        this.rpId = rpId;
        this.flags = AuthenticatorDataFlags.builder().build();
        this.counter = BinaryUtil.encodeUint32(0);
        this.attestedCredentialData = attestedCredentialData;
        this.extensions = Optional.absent();
    }

    public static AuthenticatorDataBuilder builder() {
        return new AuthenticatorDataBuilder();
    }

    public static class AuthenticatorDataBuilder {

        private byte[] rpId;
        private AttestedCredentialData attestedCredentialData;

        AuthenticatorDataBuilder() {}

        /**
         * The rpId of the replying party.
         *
         * @param rpId
         * @return
         */
        public AuthenticatorDataBuilder rpId(@NonNull final byte[] rpId) {
            this.rpId = rpId;
            return this;
        }

        /**
         * The AttestedCredentialData of the attested credential.
         *
         * @param attestedCredentialData
         * @return
         */
        public AuthenticatorDataBuilder attestedCredentialData(@NonNull final AttestedCredentialData attestedCredentialData) {
            this.attestedCredentialData = attestedCredentialData;
            return this;
        }

        public AuthenticatorData build() {
            return new AuthenticatorData(rpId, attestedCredentialData);
        }
    }

    /**
     * The SHA-256 hash of the RP ID the credential is scoped to.
     *
     * @return
     */
    public byte[] getRpIdHash() {
        try {
            return Crypto_hash_sha256.sha256(rpId);
        } catch(SodiumException e){
            return new byte[0];
        }
    }

    /**
     * The 32-bit unsigned signature counter.
     *
     * @return
     */
    public int getCounter() {
        return BinaryUtil.getUint32(counter);
    }
    
    public void setCounter(int counter){
        this.counter = BinaryUtil.encodeUint32(counter);
    }

    /**
     * Extension-defined authenticator data, if present.
     *
     * <p>
     * This member is present if and only if the
     * {@link AuthenticatorDataFlags#ED} flag is set.
     * </p>
     *
     * <p>
     * Changes to the returned value are not reflected in the
     * {@link AuthenticatorData} object.
     * </p>
     *
     * @return
     * @see #flags
     */
    public Optional<CBORObject> getExtensions() {
        return extensions;
    }
    
    public void setExtensions(Optional<CBORObject> extensions){
        this.extensions = extensions;
    }
    
    static class JsonSerializer extends com.fasterxml.jackson.databind.JsonSerializer<AuthenticatorData> {
        @Override
        public void serialize(AuthenticatorData value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(Base64.encodeToString(value.getBytes(), Base64.URL_SAFE | Base64.NO_PADDING));

        }
    }

    /**
     * The original raw byte array that this object is decoded from. This is a
     * byte array of 37 bytes or more.
     *
     * @return
     * @see
     * <a href="https://www.w3.org/TR/webauthn/#sec-authenticator-data">§6.1.
     * Authenticator Data</a>
     */
    @NonNull
    public byte[] getBytes() {

        byte[] result = BinaryUtil.concat(
                getRpIdHash(),
                getFlags().getBytes(),
                counter);

        if (attestedCredentialData != null) {
            result = BinaryUtil.concat(result, attestedCredentialData.getBytes());
        }
        if (extensions.isPresent()) {
            result = BinaryUtil.concat(result, extensions.get().EncodeToBytes());
        }
        return result;
    }

    /**
     * The flags bit field.
     *
     * @return
     */
    @NonNull
    public AuthenticatorDataFlags getFlags() {
        return flags;
    }
    public void setFlags(AuthenticatorDataFlags flags){
        this.flags = flags;
    }

    /**
     * Attested credential data, if present.
     *
     * <p>
     * This member is present if and only if the
     * {@link AuthenticatorDataFlags#AT} flag is set.
     * </p>
     *
     * @return
     * @see #flags
     */
    @NonNull
    public AttestedCredentialData getAttestedCredentialData() {
        return attestedCredentialData;
    }
    
    
}
