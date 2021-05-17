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

import java.io.IOException;
import java.util.Date;
import jssi.wallet.crypto.KeyDerivationData;
import jssi.wallet.crypto.Method;
import static org.libsodium.jni.SodiumConstants.CRYPTO_AEAD_CHACHA20POLY1305_IETF_KEYBYTES;
import static org.libsodium.jni.SodiumConstants.CRYPTO_AEAD_CHACHA20POLY1305_IETF_NONCEBYTES;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.ArrayValue;


/**
 *
 * @author ITON Solutions
 */
public class Header {
    
    public static final int CHUNK_SIZE = 1024;
    
    private KeyDerivationData data;
    private byte[] nonce = new byte[CRYPTO_AEAD_CHACHA20POLY1305_IETF_NONCEBYTES];
    private int chunkSize = CHUNK_SIZE;
    
    private Date date = new Date();
    private int version = 0;
    
    Header(){}
    
    byte[] serialize(KeyDerivationData data) throws IOException{
        
        this.data = data;
        
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        
        packer.packArrayHeader(3);
            packer.packArrayHeader(2);
                packer.packInt(data.getDerivationMethod().getId());
                packer.packArrayHeader(3);
                    packer.packArrayHeader(data.getSalt().length);
                    for(byte item : data.getSalt()){
                        packer.packInt(item & 0xFF);
                    }
                    packer.packArrayHeader(nonce.length);
                    for(byte item : nonce){
                         packer.packInt(item & 0xFF);
                    }
                    
                    packer.packInt(chunkSize);
       
            packer.packLong(date.getTime() / 1000);
            packer.packInt(version);    
        
        return packer.toByteArray();
    }
    
    Header deserialize(byte[] msg, String passphrase) throws IOException{
        
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(msg);
        unpacker.unpackArrayHeader();
            unpacker.unpackArrayHeader();

                Method method = Method.values()[unpacker.unpackInt()];
                
                unpacker.unpackArrayHeader();
                byte[] salt  = new byte[CRYPTO_AEAD_CHACHA20POLY1305_IETF_KEYBYTES];
                toBytes(unpacker.unpackValue().asArrayValue(), salt);
                toBytes(unpacker.unpackValue().asArrayValue(), nonce);

                chunkSize = unpacker.unpackInt();
                data = new KeyDerivationData(passphrase, salt, method);
           

        date = new Date(unpacker.unpackInt() * 1000);
        version = unpacker.unpackInt();
        
        return this;
    }
    
    private void toBytes(ArrayValue src, byte[] dst){
        for(int i = 0; i < src.size(); i++){
            dst[i] = src.get(i).asNumberValue().toByte();
        }
    }

    public Method getMethod() {
        return data.getDerivationMethod();
    }


    public byte[] getNonce() {
        return nonce;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public Date getDate() {
        return date;
    }

    public int getVersion() {
        return version;
    }

    public KeyDerivationData getDerivationData() {
        return data;
    }
}
