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
package ssido.crypto.math;

public interface ScalarOps {
    /**
     * Reduce the given scalar mod $l$.
     * <p>
     * From the Ed25519 paper:<br>
     * Here we interpret $2b$-bit strings in little-endian form as integers in
     * $\{0, 1,..., 2^{(2b)}-1\}$.
     * @param s the scalar to reduce
     * @return $s \bmod l$
     */
    public byte[] reduce(byte[] s);

    /**
     * $r = (a * b + c) \bmod l$
     * @param a a scalar
     * @param b a scalar
     * @param c a scalar
     * @return $(a*b + c) \bmod l$
     */
    public byte[] multiplyAndAdd(byte[] a, byte[] b, byte[] c);
}
