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
import ssido.util.CollectionUtil;

import java.util.List;

/**
 * The PublicKeyCredentialRequestOptions dictionary supplies get() with the data it needs to generate an assertion.
 * <p>
 * Its challenge member must be present, while its other members are optional.
 * </p>
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#dictdef-publickeycredentialrequestoptions">§5.5.
 * Options for Assertion Generation (dictionary PublicKeyCredentialRequestOptions)
 * </a>
 */
public final class PublicKeyCredentialRequestOptions {
    /**
     * A challenge that the selected authenticator signs, along with other data, when producing an authentication
     * assertion. See the <a href="https://www.w3.org/TR/webauthn/#cryptographic-challenges">§13.1
     * Cryptographic Challenges</a> security consideration.
     */
    @NonNull
    private final ByteArray challenge;
    /**
     * Specifies a time, in milliseconds, that the caller is willing to wait for the call to complete.
     * <p>
     * This is treated as a hint, and MAY be overridden by the client.
     * </p>
     */
    @NonNull
    private final Optional<Long> timeout;
    /**
     * Specifies the relying party identifier claimed by the caller.
     * <p>
     * If omitted, its value will be set by the client.
     * </p>
     */
    @NonNull
    private final Optional<String> rpId;
    /**
     * A list of {@link PublicKeyCredentialDescriptor} objects representing public key credentials acceptable to the
     * caller, in descending order of the caller’s preference (the first item in the list is the most preferred
     * credential, and so on down the list).
     */
    @NonNull
    private final Optional<List<PublicKeyCredentialDescriptor>> allowCredentials;
    /**
     * Describes the Relying Party's requirements regarding <a href="https://www.w3.org/TR/webauthn/#user-verification">user
     * verification</a> for the <code>navigator.credentials.get()</code> operation.
     * <p>
     * Eligible authenticators are filtered to only those capable of satisfying this requirement.
     * </p>
     */
    @NonNull
    private final UserVerificationRequirement userVerification;
    /**
     * Additional parameters requesting additional processing by the client and authenticator.
     * <p>
     * For example, if transaction confirmation is sought from the user, then the prompt string might be included as an
     * extension.
     * </p>
     */
    @NonNull
    private final AssertionExtensionInputs extensions;

    private PublicKeyCredentialRequestOptions(@NonNull ByteArray challenge,
                                              @NonNull Optional<Long> timeout,
                                              @NonNull Optional<String> rpId,
                                              @NonNull Optional<List<PublicKeyCredentialDescriptor>> allowCredentials,
                                              @NonNull UserVerificationRequirement userVerification,
                                              @NonNull AssertionExtensionInputs extensions) {
        this.challenge = challenge;
        this.timeout = timeout;
        this.rpId = rpId;
        this.allowCredentials = allowCredentials.isPresent() ? Optional.of(CollectionUtil.immutableList(allowCredentials.get())) : Optional.absent();
        this.userVerification = userVerification;
        this.extensions = extensions;
    }

    @JsonCreator
    private PublicKeyCredentialRequestOptions(
            @NonNull @JsonProperty("challenge") ByteArray challenge,
            @JsonProperty("timeout") Long timeout,
            @JsonProperty("rpId") String rpId,
            @JsonProperty("allowCredentials") List<PublicKeyCredentialDescriptor> allowCredentials,
            @NonNull @JsonProperty("userVerification") UserVerificationRequirement userVerification,
            @JsonProperty("extensions") AssertionExtensionInputs extensions) {

        this(
                challenge,
                Optional.fromNullable(timeout),
                Optional.fromNullable(rpId),
                Optional.fromNullable(allowCredentials),
                userVerification,
                Optional.fromNullable(extensions).or(AssertionExtensionInputs.builder().build()));
    }

    public static PublicKeyCredentialRequestOptionsBuilder.MandatoryStages builder() {
        return new PublicKeyCredentialRequestOptionsBuilder.MandatoryStages();
    }


    public static class PublicKeyCredentialRequestOptionsBuilder {
        private ByteArray challenge;
        private UserVerificationRequirement userVerification = UserVerificationRequirement.PREFERRED;
        private AssertionExtensionInputs extensions = AssertionExtensionInputs.builder().build();
        @NonNull
        private Optional<Long> timeout = Optional.absent();
        @NonNull
        private Optional<String> rpId = Optional.absent();
        @NonNull
        private Optional<List<PublicKeyCredentialDescriptor>> allowCredentials = Optional.absent();


        public static class MandatoryStages {
            private PublicKeyCredentialRequestOptionsBuilder builder = new PublicKeyCredentialRequestOptionsBuilder();

            public PublicKeyCredentialRequestOptionsBuilder challenge(ByteArray challenge) {
                return builder.challenge(challenge);
            }
        }

