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
package jssi.wallet;

import android.util.Log;


import jssi.store.DatabaseHelper;
import jssi.store.PreexistingEntityException;
import jssi.wallet.crypto.Crypto;
import jssi.wallet.crypto.Keys;
import jssi.store.model.Item;
import jssi.wallet.record.ItemTags;
import jssi.wallet.record.ItemValue;
import jssi.wallet.record.WalletRecord;
import jssi.store.EncryptedDao;
import jssi.store.ItemDao;
import jssi.store.PlaintextDao;
import org.libsodium.jni.SodiumException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet {

    private static final String TAG = Wallet.class.getName();

    private final String id;
    private final Keys keys;
    private final ItemDao itemDao;
    private final EncryptedDao encryptedDao;
    private final PlaintextDao plaintextDao;
    
    Wallet(String id, Keys keys, DatabaseHelper helper) {
        this.id = id;
        this.keys = keys;
        this.itemDao = new ItemDao(helper);
        this.encryptedDao = new EncryptedDao(helper);
        this.plaintextDao = new PlaintextDao(helper);
    }
    
    public WalletRecord findRecord(String type, String name) throws SodiumException {

        Item item = findItem(type, name);
        if(item == null){
            return null;
        }
        WalletRecord record = new WalletRecord();
        return record.decrypt(item, keys);
    }

    public List<WalletRecord> findAllRecords() throws SodiumException {

        List<WalletRecord> records = new ArrayList<>();

        List<Item> items = itemDao.queryForAll();
        for(Item item : items) {
            records.add(new WalletRecord().decrypt(item, keys));
        }
        return records;
    }

    public List<WalletRecord> findRecords(String type) throws SodiumException {

        List<WalletRecord> records = new ArrayList<>();
        byte[] encryptedType = type == null ? new byte[0]
                : Crypto.encryptAsSearchable(type.getBytes(), keys.getTypeKey(), keys.getItemHmacKey());

        List<Item> items = itemDao.queryForType(encryptedType);
        for(Item item : items) {
            records.add(new WalletRecord().decrypt(item, keys));
        }
        return records;
    }


    public void addRecordTags(WalletRecord record, Map<String, String> tags) throws SodiumException {

        Item item = findItem(record.getType(), record.getName());

        if(item == null){
            return;
        }

        ItemTags itemTags = new ItemTags();
        itemTags.encrypt(item, tags, keys.getTagNameKey(), keys.getTagValueKey(), keys.getTagsHmacKey());
        encryptedDao.create(itemTags.getEncrypted());
        plaintextDao.create(itemTags.getPlaintext());
    }

    public void deleteRecordTags(WalletRecord record, Map<String, String> tags) throws SodiumException {

        Item item = findItem(record.getType(), record.getName());

        if(item == null){
            return;
        }

        ItemTags itemTags = new ItemTags();
        itemTags.encrypt(item, tags, keys.getTagNameKey(), keys.getTagValueKey(), keys.getTagsHmacKey());
        encryptedDao.delete(itemTags.getEncrypted());
        plaintextDao.delete(itemTags.getPlaintext());
    }


    public Item addRecord(WalletRecord record) throws SodiumException, PreexistingEntityException {
        Item item = record.encrypt(keys);
        long result = itemDao.create(item);

        if(result == 0){
            throw new PreexistingEntityException("Item already exists");
        }

        encryptedDao.create(item.getEncrypted());
        plaintextDao.create(item.getPlaintext());
        return item;
    }
    
    public long count() {
        return itemDao.getCount();
    }

    public void deleteRecord(WalletRecord record) {
        deleteRecord(record.getType(), record.getName());
    }
    
    public void deleteRecord(String type, String name) {

        Item item = findItem(type, name);

        if(item == null){
            return;
        }

        encryptedDao.delete(item.getEncrypted());
        plaintextDao.delete(item.getPlaintext());
        itemDao.delete(item);
    }

    public void updateRecordValue(WalletRecord record, String value) throws SodiumException {

        Item item = findItem(record.getType(), record.getName());

        if(item == null){
            return;
        }

        ItemValue itemValue = new ItemValue(item);
        itemValue = itemValue.encrypt(value.getBytes(), keys.getValueKey());
        item.setValue(itemValue.getValue());
        item.setKey(itemValue.getKey());
        itemDao.update(item);
    }

    public void updateRecordTags(WalletRecord record, Map<String, String> tags) throws SodiumException {

        Item item = findItem(record.getType(), record.getName());
        if(item == null){
            return;
        }

        Map<String, String> aggregated = new HashMap<>();
        for(String element : tags.keySet()){
            if(record.getTags().keySet().contains(element)){
                aggregated.put(element, tags.get(element));
            }
        }

        ItemTags itemTags = new ItemTags();
        itemTags.encrypt(item, aggregated, keys.getTagNameKey(), keys.getTagValueKey(), keys.getTagsHmacKey());
        encryptedDao.update(itemTags.getEncrypted());
        plaintextDao.update(itemTags.getPlaintext());
    }

    public String getId() {
        return id;
    }

    private Item findItem(String type, String name){
        Item item = null;
        try {
            byte[] encryptedType = type == null ? new byte[0]
                    : Crypto.encryptAsSearchable(type.getBytes(), keys.getTypeKey(), keys.getItemHmacKey());
            byte[] encryptedName = name == null ? new byte[0]
                    : Crypto.encryptAsSearchable(name.getBytes(), keys.getNameKey(), keys.getItemHmacKey());

            item = itemDao.queryForFirst(encryptedType, encryptedName);
        } catch (SodiumException e){
            Log.e(TAG, String.format("Error: %s", e.getMessage()));
        }
        return item;
    }
}
