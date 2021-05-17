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
package jssi.wallet.crypto;

import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Base58;
import org.libsodium.api.Crypto_randombytes;
import static org.libsodium.jni.SodiumConstants.CRYPTO_AEAD_CHACHA20POLY1305_IETF_KEYBYTES;
import static org.libsodium.jni.SodiumConstants.CRYPTO_PWHASH_ARGON2I_SALTBYTES;
import org.libsodium.jni.SodiumException;

/**
 *
 * @author ITON Solutions
 */
public class KeyDerivationData {
    
    private final String passphrase;
    private byte[] salt;
    private final Method method;
    
    
    public KeyDerivationData(final String passphrase, byte[] salt){
        this(passphrase, salt, Method.ARGON2I_MOD);
    }

    public KeyDerivationData(final String passphrase, byte[] salt, Method method){
        this.method = method;
        this.passphrase = passphrase;
        
        if(method != Method.RAW){
            this.salt = salt;
        }
    }
    
    public KeyDerivationData(final String passphrase) throws SodiumException{
        this(passphrase, Method.ARGON2I_MOD);
    }

    public KeyDerivationData(final String passphrase, final Method method) throws SodiumException{
        this.method = method;
        this.passphrase = passphrase;
        
        if(method != Method.RAW){
            salt = new byte[CRYPTO_PWHASH_ARGON2I_SALTBYTES];
            Crypto_randombytes.buf(salt);
        }
    }
    
    public KeyDerivationData(final String passphrase, final KeysMetadata metadata){
        this(passphrase, metadata, Method.ARGON2I_MOD);
    }
    
    private KeyDerivationData(final String passphrase, final KeysMetadata metadata, Method method){
        this.method = method;
        this.passphrase = passphrase;
        
        if(method != Method.RAW){
            salt = metadata.getMasterKeySalt();
        }
    }
    
    public byte[] deriveMasterKey() throws SodiumException{
        
        byte[] masterKey;
        
        if(method == Method.RAW){
            masterKey = Base58.decode(passphrase);
            
            if(masterKey.length != CRYPTO_AEAD_CHACHA20POLY1305_IETF_KEYBYTES){
                throw new AddressFormatException("Incorrect RAW passphrase");
            }
            
        } else {
            masterKey = Crypto.deriveKey(passphrase, salt, method);
        }
        return masterKey;
    }

    public Method getDerivationMethod() {
        return method;
    }

    public byte[] getSalt() {
        return salt;
    }
}