        /**
         * Specifies a time, in milliseconds, that the caller is willing to wait for the call to complete.
         * <p>
         * This is treated as a hint, and MAY be overridden by the client.
         * </p>
         * @param timeout
         * @return 
         */
        public PublicKeyCredentialRequestOptionsBuilder timeout(@NonNull Optional<Long> timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Specifies a time, in milliseconds, that the caller is willing to wait for the call to complete.
         * <p>
         * This is treated as a hint, and MAY be overridden by the client.
         * </p>
         * @param timeout
         * @return 
         */
        public PublicKeyCredentialRequestOptionsBuilder timeout(long timeout) {
            return this.timeout(Optional.of(timeout));
        }

        /**
         * Specifies the relying party identifier claimed by the caller.
         * <p>
         * If omitted, its value will be set by the client.
         * </p>
         * @param rpId
         * @return 
         */
        public PublicKeyCredentialRequestOptionsBuilder rpId(@NonNull Optional<String> rpId) {
            this.rpId = rpId;
            return this;
        }

        /**
         * Specifies the relying party identifier claimed by the caller.
         * <p>
         * If omitted, its value will be set by the client.
         * </p>
         * @param rpId
         * @return 
         */
        public PublicKeyCredentialRequestOptionsBuilder rpId(@NonNull String rpId) {
            return this.rpId(Optional.of(rpId));
        }

        /**
         * A list of {@link PublicKeyCredentialDescriptor} objects representing public key credentials acceptable to the
         * caller, in descending order of the caller’s preference (the first item in the list is the most preferred
         * credential, and so on down the list).
         * @param allowCredentials
         * @return 
         */
        public PublicKeyCredentialRequestOptionsBuilder allowCredentials(@NonNull Optional<List<PublicKeyCredentialDescriptor>> allowCredentials) {
            this.allowCredentials = allowCredentials;
            return this;
        }

        /**
         * A list of {@link PublicKeyCredentialDescriptor} objects representing public key credentials acceptable to the
         * caller, in descending order of the caller’s preference (the first item in the list is the most preferred
         * credential, and so on down the list).
         * @param allowCredentials
         * @return 
         */
        public PublicKeyCredentialRequestOptionsBuilder allowCredentials(@NonNull List<PublicKeyCredentialDescriptor> allowCredentials) {
            return this.allowCredentials(Optional.of(allowCredentials));
        }

        PublicKeyCredentialRequestOptionsBuilder() {
        }

        /**
         * A challenge that the selected authenticator signs, along with other data, when producing an authentication

         * assertion. See the <a href="https://www.w3.org/TR/webauthn/#cryptographic-challenges">§13.1
         * Cryptographic Challenges</a> security consideration.
         * @param challenge
         * @return 
         */
        public PublicKeyCredentialRequestOptionsBuilder challenge(@NonNull final ByteArray challenge) {
            this.challenge = challenge;
            return this;
        }

        /**
         * Describes the Relying Party's requirements regarding <a href="https://www.w3.org/TR/webauthn/#user-verification">user
         * verification</a> for the <code>navigator.credentials.get()</code> operation.
         * <p>
         * Eligible authenticators are filtered to only those capable of satisfying this requirement.
         * </p>
         * @param userVerification
         * @return 
         */
        public PublicKeyCredentialRequestOptionsBuilder userVerification(@NonNull final UserVerificationRequirement userVerification) {
            this.userVerification = userVerification;
            return this;
        }

        /**
         * Additional parameters requesting additional processing by the client and authenticator.
         * <p>
         * For example, if transaction confirmation is sought from the user, then the prompt string might be included as an
         * extension.
         * </p>
         * @param extensions
         * @return 
         */
        public PublicKeyCredentialRequestOptionsBuilder extensions(@NonNull final AssertionExtensionInputs extensions) {
            this.extensions = extensions;
            return this;
        }

        public PublicKeyCredentialRequestOptions build() {
            return new PublicKeyCredentialRequestOptions(challenge, timeout, rpId, allowCredentials, userVerification, extensions);
        }
    }

    public PublicKeyCredentialRequestOptionsBuilder toBuilder() {
        return new PublicKeyCredentialRequestOptionsBuilder()
                .challenge(this.challenge)
                .timeout(this.timeout)
                .rpId(this.rpId)
                .allowCredentials(this.allowCredentials)
                .userVerification(this.userVerification)
                .extensions(this.extensions);
    }

    /**
     * A challenge that the selected authenticator signs, along with other data, when producing an authentication
     * assertion. See the <a href="https://www.w3.org/TR/webauthn/#cryptographic-challenges">§13.1
     * Cryptographic Challenges</a> security consideration.
     * @return 
     */
    @NonNull
    public ByteArray getChallenge() {
        return this.challenge;
    }

    /**
     * Specifies a time, in milliseconds, that the caller is willing to wait for the call to complete.
     * <p>
     * This is treated as a hint, and MAY be overridden by the client.
     * </p>
     * @return 
     */
    @NonNull
    public Optional<Long> getTimeout() {
        return timeout;
    }

    /**
     * Specifies the relying party identifier claimed by the caller.
     * <p>
     * If omitted, its value will be set by the client.
     * </p>
     * @return 
     */
    @NonNull
    public Optional<String> getRpId() {
        return rpId;
    }

    /**
     * A list of {@link PublicKeyCredentialDescriptor} objects representing public key credentials acceptable to the
     * caller, in descending order of the caller’s preference (the first item in the list is the most preferred
     * credential, and so on down the list).
     * @return 
     */
    @NonNull
    public Optional<List<PublicKeyCredentialDescriptor>> getAllowCredentials() {
        return allowCredentials;
    }

    /**
     * Describes the Relying Party's requirements regarding <a href="https://www.w3.org/TR/webauthn/#user-verification">user
     * verification</a> for the <code>navigator.credentials.get()</code> operation.
     * <p>
     * Eligible authenticators are filtered to only those capable of satisfying this requirement.
     * </p>
     * @return 
     */
    @NonNull
    public UserVerificationRequirement getUserVerification() {
        return userVerification;
    }

    /**
     * Additional parameters requesting additional processing by the client and authenticator.
     * <p>
     * For example, if transaction confirmation is sought from the user, then the prompt string might be included as an
     * extension.
     * </p>
     * @return 
     */
    @NonNull
    public AssertionExtensionInputs getExtensions() {
        return extensions;
    }
}
