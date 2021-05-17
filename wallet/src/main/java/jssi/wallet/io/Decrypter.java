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
package jssi.wallet.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import jssi.wallet.crypto.Crypto;
import org.libsodium.api.Crypto_randombytes;
import static org.libsodium.jni.SodiumConstants.CRYPTO_AEAD_CHACHA20POLY1305_IETF_TAGBYTES;
import org.libsodium.jni.SodiumException;

/**
 *
 * @author ITON Solutions
 */
class Decrypter {
    
    private final byte[] key;
    private final byte[] nonce;
    private final int chunkSize;
            
    
    Decrypter(final byte[] key, byte[] nonce, int chunkSize){
        this.key = key;
        this.nonce = nonce;
        this.chunkSize = chunkSize;
        
    }
     
    ByteBuffer decrypt(ByteBuffer buffer) throws IOException, SodiumException{
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while (buffer.position() < buffer.limit()) {
            int read = Math.min(chunkSize + CRYPTO_AEAD_CHACHA20POLY1305_IETF_TAGBYTES, buffer.limit() - buffer.position());
            byte[] data = new byte[read];
            buffer.get(data);
            byte[] decrypted = Crypto.decrypt(data, nonce, key);
            baos.write(decrypted);
            Crypto_randombytes.increment(nonce);
        }
        return ByteBuffer.wrap(baos.toByteArray());
    }
}
