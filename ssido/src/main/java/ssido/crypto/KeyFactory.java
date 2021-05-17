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
package ssido.crypto;

import ssido.crypto.spec.EdDSAPrivateKeySpec;
import ssido.crypto.spec.EdDSAPublicKeySpec;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author str4d
 *
 */
public final class KeyFactory extends KeyFactorySpi {

    protected PrivateKey engineGeneratePrivate(KeySpec keySpec)
            throws InvalidKeySpecException {
        if (keySpec instanceof EdDSAPrivateKeySpec) {
            return new EdDSAPrivateKey((EdDSAPrivateKeySpec) keySpec);
        }
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            return new EdDSAPrivateKey((PKCS8EncodedKeySpec) keySpec);
        }
        throw new InvalidKeySpecException("key spec not recognised: " + keySpec.getClass());
    }

    protected PublicKey engineGeneratePublic(KeySpec keySpec)
            throws InvalidKeySpecException {
        if (keySpec instanceof EdDSAPublicKeySpec) {
            return new EdDSAPublicKey((EdDSAPublicKeySpec) keySpec);
        }
        if (keySpec instanceof X509EncodedKeySpec) {
            return new EdDSAPublicKey((X509EncodedKeySpec) keySpec);
        }
        throw new InvalidKeySpecException("key spec not recognised: " + keySpec.getClass());
    }

    @SuppressWarnings("unchecked")
    protected <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> keySpec)
            throws InvalidKeySpecException {
        if (keySpec.isAssignableFrom(EdDSAPublicKeySpec.class) && key instanceof EdDSAPublicKey) {
            EdDSAPublicKey k = (EdDSAPublicKey) key;
            if (k.getParams() != null) {
                return (T) new EdDSAPublicKeySpec(k.getA(), k.getParams());
            }
        } else if (keySpec.isAssignableFrom(EdDSAPrivateKeySpec.class) && key instanceof EdDSAPrivateKey) {
            EdDSAPrivateKey k = (EdDSAPrivateKey) key;
            if (k.getParams() != null) {
                return (T) new EdDSAPrivateKeySpec(k.getSeed(), k.getH(), k.geta(), k.getA(), k.getParams());
            }
        }
        throw new InvalidKeySpecException("not implemented yet " + key + " " + keySpec);
    }

    protected Key engineTranslateKey(Key key) throws InvalidKeyException {
        throw new InvalidKeyException("No other EdDSA key providers known");
    }
}
