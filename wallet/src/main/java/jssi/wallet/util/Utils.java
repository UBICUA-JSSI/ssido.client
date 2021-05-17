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
package jssi.wallet.util;

/**
 *
 * @author ITON Solutions
 */
public class Utils {
    
    public static String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
    
    public static byte[] fromHex(String data) {
        byte[] result = new byte[data.length() / 2];

        for (int i = 0; i < result.length; i++) {
            int index = i * 2;
            result[i] = Integer.valueOf(data.substring(index, index + 2), 0x10).byteValue();
        }
        return result;
    }
        
    public static byte[] toBytes(int value) {
        byte[] result = new byte[4];
        result[3] = (byte) (value >> 24);
        result[2] = (byte) (value >> 16);
        result[1] = (byte) (value >> 8);
        result[0] = (byte) value;
        return result;
    }
}
