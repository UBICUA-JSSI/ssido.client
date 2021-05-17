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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to supply additional parameters when creating a new credential.
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#dictdef-publickeycredentialparameters">§5.3.
 * Parameters for Credential Generation (dictionary PublicKeyCredentialParameters)
 * </a>
 */
public final class PublicKeyCredentialParameters {
    /**
     * Specifies the cryptographic signature algorithm with which the newly generated credential will be used, and thus
     * also the type of asymmetric key pair to be generated, e.g., RSA or Elliptic Curve.
     */
    @NonNull
    private final COSEAlgorithmIdentifier alg;
    /**
     * Specifies the type of credential to be created.
     */
    @NonNull
    private final PublicKeyCredentialType type;

    private PublicKeyCredentialParameters(@NonNull @JsonProperty("alg") COSEAlgorithmIdentifier alg, @NonNull @JsonProperty("type") PublicKeyCredentialType type) {
        this.alg = alg;
        this.type = type;
    }

    /**
     * Algorithm {@link COSEAlgorithmIdentifier#EdDSA} and type {@link PublicKeyCredentialType#PUBLIC_KEY}.
     */
    public static final PublicKeyCredentialParameters EdDSA = builder().alg(COSEAlgorithmIdentifier.EdDSA).build();
    /**
     * Algorithm {@link COSEAlgorithmIdentifier#ES256} and type {@link PublicKeyCredentialType#PUBLIC_KEY}.
     */
    public static final PublicKeyCredentialParameters ES256 = builder().alg(COSEAlgorithmIdentifier.ES256).build();
    /**
     * Algorithm {@link COSEAlgorithmIdentifier#RS256} and type {@link PublicKeyCredentialType#PUBLIC_KEY}.
     */
    public static final PublicKeyCredentialParameters RS256 = builder().alg(COSEAlgorithmIdentifier.RS256).build();

    public static PublicKeyCredentialParametersBuilder.MandatoryStages builder() {
        return new PublicKeyCredentialParametersBuilder.MandatoryStages();
    }

    public static class PublicKeyCredentialParametersBuilder {
        private COSEAlgorithmIdentifier alg;
        private PublicKeyCredentialType type = PublicKeyCredentialType.PUBLIC_KEY;


        static class MandatoryStages {
            private PublicKeyCredentialParametersBuilder builder = new PublicKeyCredentialParametersBuilder();

            PublicKeyCredentialParametersBuilder alg(COSEAlgorithmIdentifier alg) {
                return builder.alg(alg);
            }
        }

        PublicKeyCredentialParametersBuilder() {
        }

        /**
         * Specifies the cryptographic signature algorithm with which the newly generated credential will be used, and thus
         * also the type of asymmetric key pair to be generated, e.g., RSA or Elliptic Curve.
         * @param alg
         * @return 
         */
        public PublicKeyCredentialParametersBuilder alg(@NonNull final COSEAlgorithmIdentifier alg) {
            this.alg = alg;
            return this;
        }

        /**
         * Specifies the type of credential to be created.
         * @param type
         * @return 
         */
        public PublicKeyCredentialParametersBuilder type(@NonNull final PublicKeyCredentialType type) {
            this.type = type;
            return this;
        }

        public PublicKeyCredentialParameters build() {
            return new PublicKeyCredentialParameters(alg, type);
        }

        
    }

    public PublicKeyCredentialParametersBuilder toBuilder() {
        return new PublicKeyCredentialParametersBuilder().alg(this.alg).type(this.type);
    }

    /**
     * Specifies the cryptographic signature algorithm with which the newly generated credential will be used, and thus
     * also the type of asymmetric key pair to be generated, e.g., RSA or Elliptic Curve.
     * @return 
     */
    @NonNull
    public COSEAlgorithmIdentifier getAlg() {
        return this.alg;
    }

    /**
     * Specifies the type of credential to be created.
     * @return 
     */
    @NonNull
    public PublicKeyCredentialType getType() {
        return this.type;
    }
}