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

package jssi.crypto;

import androidx.annotation.Nullable;
import jssi.crypto.algorithm.ICrypto.CryptoType;

/**
 *
 * @author ITON Solutions
 */
public class KeyInfo {
    
    public @Nullable String seed;
    public String cryptoType;
    
    public KeyInfo(@Nullable String seed){
        this.seed = seed;
        this.cryptoType = CryptoType.DEFAULT_CRYPTO_TYPE.getName();
    }
    
    public KeyInfo(@Nullable String seed, @Nullable String cryptoType){
        this.seed = seed;
        this.cryptoType = cryptoType == null ? CryptoType.DEFAULT_CRYPTO_TYPE.getName() : cryptoType;
    }
    
    @Override
    public String toString(){
        return String.format("KeyInfo: {type %s, seed: %s}", cryptoType, seed);
    }
}
