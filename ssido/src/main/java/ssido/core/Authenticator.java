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
package ssido.core;

import androidx.annotation.NonNull;

import ssido.model.AttestationObject;
import ssido.model.AttestationStatement;
import ssido.model.AttestedCredentialData;
import ssido.model.AuthenticatorData;
import ssido.model.PackedAttestationStatement;

/**
 *
 * @author ITON Solutions
 */
public class Authenticator {
    private static final String TAG = Authenticator.class.getName();
    
    public static final byte[] AAGUID = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}; // 16 bytes long
    
    private final AttestedCredentialData attestedCredentialData;
    private final AuthenticatorData authenticatorData;
    private final AttestationStatement attestationStatement;
    
   
    private Authenticator(AuthenticatorData authenticatorData, AttestationStatement attestationStatement, AttestedCredentialData attestedCredentialData){
        this.authenticatorData = authenticatorData;
        this.attestationStatement = attestationStatement;
        this.attestedCredentialData = attestedCredentialData;
    }
    
    public static AuthenticatorBuilder.MandatoryStages builder() {
        return new AuthenticatorBuilder.MandatoryStages();
    }

    public static class AuthenticatorBuilder {

        private AttestedCredentialData attestedCredentialData;
        private AuthenticatorData authenticatorData;
        private AttestationStatement attestationStatement;
        
        AuthenticatorBuilder(){}

        public static class MandatoryStages {
            private final AuthenticatorBuilder builder = new AuthenticatorBuilder();

            public AuthenticatorStage publicKey(@NonNull final byte[] publicKey) {
                builder.publicKey(publicKey);
                return new AuthenticatorStage();
            }

            public class AuthenticatorStage{
                public CounterStage rpId(@NonNull final byte[] rpId) {
                    builder.rpId(rpId);
                    return new CounterStage();
                }
            }

            public class CounterStage{
                public AttestationStatementStage counter(@NonNull final Integer counter) {
                    builder.counter(counter);
                    return new AttestationStatementStage ();
                }
            }


            public class AttestationStatementStage{
                public AuthenticatorBuilder attestationStatement(@NonNull byte[] privateKey, byte[] clientData){
                    return builder.attestationStatement(privateKey, clientData);
                }
            }
        }
        
        AuthenticatorBuilder publicKey(@NonNull byte[] publicKey){
        
            this.attestedCredentialData = AttestedCredentialData.builder()
                    .aaguid(AAGUID)
                    .publicKey(publicKey)
                    .build();

            return this;
        }
        
        AuthenticatorBuilder rpId(@NonNull byte[] rpId) {
            this.authenticatorData = AuthenticatorData.builder()
                    .rpId(rpId)
                    .attestedCredentialData(attestedCredentialData)
                    .build();
            return this;
        }

        AuthenticatorBuilder counter(@NonNull Integer counter) {
            this.authenticatorData.setCounter(counter);
            return this;
        }


        AuthenticatorBuilder attestationStatement(@NonNull byte[] privateKey, byte[] clientData) {
            this.attestationStatement = PackedAttestationStatement.builder()
                    .privateKey(privateKey)
                    .authenticatorData(authenticatorData.getBytes())
                    .clientData(clientData)
                    .build();
            return this;
        }
        
        public Authenticator build(){
            return new Authenticator(authenticatorData, attestationStatement, attestedCredentialData);
        }
    }

    public AttestedCredentialData getAttestedCredentialData() {
        return attestedCredentialData;
    }

    public AuthenticatorData getAuthenticatorData() {
        return authenticatorData;
    }

    public AttestationStatement getAttestationStatement() {
        return attestationStatement;
    }
    
    public AttestationObject getAttestationObject(){
        return AttestationObject.builder()
                .attestationStatement(attestationStatement.getObjectNode())
                .authenticatorData(authenticatorData)
                .build();
    }
}
