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

import com.upokecenter.cbor.CBORObject;

import ssido.crypto.EdDSAPublicKey;

import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


public final class AuthenticatorCodec {

    public static byte[] publicKeyToCose(PublicKey key) {
        Map<Long, Object> coseKey = new HashMap<>();

        coseKey.put(1L, 1L);  // Key type: octet key pair
        coseKey.put(3L, -8);  // EdDSA(-8)
        coseKey.put(-1L, 6L); // crv: Ed25519
        coseKey.put(-2L, key.getEncoded());

        return CBORObject.FromObject(coseKey).EncodeToBytes();
    }
    
    public static PublicKey coseToPublicKey(CBORObject cose) {
        byte[] encoded = cose.get(CBORObject.FromObject(-2)).GetByteString();

        try {
            X509EncodedKeySpec decoded = new X509EncodedKeySpec(encoded);
            return new EdDSAPublicKey(decoded);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
