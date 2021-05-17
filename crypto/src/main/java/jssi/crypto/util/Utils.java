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
package jssi.crypto.util;

import org.bitcoinj.core.Base58;

/**
 *
 * @author ITON Solutions
 */
public class Utils {

    public static byte[] fromHex(String data) {
        byte[] result = new byte[data.length() / 2];

        for (int i = 0; i < result.length; i++) {
            int index = i * 2;
            result[i] = Integer.valueOf(data.substring(index, index + 2), 0x10).byteValue();
        }
        return result;
    }

    public static String buildFullVerkey(String dest, String verkey) {

        if (verkey == null) {
            return dest;
        }

        String key = null;
        String type = null;

        String[] data = verkey.split(":");
        if (data.length == 2) {
            key = data[0];
            type = data[1];
        } else {
            key = verkey;
            type = null;
        }

        if (key.startsWith("~")) {
            byte[] result = Base58.decode(dest);
            byte[] suffix = Base58.decode(key.substring(1));
            
            result = concat(result, suffix);
            key = Base58.encode(result);
        }

        if (type != null) {
            key = String.format("%s:%s", key, type);
        }
        return key;
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

    public static String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
