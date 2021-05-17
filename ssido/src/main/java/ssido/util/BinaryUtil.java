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

package ssido.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.spongycastle.util.encoders.Hex;


public class BinaryUtil {

    public static byte[] copy(byte[] bytes) {
        return Arrays.copyOf(bytes, bytes.length);
    }

    /**
     * @param bytes
     *     Bytes to encode
     * @return 
     */
    public static String toHex(byte[] bytes) {
        return Hex.toHexString(bytes);
    }

    /**
     * @param hex
     *     String of hexadecimal digits to decode as bytes.
     * @return 
     */
    public static byte[] fromHex(String hex) {
        return Hex.decode(hex);
    }

    /**
     * Parse a single byte from two hexadecimal characters.
     *
     * @param hex
     *     String of hexadecimal digits to decode as bytes.
     * @return 
     */
    public static byte singleFromHex(String hex) {
        ExceptionUtil.assure(hex.length() == 2, "Argument must be exactly 2 hexadecimal characters, was: %s", hex);
        return fromHex(hex)[0];
    }

        /**
         * Read one byte as an unsigned 8-bit integer.
         * <p>
         * Result is of type Short because Java don't have unsigned types.
         *
     * @param b
         * @return A value between 0 and 255, inclusive.
         */
    public static short getUint8(byte b) {
        // Prepend a zero so we can parse it as a signed int16 instead of a signed int8
        return ByteBuffer.wrap(new byte[]{ 0, b })
            .order(ByteOrder.BIG_ENDIAN)
            .getShort();
    }


    /**
     * Read 2 bytes as a big endian unsigned 16-bit integer.
     * <p>
     * Result is of type Int because Java don't have unsigned types.
     *
     * @param bytes
     * @return A value between 0 and 2^16- 1, inclusive.
     */
    public static int getUint16(byte[] bytes) {
        if (bytes.length == 2) {
            // Prepend zeroes so we can parse it as a signed int32 instead of a signed int16
            return ByteBuffer.wrap(new byte[] { 0, 0, bytes[0], bytes[1] })
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        } else {
            throw new IllegalArgumentException("Argument must be 2 bytes, was: " + bytes.length);
        }
    }


    /**
     * Read 4 bytes as a big endian unsigned 32-bit integer.
     * <p>
     * Result is of type Long because Java don't have unsigned types.
     *
     * @param bytes
     * @return A value between 0 and 2^32 - 1, inclusive.
     */
    public static int getUint32(byte[] bytes) {
        if (bytes.length == 4) {
            // Prepend zeroes so we can parse it as a signed int32 instead of a signed int16
            return ByteBuffer.wrap(new byte[] { 0, 0, 0, 0, bytes[0], bytes[1], bytes[2], bytes[3] })
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        } else {
            throw new IllegalArgumentException("Argument must be 4 bytes, was: " + bytes.length);
        }
    }

    public static byte[] encodeUint16(int value) {
        ExceptionUtil.assure(value >= 0, "Argument must be non-negative, was: %d", value);
        ExceptionUtil.assure(value < 65536, "Argument must be smaller than 2^15=65536, was: %d", value);

        ByteBuffer b = ByteBuffer.allocate(4);
        b.order(ByteOrder.BIG_ENDIAN);
        b.putInt(value);
        b.rewind();
        return Arrays.copyOfRange(b.array(), 2, 4);
    }
    
    public static byte[] encodeUint32(int value) {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.order(ByteOrder.BIG_ENDIAN);
        b.putInt(value);
        b.rewind();
        return b.array();
    }

    /**
     * Returns the values from each provided array combined into a single array. For example, {@code
     * concat(new byte[] {a, b}, new byte[] {}, new byte[] {c}} returns the array {@code {a, b, c}}.
     *
     * @param arrays zero or more {@code byte} arrays
     * @return a single array containing all the values from the source arrays, in order
     */
    public static byte[] concat(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] result = new byte[length];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }


    /**
     * Get the i'th bit of a byte array.
     * @param h the byte array.
     * @param i the bit index.
     * @return 0 or 1, the value of the i'th bit in h
     */
    public static int bit(byte[] h, int i) {
        return (h[i >> 3] >> (i & 7)) & 1;
    }

    /**
     * Constant-time determine if byte is negative.
     * @param b the byte to check.
     * @return 1 if the byte is negative, 0 otherwise.
     */
    public static int negative(int b) {
        return (b >> 8) & 1;
    }
}
