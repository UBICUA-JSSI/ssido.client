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
import org.libsodium.jni.SodiumException;

/**
 *
 * @author ITON Solutions
 */
class Encrypter {
    
    private final byte[] key;
    private final byte[] nonce;
    private final int chunkSize;
            
    
    Encrypter(final byte[] key, byte[] nonce, int chunkSize){
        this.key = key;
        this.nonce = nonce;
        this.chunkSize = chunkSize;
    }
    
    byte [] encrypt(ByteBuffer buffer) throws IOException, SodiumException{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        while(buffer.position() < buffer.limit()){
            byte[] chunk = new byte[Math.min(chunkSize, buffer.limit() - buffer.position())];
            buffer.get(chunk);
            byte[] cipher = Crypto.encrypt(chunk, nonce, key);
            baos.write(cipher);
            Crypto_randombytes.increment(nonce);
        }
        return baos.toByteArray();
    }
}
