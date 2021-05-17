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

import ssido.crypto.math.Curve;
import ssido.crypto.math.Field;
import ssido.crypto.math.ed25519.Ed25519LittleEndianEncoding;
import ssido.crypto.math.ed25519.Ed25519ScalarOps;
import ssido.util.CryptoUtil;

import java.util.HashMap;
import java.util.Locale;


/**
 * The named EdDSA curves.
 *
 * @author str4d
 */
public class EdDSANamedCurveTable {
    public static final String ED_25519 = "Ed25519";

    private static final Field ed25519field = new Field(
            256, // b
            CryptoUtil.fromHex("edffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f"), // q
            new Ed25519LittleEndianEncoding());

    private static final Curve ed25519curve = new Curve(ed25519field,
            CryptoUtil.fromHex("a3785913ca4deb75abd841414d0a700098e879777940c78c73fe6f2bee6c0352"), // d
            ed25519field.fromByteArray(CryptoUtil.fromHex("b0a00e4a271beec478e42fad0618432fa7d7fb3d99004d2b0bdfc14f8024832b"))); // I

    public static final EdDSANamedCurveSpec ED_25519_CURVE_SPEC = new EdDSANamedCurveSpec(
            ED_25519,
            ed25519curve,
            "SHA-512", // H
            new Ed25519ScalarOps(), // l
            ed25519curve.createPoint( // B
                    CryptoUtil.fromHex("5866666666666666666666666666666666666666666666666666666666666666"),
                    true)); // Precompute tables for B

    private static volatile HashMap<String, EdDSANamedCurveSpec> curves = new HashMap<>();

    private static synchronized void putCurve(String name, EdDSANamedCurveSpec curve) {
        HashMap<String, EdDSANamedCurveSpec> newCurves = new HashMap<>(curves);
        newCurves.put(name, curve);
        curves = newCurves;
    }

    public static void defineCurve(EdDSANamedCurveSpec curve) {
        putCurve(curve.getName().toLowerCase(Locale.ENGLISH), curve);
    }

    static void defineCurveAlias(String name, String alias) {
        EdDSANamedCurveSpec curve = curves.get(name.toLowerCase(Locale.ENGLISH));
        if (curve == null) {
            throw new IllegalStateException();
        }
        putCurve(alias.toLowerCase(Locale.ENGLISH), curve);
    }

    static {
        // RFC 8032
        defineCurve(ED_25519_CURVE_SPEC);
    }

    public static EdDSANamedCurveSpec getByName(String name) {
        return curves.get(name.toLowerCase(Locale.ENGLISH));
    }
}
