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
import jssi.store.model.Encrypted;
import jssi.store.model.Item;
import jssi.store.model.Plaintext;
import org.libsodium.jni.SodiumException;

import java.util.*;

public class ItemTags {
    
    private Collection<Encrypted> encrypted = new ArrayList<>();
    private Collection<Plaintext> plaintext = new ArrayList<>();

    public ItemTags(){}
    
    public ItemTags(Item item){
        encrypted.addAll(item.getEncrypted());
        plaintext.addAll(item.getPlaintext());
    }

    public Map<String, String> decrypt(byte[] tagNameKey, byte[] tagValueKey) throws SodiumException{

        Map<String, String> decrypted = new HashMap<>();

        for (Encrypted tag : encrypted) {
            String name = new String(Crypto.decryptMerged(tag.getName(), tagNameKey));
            String value = new String(Crypto.decryptMerged(tag.getValue(), tagValueKey));
            decrypted.put(name, value);
        }
        for (Plaintext tag : plaintext) {
            String name = new String(Crypto.decryptMerged(tag.getName(), tagNameKey));
            String value = new String(tag.getValue());
            decrypted.put(String.format("~%s", name), value);
        }
        return decrypted;
    }

    public void encrypt(Item item, Map<String, String> tags, byte[] tagNameKey, byte[] tagValueKey, byte[] tagsHmacKey) throws SodiumException{

        for(String name : tags.keySet()) {
            if(name.startsWith("~")){
                byte[] encryptedValue = tags.get(name).getBytes();
                name = name.substring(1);
                byte[] encryptedName  = Crypto.encryptAsSearchable(name.getBytes(), tagNameKey, tagsHmacKey);
                plaintext.add(new Plaintext(item.getId(), encryptedName, encryptedValue));
            } else {
                String value = tags.get(name);
                byte[] encryptedName  = Crypto.encryptAsSearchable(name.getBytes(), tagNameKey, tagsHmacKey);
                byte[] encryptedValue = Crypto.encryptAsSearchable(value.getBytes(), tagValueKey, tagsHmacKey);
                encrypted.add(new Encrypted(item.getId(), encryptedName, encryptedValue));
            }
        }
    }

    public Collection<Encrypted> getEncrypted() {
        return encrypted;
    }

    public Collection<Plaintext> getPlaintext() {
        return plaintext;
    }
    
}
