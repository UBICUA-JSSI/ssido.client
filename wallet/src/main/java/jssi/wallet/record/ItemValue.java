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
package jssi.wallet.record;

import jssi.wallet.crypto.Crypto;
import jssi.store.model.Item;
import org.libsodium.api.Crypto_randombytes;
import static org.libsodium.jni.SodiumConstants.CRYPTO_AEAD_CHACHA20POLY1305_IETF_KEYBYTES;
import org.libsodium.jni.SodiumException;

public class ItemValue {
    
    private byte[] value;
    private byte[] key;
    
    ItemValue(){
    }

    public ItemValue(Item item){
        this.value = item.getValue();
        this.key = item.getKey();
    }

    public String decrypt(byte[] value_key) throws SodiumException{
        byte[] decrypt_key = Crypto.decryptMerged(key, value_key);
        return new String(Crypto.decryptMerged(value, decrypt_key));
    }
    
    public ItemValue encrypt(byte[] value, byte[] value_key) throws SodiumException{
        byte[] encrypt_key = new byte[CRYPTO_AEAD_CHACHA20POLY1305_IETF_KEYBYTES];
        Crypto_randombytes.buf(encrypt_key);
        this.value = Crypto.encryptAsNotSearchable(value, encrypt_key);
        this.key   = Crypto.encryptAsNotSearchable(encrypt_key, value_key);
        return this;
    }

    public byte[] getValue() {
        return value;
    }

    public byte[] getKey() {
        return key;
    }
    
}
