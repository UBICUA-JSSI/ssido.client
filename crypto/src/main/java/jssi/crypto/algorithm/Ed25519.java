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

import java.util.Map;

import org.bitcoinj.core.Base58;
import jssi.crypto.CryptoException;
import jssi.crypto.Keys;
import org.libsodium.api.Crypto_box;
import org.libsodium.api.Crypto_randombytes;
import org.libsodium.api.Crypto_sign_ed25519;
import org.libsodium.jni.SodiumConstants;
import static org.libsodium.jni.SodiumConstants.CRYPTO_BOX_CURVE25519XCHACHA20POLY1305_NONCEBYTES;
import static org.libsodium.jni.SodiumConstants.CRYPTO_SIGN_ED25519_SECRETKEYBYTES;
import org.libsodium.jni.SodiumException;

/**
 *
 * @author UBICUA
 */
public class Ed25519 implements ICrypto{

    @Override
    public Keys createKeys(byte[] seed) throws SodiumException {
;
        Map<String, byte[]> result;
        
        if(seed == null){
            result = Crypto_sign_ed25519.keypair();
        } else { 
            result = Crypto_sign_ed25519.seed_keypair(seed);
        }
        byte[] pk = result.get("pk");
        byte[] sk = result.get("sk");
        
        Keys keys = new Keys(Base58.encode(pk), Base58.encode(sk));
        return keys;
    }
    
    @Override
    public byte[] sign(byte[] data, byte[] sk) throws SodiumException, CryptoException{
        if(sk.length != CRYPTO_SIGN_ED25519_SECRETKEYBYTES){
            throw new CryptoException(String.format("Invalid signkey length %d, expected %d", sk.length, SodiumConstants.CRYPTO_SIGN_SECRETKEYBYTES));
        }
        return Crypto_sign_ed25519.sign(data, sk);
    }
    
    @Override
    public boolean verify(byte[] data, byte[] sign, byte[] pk) throws SodiumException{
        return Crypto_sign_ed25519.verify(data, sign, pk);
    }
    
    @Override
    public byte[] genNonce() throws SodiumException {
        byte[] nonce = new byte[CRYPTO_BOX_CURVE25519XCHACHA20POLY1305_NONCEBYTES];
        Crypto_randombytes.randombytes(nonce);
        return nonce;
    }
    
    /*
     * crypto_box encrypts and authenticates a message `data` using the senders secret key `sk`,
     * the receivers public key `pk` and a nonce `nonce`. It returns a ciphertext `cipher`.
     */
    @Override
    public byte[] cryptoBox(byte[] data, byte[] nonce, byte[] verkey, byte[] signkey) throws SodiumException {

        byte[] sk = Crypto_sign_ed25519.sk_to_curve25519(signkey);
        byte[] pk = Crypto_sign_ed25519.pk_to_curve25519(verkey);
        return Crypto_box.easy(data, nonce, pk, sk);
    }
    
    /*
     * crypto_box_open verifies and decrypts a ciphertext `cipher` using the receiver's secret key `sk`,
     * the senders public key `pk`, and a nonce `nonce`. It returns a plaintext `data)`.
     * If the ciphertext fails verification, throws SodiumException.
     */
    @Override
    public byte[] cryptoBoxOpen(byte[] cipher, byte[] nonce, byte[] verkey, byte[] signkey) throws SodiumException {
        
        byte[] sk = Crypto_sign_ed25519.sk_to_curve25519(signkey);
        byte[] pk = Crypto_sign_ed25519.pk_to_curve25519(verkey);
        return Crypto_box.open_easy(cipher, nonce, pk, sk);
    }
    
    
    @Override
    public byte[] cryptoBoxSeal(byte[] data, byte[] verkey) throws SodiumException {
        
        byte[] pk = Crypto_sign_ed25519.pk_to_curve25519(verkey);
        return Crypto_box.seal(data, pk);
    }

    @Override
    public byte[] cryptoBoxSealOpen(byte[] cipher, byte[] verkey, byte[] signkey) throws SodiumException {
        
        byte[] pk = Crypto_sign_ed25519.pk_to_curve25519(verkey);
        byte[] sk = Crypto_sign_ed25519.sk_to_curve25519(signkey);
        return Crypto_box.seal_open(cipher, pk, sk);
    }

    @Override
    public CryptoType getType() {
        return CryptoType.DEFAULT_CRYPTO_TYPE;
    }

    @Override
    public void validateKeys(String verkey) {
    }
}
