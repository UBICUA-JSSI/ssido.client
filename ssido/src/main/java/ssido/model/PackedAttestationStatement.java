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

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Arrays;

import ssido.util.BinaryUtil;
import org.libsodium.api.Crypto_hash_sha256;
import org.libsodium.api.Crypto_sign_ed25519;
import org.libsodium.jni.SodiumException;
/**
 *
 * @author ITON Solutions
 */
public class PackedAttestationStatement{
    
    private static final String TAG = PackedAttestationStatement.class.getName();
  
    private PackedAttestationStatement() {
    }

    public static AttestationStatementBuilder builder() {
        return new AttestationStatementBuilder();
    }
    
    public static class AttestationStatementBuilder{
        byte[] authenticatorData;
        byte[] clientData;
        byte[] privateKey;
        
        AttestationStatementBuilder(){}
        
        public AttestationStatementBuilder privateKey(@NonNull byte[] privateKey){
            this.privateKey = privateKey;
            return this;
        }

        public AttestationStatementBuilder authenticatorData(@NonNull byte[] authenticatorData){
            this.authenticatorData = authenticatorData;
            return this;
        }

        public AttestationStatementBuilder clientData(@NonNull byte[] clientData){
            this.clientData = clientData;
            return this;
        }
        
        public AttestationStatement build() {
            
            JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectNode result = factory.objectNode();
            
            try {
                byte[] data = BinaryUtil.concat(authenticatorData, Crypto_hash_sha256.sha256(clientData));
                byte[] sig = Crypto_sign_ed25519.sign(data, privateKey);
                result.set("sig", factory.binaryNode(Arrays.copyOfRange(sig, 0, sig.length - data.length))); // detach data
                result.set("alg", factory.numberNode(-8)); // EdDSA(-8)

            } catch (SodiumException e) {
                Log.e("Sodium exception {}", e.getMessage());
            }

            return () -> result;
        }
    }
}
