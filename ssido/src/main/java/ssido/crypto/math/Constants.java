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


import ssido.util.CryptoUtil;

final class Constants {
    static final byte[] ZERO = CryptoUtil.fromHex("0000000000000000000000000000000000000000000000000000000000000000");
    static final byte[] ONE = CryptoUtil.fromHex("0100000000000000000000000000000000000000000000000000000000000000");
    static final byte[] TWO = CryptoUtil.fromHex("0200000000000000000000000000000000000000000000000000000000000000");
    static final byte[] FOUR = CryptoUtil.fromHex("0400000000000000000000000000000000000000000000000000000000000000");
    static final byte[] FIVE = CryptoUtil.fromHex("0500000000000000000000000000000000000000000000000000000000000000");
    static final byte[] EIGHT = CryptoUtil.fromHex("0800000000000000000000000000000000000000000000000000000000000000");
}
