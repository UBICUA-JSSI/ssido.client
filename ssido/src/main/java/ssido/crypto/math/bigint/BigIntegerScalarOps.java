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
package ssido.crypto.math.bigint;

import ssido.crypto.math.Field;
import ssido.crypto.math.ScalarOps;

import java.math.BigInteger;

public class BigIntegerScalarOps implements ScalarOps {
    private final BigInteger l;
    private final BigIntegerLittleEndianEncoding enc;

    public BigIntegerScalarOps(Field f, BigInteger l) {
        this.l = l;
        enc = new BigIntegerLittleEndianEncoding();
        enc.setField(f);
    }

    public byte[] reduce(byte[] s) {
        return enc.encode(enc.toBigInteger(s).mod(l));
    }

    public byte[] multiplyAndAdd(byte[] a, byte[] b, byte[] c) {
        return enc.encode(enc.toBigInteger(a).multiply(enc.toBigInteger(b)).add(enc.toBigInteger(c)).mod(l));
    }

}
