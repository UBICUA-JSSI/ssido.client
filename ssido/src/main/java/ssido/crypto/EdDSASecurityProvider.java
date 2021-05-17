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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;

/**
 * A security {@link Provider} that can be registered via {@link Security#addProvider(Provider)}
 *
 * @author str4d
 */
public class EdDSASecurityProvider extends Provider {
    public static final String PROVIDER_NAME = "EdDSA";

    public EdDSASecurityProvider() {
        super(PROVIDER_NAME, 0.3 /* should match POM major.minor version */, "UBICUA " + PROVIDER_NAME + " security provider wrapper");

        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                setup();
                return null;
            }
        });
    }

    protected void setup() {
        // See https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/HowToImplAProvider.html
        put("KeyFactory." + EdDSAKey.KEY_ALGORITHM, "ssido.crypto.KeyFactory");
        put("KeyPairGenerator." + EdDSAKey.KEY_ALGORITHM, "ssido.crypto.KeyPairGenerator");
        put("Signature." + EdDSAEngine.SIGNATURE_ALGORITHM, "ssido.crypto.EdDSAEngine");

        // OID Mappings
        // See section "Mapping from OID to name".
        // The Key* -> OID mappings correspond to the default algorithm in KeyPairGenerator.
        //
        // From RFC 8410 section 3:
        //   id-Ed25519   OBJECT IDENTIFIER ::= { 1 3 101 112 }
        put("Alg.Alias.KeyFactory.1.3.101.112", EdDSAKey.KEY_ALGORITHM);
        put("Alg.Alias.KeyFactory.OID.1.3.101.112", EdDSAKey.KEY_ALGORITHM);
        put("Alg.Alias.KeyPairGenerator.1.3.101.112", EdDSAKey.KEY_ALGORITHM);
        put("Alg.Alias.KeyPairGenerator.OID.1.3.101.112", EdDSAKey.KEY_ALGORITHM);
        put("Alg.Alias.Signature.1.3.101.112", EdDSAEngine.SIGNATURE_ALGORITHM);
        put("Alg.Alias.Signature.OID.1.3.101.112", EdDSAEngine.SIGNATURE_ALGORITHM);
    }
}
