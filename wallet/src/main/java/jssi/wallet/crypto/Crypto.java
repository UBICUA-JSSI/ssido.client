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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.libsodium.api.Crypto_aead_chacha20poly1305_ietf;
import org.libsodium.api.Crypto_auth_hmacsha256;
import org.libsodium.api.Crypto_pwhash;
import org.libsodium.api.Crypto_randombytes;
import static org.libsodium.jni.SodiumConstants.CRYPTO_AEAD_CHACHA20POLY1305_IETF_NONCEBYTES;
import static org.libsodium.jni.SodiumConstants.CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE;
import static org.libsodium.jni.SodiumConstants.CRYPTO_PWHASH_MEMLIMIT_MODERATE;
import static org.libsodium.jni.SodiumConstants.CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE;
import static org.libsodium.jni.SodiumConstants.CRYPTO_PWHASH_OPSLIMIT_MODERATE;
import org.libsodium.jni.SodiumException;

/**
 *
 * @author ITON Solutions
 */
public class Crypto {
    
    public static byte[] encryptAsSearchable(byte[] data, byte[] key, byte[] hmac_key) throws SodiumException{
        
        byte[] out = Crypto_auth_hmacsha256.hmacsha256(data, hmac_key);
        byte[] nonce = new byte[CRYPTO_AEAD_CHACHA20POLY1305_IETF_NONCEBYTES];
        System.arraycopy(out, 0, nonce, 0, nonce.length);
        byte[] cipher = encrypt(data, nonce, key);
        
        byte[] merged = new byte[nonce.length + cipher.length];
        System.arraycopy(nonce, 0, merged, 0, nonce.length);
        System.arraycopy(cipher, 0, merged, nonce.length, cipher.length);
        
        return merged;
    }
    
    public static byte[] encryptAsNotSearchable(byte[] data, byte[] key) throws SodiumException {
        
        byte[] nonce = new byte[CRYPTO_AEAD_CHACHA20POLY1305_IETF_NONCEBYTES];
        Crypto_randombytes.buf(nonce);
        byte[] cipher = encrypt(data, nonce, key);
        byte[] merged = new byte[nonce.length + cipher.length];
        System.arraycopy(nonce, 0, merged, 0, nonce.length);
        System.arraycopy(cipher, 0, merged, nonce.length, cipher.length);
        return merged;
    }
    
    public static byte[] encrypt(byte[] data, byte[] nonce, byte[] key) throws SodiumException{
        return Crypto_aead_chacha20poly1305_ietf.encrypt(data, null, nonce, key);
    }
    
    static byte[] deriveKey(String passphrase, byte[] salt, Method key_derivation_method) throws SodiumException{
        
        int opslimit;
        int memlimit;
        
        byte[] credentials = passphrase.getBytes();

        if (key_derivation_method == Method.ARGON2I_INT) {
            opslimit = CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE;
            memlimit = CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE;
        } else {
            opslimit = CRYPTO_PWHASH_OPSLIMIT_MODERATE;
            memlimit = CRYPTO_PWHASH_MEMLIMIT_MODERATE;
        }

        return Crypto_pwhash.pwhash(credentials, salt, opslimit, memlimit);
    }
    
    public static byte[] decrypt(byte[] cipher, byte[] nonce, byte[] key) throws SodiumException{
        return Crypto_aead_chacha20poly1305_ietf.decrypt(cipher, null, nonce, key);
    }
    
    public static byte[] decryptMerged(byte[] data, byte[] key) throws SodiumException{
        byte[] nonce = new byte[CRYPTO_AEAD_CHACHA20POLY1305_IETF_NONCEBYTES];
        System.arraycopy( data, 0, nonce, 0, nonce.length );
        byte[] cipher  = new byte[data.length - nonce.length];
        System.arraycopy(data, nonce.length, cipher, 0, cipher.length );
        return decrypt(cipher, nonce, key);
    }
    
    public static byte[] hash256(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            return new byte[]{};
        }
    }
}
