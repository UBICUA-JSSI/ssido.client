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
package jssi.store;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;

import jssi.store.model.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ITON Solutions
 */
public class ItemDao implements Serializable {

    private static final String TAG = ItemDao.class.getName();
    private SQLiteDatabase database;
    private DatabaseHelper helper;

    public ItemDao(DatabaseHelper helper) {
        this.helper = helper;
        database = helper.getWritableDatabase();
    }

    public long create(Item item) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.Column.Item.TYPE, item.getType());
        values.put(DatabaseHelper.Column.Item.NAME, item.getName());
        values.put(DatabaseHelper.Column.Item.VALUE, item.getValue());
        values.put(DatabaseHelper.Column.Item.KEY, item.getKey());
        long result = database.insert(DatabaseHelper.Table.ITEMS, null, values);

        EncryptedDao encrypted = new EncryptedDao(helper);
        PlaintextDao plaintext = new PlaintextDao(helper);
        encrypted.create(item.getEncrypted());
        plaintext.create(item.getPlaintext());
        return result;
    }

    public int update(Item item) {
        int result = 0;

        EncryptedDao encrypted = new EncryptedDao(helper);
        PlaintextDao plaintext = new PlaintextDao(helper);
        encrypted.update(item.getEncrypted());
        plaintext.update(item.getPlaintext());

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Column.Item.TYPE, item.getType());
        values.put(DatabaseHelper.Column.Item.NAME, item.getName());
        values.put(DatabaseHelper.Column.Item.VALUE, item.getValue());
        values.put(DatabaseHelper.Column.Item.KEY, item.getKey());

        String[] item_id = {String.valueOf(item.getId())};
        result += database.update(
                DatabaseHelper.Table.ITEMS,
                values, "id = ?",
                item_id);


        return result;
    }

    public int delete(Item item) {
        String[] id = {String.valueOf(item.getId())};
        database.delete(DatabaseHelper.Table.TAGS_ENCRYPTED, "item_id = ?", id);
        database.delete(DatabaseHelper.Table.TAGS_PLAINTEXT, "item_id = ?", id);
        return database.delete(DatabaseHelper.Table.ITEMS, "id = ?", id);
    }

    public List<Item> queryForAll() {
        List<Item> items = new ArrayList<>();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + DatabaseHelper.Table.ITEMS,
                null);

        if (cursor.moveToFirst()) {
            Wrapper wrapper = new Wrapper(cursor);
            while (!wrapper.isAfterLast()) {
                Item item = wrapper.wrap();

                EncryptedDao encrypted = new EncryptedDao(helper);
                PlaintextDao plaintext = new PlaintextDao(helper);
                item.setEncrypted(encrypted.queryForAll(item.getId()));
                item.setPlaintext(plaintext.queryForAll(item.getId()));

                items.add(item);
                wrapper.moveToNext();
            }
        }

        cursor.close();
        return items;
    }

    public List<Item> queryForType(byte[] type) {
        List<Item> items = new ArrayList<>();

        final SQLiteDatabase.CursorFactory factory = (db, masterQuery, editTable, query) -> {
            query.bindBlob(1, type);
            return new SQLiteCursor(masterQuery, editTable, query);
        };

        final Cursor cursor = database.queryWithFactory(
                factory,
                false, DatabaseHelper.Table.ITEMS,
                null,
                DatabaseHelper.Column.Item.TYPE + " = ?",
                null,
                null, null,
                null,
                null);

        if (cursor.moveToFirst()) {
            Wrapper wrapper = new Wrapper(cursor);
            while (!wrapper.isAfterLast()) {
                Item item = wrapper.wrap();

                EncryptedDao encrypted = new EncryptedDao(helper);
                PlaintextDao plaintext = new PlaintextDao(helper);
                item.setEncrypted(encrypted.queryForAll(item.getId()));
                item.setPlaintext(plaintext.queryForAll(item.getId()));

                items.add(item);
                wrapper.moveToNext();
            }
        }

        cursor.close();
        return items;
    }

    public Item queryForFirst(byte[] type, byte[] name) {
        Item item = null;

        final SQLiteDatabase.CursorFactory factory = (db, masterQuery, editTable, query) -> {
            query.bindBlob(1, type);
            query.bindBlob(2, name);
            return new SQLiteCursor(masterQuery, editTable, query);
        };

        final Cursor cursor = database.queryWithFactory(
                factory,
                false, DatabaseHelper.Table.ITEMS,
                null,
                DatabaseHelper.Column.Item.TYPE + " = ? AND " + DatabaseHelper.Column.Item.NAME + " = ?",
                null,
                null, null,
                null,
                null);

        if (cursor.moveToFirst()) {
            Wrapper wrapper = new Wrapper(cursor);
                item = wrapper.wrap();

                EncryptedDao encrypted = new EncryptedDao(helper);
                PlaintextDao plaintext = new PlaintextDao(helper);
                item.setEncrypted(encrypted.queryForAll(item.getId()));
                item.setPlaintext(plaintext.queryForAll(item.getId()));
        }

        cursor.close();
        return item;

    }

    public long getCount() {
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.Table.ITEMS);
    }

    private class Wrapper extends CursorWrapper {

        Wrapper(Cursor cursor) {
            super(cursor);
        }

        Item wrap() {
            int id = getInt(getColumnIndex(DatabaseHelper.Column.Item.ID));
            byte[] type = getBlob(getColumnIndex(DatabaseHelper.Column.Item.TYPE));
            byte[] name = getBlob(getColumnIndex(DatabaseHelper.Column.Item.NAME));
            byte[] value = getBlob(getColumnIndex(DatabaseHelper.Column.Item.VALUE));
            byte[] key = getBlob(getColumnIndex(DatabaseHelper.Column.Item.KEY));
            return new Item(id, type, name, value, key);
        }
    }

}
