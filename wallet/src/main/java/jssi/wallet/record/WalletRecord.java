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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import jssi.wallet.crypto.Crypto;
import jssi.wallet.crypto.Keys;
import jssi.store.model.Item;
import org.libsodium.jni.SodiumException;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

/**
 *
 * @author ITON Solutions
 */
public class WalletRecord {
    
    private String name;
    private String value;
    private String type;
    private Map<String, String> tags = new HashMap<>();
    
    public WalletRecord(){}

    public WalletRecord(String type, String name , String value){
        this(type, name, value, null);
    }

    public WalletRecord(String type, String name, String value, Map<String, String> tags){
        this.type = type;
        this.name = name;
        this.value = value;
        this.tags = tags == null ? this.tags : tags;
    }

    public WalletRecord decrypt(final Item item, final Keys keys) throws SodiumException{
  
        name = new String(Crypto.decryptMerged(item.getName(), keys.getNameKey()));
        type = new String(Crypto.decryptMerged(item.getType(), keys.getTypeKey()));
        value = new ItemValue(item).decrypt(keys.getValueKey());
        
        ItemTags itemTags = new ItemTags(item);
        tags = itemTags.decrypt(keys.getTagNameKey(), keys.getTagValueKey());
        return this;
    }
    
    public Item encrypt(final Keys keys) throws SodiumException{
        
        byte[] encryptedType = type == null ? new byte[0]
                : Crypto.encryptAsSearchable(type.getBytes(), keys.getTypeKey(), keys.getItemHmacKey());
        byte[] encryptedName = name == null ? new byte[0]
                : Crypto.encryptAsSearchable(name.getBytes(), keys.getNameKey(), keys.getItemHmacKey());
        
        ItemValue itemValue = new ItemValue();
        byte[] encryptedValue = itemValue.encrypt(value.getBytes(), keys.getValueKey()).getValue();
        byte[] encryptedKey   = itemValue.getKey();
        
        Item item = new Item(encryptedType, encryptedName, encryptedValue, encryptedKey);
        ItemTags itemTags = new ItemTags();
        itemTags.encrypt(item, tags, keys.getTagNameKey(), keys.getTagValueKey(), keys.getTagsHmacKey());
        item.setEncrypted(itemTags.getEncrypted());
        item.setPlaintext(itemTags.getPlaintext());
        return item;
    }
    
    public WalletRecord deserialize(byte[] msg) throws IOException{
        
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(msg);
        unpacker.unpackArrayHeader();
            this.type  = unpacker.unpackString();
            this.name  = unpacker.unpackString();
            this.value = unpacker.unpackString();

            int size = unpacker.unpackMapHeader();
        
                while (size-- > 0) {
                    String key = unpacker.unpackString();
                    String tag = unpacker.unpackString();
                    tags.put(key, tag);
                }
        
        return this;
    }
    
    public byte[] serialize() throws IOException{
        
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packArrayHeader(4);
            packer.packString(type);
            packer.packString(name);
            packer.packString(value);

            packer.packMapHeader(tags.size() );
            for(String key : tags.keySet()){
                packer.packString(key);
                packer.packString(tags.get(key));
            }
        return packer.toByteArray();
    }
    

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
