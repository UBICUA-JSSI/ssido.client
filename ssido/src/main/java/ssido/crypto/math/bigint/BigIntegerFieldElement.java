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
import ssido.crypto.math.FieldElement;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * A particular element of the field \Z/(2^255-19).
 * @author str4d
 *
 */
public class BigIntegerFieldElement extends FieldElement implements Serializable {
    /**
     * Variable is package private for encoding.
     */
    final BigInteger bi;

    public BigIntegerFieldElement(Field f, BigInteger bi) {
        super(f);
        this.bi = bi;
    }

    public boolean isNonZero() {
        return !bi.equals(BigInteger.ZERO);
    }

    public FieldElement add(FieldElement val) {
        return new BigIntegerFieldElement(f, bi.add(((BigIntegerFieldElement)val).bi)).mod(f.getQ());
    }

    @Override
    public FieldElement addOne() {
        return new BigIntegerFieldElement(f, bi.add(BigInteger.ONE)).mod(f.getQ());
    }

    public FieldElement subtract(FieldElement val) {
        return new BigIntegerFieldElement(f, bi.subtract(((BigIntegerFieldElement)val).bi)).mod(f.getQ());
    }

    @Override
    public FieldElement subtractOne() {
        return new BigIntegerFieldElement(f, bi.subtract(BigInteger.ONE)).mod(f.getQ());
    }

    public FieldElement negate() {
        return f.getQ().subtract(this);
    }

    @Override
    public FieldElement divide(FieldElement val) {
        return divide(((BigIntegerFieldElement)val).bi);
    }

    public FieldElement divide(BigInteger val) {
        return new BigIntegerFieldElement(f, bi.divide(val)).mod(f.getQ());
    }

    public FieldElement multiply(FieldElement val) {
        return new BigIntegerFieldElement(f, bi.multiply(((BigIntegerFieldElement)val).bi)).mod(f.getQ());
    }

    public FieldElement square() {
        return multiply(this);
    }

    public FieldElement squareAndDouble() {
        FieldElement sq = square();
        return sq.add(sq);
    }

    public FieldElement invert() {
        // Euler's theorem
        //return modPow(f.getQm2(), f.getQ());
        return new BigIntegerFieldElement(f, bi.modInverse(((BigIntegerFieldElement)f.getQ()).bi));
    }

    public FieldElement mod(FieldElement m) {
        return new BigIntegerFieldElement(f, bi.mod(((BigIntegerFieldElement)m).bi));
    }

    public FieldElement modPow(FieldElement e, FieldElement m) {
        return new BigIntegerFieldElement(f, bi.modPow(((BigIntegerFieldElement)e).bi, ((BigIntegerFieldElement)m).bi));
    }

    public FieldElement pow(FieldElement e){
        return modPow(e, f.getQ());
    }

    public FieldElement pow22523(){
        return pow(f.getQm5d8());
    }

    @Override
    public FieldElement cmov(FieldElement val, int b) {
        // Not constant-time, but it doesn't really matter because none of the underlying BigInteger operations
        // are either, so there's not much point in trying hard here ...
        return b == 0 ? this : val;
    }

    @Override
    public int hashCode() {
        return bi.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BigIntegerFieldElement))
            return false;
        BigIntegerFieldElement fe = (BigIntegerFieldElement) obj;
        return bi.equals(fe.bi);
    }

    @Override
    public String toString() {
        return "[BigIntegerFieldElement val=" + bi + "]";
    }
}
