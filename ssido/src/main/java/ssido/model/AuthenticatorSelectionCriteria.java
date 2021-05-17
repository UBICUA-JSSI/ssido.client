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


/**
 * This class may be used to specify requirements regarding authenticator attributes.
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#dictdef-authenticatorselectioncriteria">§5.4.4.
 * Authenticator Selection Criteria (dictionary AuthenticatorSelectionCriteria)
 * </a>
 */
public final class AuthenticatorSelectionCriteria {
    /**
     * If present, eligible authenticators are filtered to only authenticators attached with the specified <a
     * href="https://www.w3.org/TR/webauthn/#attachment">§5.4.5 Authenticator Attachment Enumeration
     * (enum AuthenticatorAttachment)</a>.
     */
    @NonNull
    private final Optional<AuthenticatorAttachment> authenticatorAttachment;
    /**
     * Describes the Relying Party's requirements regarding resident credentials. If set to <code>true</code>, the

     * authenticator MUST create a <a href="https://www.w3.org/TR/webauthn/#client-side-resident-public-key-credential-source">client-side-resident

     * public key credential source</a> when creating a public key credential.
     */
    private final boolean requireResidentKey;
    /**
     * Describes the Relying Party's requirements regarding <a href="https://www.w3.org/TR/webauthn/#user-verification">user

     * verification</a> for the

     * <code>navigator.credentials.create()</code> operation. Eligible authenticators are filtered to only those

     * capable of satisfying this requirement.
     */
    @NonNull
    private final UserVerificationRequirement userVerification;

    @JsonCreator
    private AuthenticatorSelectionCriteria(
            @JsonProperty("authenticatorAttachment") AuthenticatorAttachment authenticatorAttachment,
            @JsonProperty("requireResidentKey") boolean requireResidentKey,
            @NonNull @JsonProperty("userVerification") UserVerificationRequirement userVerification) {
        this(Optional.fromNullable(authenticatorAttachment), requireResidentKey, userVerification);
    }


    public static class AuthenticatorSelectionCriteriaBuilder {
        private boolean requireResidentKey = false;
        private UserVerificationRequirement userVerification = UserVerificationRequirement.PREFERRED;
        @NonNull
        private Optional<AuthenticatorAttachment> authenticatorAttachment = Optional.absent();

        /**
         * If present, eligible authenticators are filtered to only authenticators attached with the specified <a
         * href="https://www.w3.org/TR/webauthn/#attachment">§5.4.5 Authenticator Attachment Enumeration
         * (enum AuthenticatorAttachment)</a>.
         * @param authenticatorAttachment
         * @return 
         */
        public AuthenticatorSelectionCriteriaBuilder authenticatorAttachment(@NonNull Optional<AuthenticatorAttachment> authenticatorAttachment) {
            this.authenticatorAttachment = authenticatorAttachment;
            return this;
        }

        /**
         * If present, eligible authenticators are filtered to only authenticators attached with the specified <a
         * href="https://www.w3.org/TR/webauthn/#attachment">§5.4.5 Authenticator Attachment Enumeration
         * (enum AuthenticatorAttachment)</a>.
         * @param authenticatorAttachment
         * @return 
         */
        public AuthenticatorSelectionCriteriaBuilder authenticatorAttachment(@NonNull AuthenticatorAttachment authenticatorAttachment) {
            return this.authenticatorAttachment(Optional.of(authenticatorAttachment));
        }

        @SuppressWarnings("all")
        AuthenticatorSelectionCriteriaBuilder() {
        }

        /**
         * Describes the Relying Party's requirements regarding resident credentials. If set to <code>true</code>, the
         * authenticator MUST create a <a href="https://www.w3.org/TR/webauthn/#client-side-resident-public-key-credential-source">client-side-resident
         * public key credential source</a> when creating a public key credential.
         */
        @SuppressWarnings("all")
        public AuthenticatorSelectionCriteriaBuilder requireResidentKey(final boolean requireResidentKey) {
            this.requireResidentKey = requireResidentKey;
            return this;
        }

        /**
         * Describes the Relying Party's requirements regarding <a href="https://www.w3.org/TR/webauthn/#user-verification">user
         * verification</a> for the
         * <code>navigator.credentials.create()</code> operation. Eligible authenticators are filtered to only those
         * capable of satisfying this requirement.
         */
        @SuppressWarnings("all")
        public AuthenticatorSelectionCriteriaBuilder userVerification(@NonNull final UserVerificationRequirement userVerification) {
            this.userVerification = userVerification;
            return this;
        }

        public AuthenticatorSelectionCriteria build() {
            boolean requireResidentKey = this.requireResidentKey;
            UserVerificationRequirement userVerification = this.userVerification;
            return new AuthenticatorSelectionCriteria(authenticatorAttachment, requireResidentKey, userVerification);
        }
    }

    public static AuthenticatorSelectionCriteriaBuilder builder() {
        return new AuthenticatorSelectionCriteriaBuilder();
    }

    public AuthenticatorSelectionCriteriaBuilder toBuilder() {
        return new AuthenticatorSelectionCriteriaBuilder().authenticatorAttachment(this.authenticatorAttachment).requireResidentKey(this.requireResidentKey).userVerification(this.userVerification);
    }

    /**
     * If present, eligible authenticators are filtered to only authenticators attached with the specified <a
     * href="https://www.w3.org/TR/webauthn/#attachment">§5.4.5 Authenticator Attachment Enumeration
     * (enum AuthenticatorAttachment)</a>.
     */
    @NonNull
    public Optional<AuthenticatorAttachment> getAuthenticatorAttachment() {
        return this.authenticatorAttachment;
    }

    /**
     * Describes the Relying Party's requirements regarding resident credentials. If set to <code>true</code>, the
     * authenticator MUST create a <a href="https://www.w3.org/TR/webauthn/#client-side-resident-public-key-credential-source">client-side-resident
     * public key credential source</a> when creating a public key credential.
     */
    public boolean isRequireResidentKey() {
        return this.requireResidentKey;
    }

    /**
     * Describes the Relying Party's requirements regarding <a href="https://www.w3.org/TR/webauthn/#user-verification">user
     * verification</a> for the
     * <code>navigator.credentials.create()</code> operation. Eligible authenticators are filtered to only those
     * capable of satisfying this requirement.
     */
    @NonNull
    public UserVerificationRequirement getUserVerification() {
        return this.userVerification;
    }

    private AuthenticatorSelectionCriteria(
            @NonNull final Optional<AuthenticatorAttachment> authenticatorAttachment,
            final boolean requireResidentKey,
            @NonNull final UserVerificationRequirement userVerification) {
        this.authenticatorAttachment = authenticatorAttachment;
        this.requireResidentKey = requireResidentKey;
        this.userVerification = userVerification;
    }
}
