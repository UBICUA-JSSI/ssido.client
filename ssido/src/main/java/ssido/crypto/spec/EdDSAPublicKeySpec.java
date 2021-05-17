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
package ssido.crypto.spec;

import ssido.crypto.math.GroupElement;

import java.security.spec.KeySpec;


/**
 * @author str4d
 *
 */
public class EdDSAPublicKeySpec implements KeySpec {
    private final GroupElement A;
    private GroupElement Aneg = null;
    private final EdDSAParameterSpec spec;

    /**
     * @param pk the public key
     * @param spec the parameter specification for this key
     * @throws IllegalArgumentException if key length is wrong
     */
    public EdDSAPublicKeySpec(byte[] pk, EdDSAParameterSpec spec) {
        if (pk.length != spec.getCurve().getField().getb()/8)
            throw new IllegalArgumentException("public-key length is wrong");

        this.A = new GroupElement(spec.getCurve(), pk);
        this.spec = spec;
    }

    public EdDSAPublicKeySpec(GroupElement A, EdDSAParameterSpec spec) {
        this.A = A;
        this.spec = spec;
    }

    public GroupElement getA() {
        return A;
    }

    public GroupElement getNegativeA() {
        // Only read Aneg once, otherwise read re-ordering might occur between here and return. Requires all GroupElement's fields to be final.
        GroupElement ourAneg = Aneg;
        if(ourAneg == null) {
            ourAneg = A.negate();
            Aneg = ourAneg;
        }
        return ourAneg;
    }

    public EdDSAParameterSpec getParams() {
        return spec;
    }
}
