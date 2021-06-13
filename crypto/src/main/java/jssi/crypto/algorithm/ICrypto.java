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
package jssi.crypto.algorithm;

import jssi.crypto.CryptoException;
import jssi.crypto.Keys;
import org.libsodium.jni.SodiumException;

/**
 *
 * @author UBICUA
 */
public interface ICrypto {
    
    public static enum CryptoType {
        DEFAULT_CRYPTO_TYPE("ed25519");
                
        private final String type;
        CryptoType(String type){
            this.type = type;
        }
        
        public String getName(){
            return type;
        }
        
        public static CryptoType toType(String type) {
            for (CryptoType item : CryptoType.values()) {
                if (item.type.equalsIgnoreCase(type)) {
                    return item;
                }
            }
            return null;
        }
    }
    
    public Keys createKeys(byte[] seed) throws SodiumException;
    public byte[] sign(byte[] data, byte[] sk) throws SodiumException, CryptoException;
    public boolean verify(byte[] data, byte[] signature, byte[] pk) throws SodiumException;
    public byte[] cryptoBox(byte[] data, byte[] nonce, byte[] pk, byte[] sk) throws SodiumException;
    public byte[] cryptoBoxOpen(byte[] cipher, byte[] nonce, byte[] pk, byte[] sk) throws SodiumException;
    public byte[] cryptoBoxSeal(byte[] data, byte[] pk) throws SodiumException;
    public byte[] cryptoBoxSealOpen(byte[] cipher, byte[] verkey, byte[] sk) throws SodiumException;
    public byte[] genNonce() throws SodiumException;
    
    public void validateKeys(String verkey);
    public CryptoType getType();
}
