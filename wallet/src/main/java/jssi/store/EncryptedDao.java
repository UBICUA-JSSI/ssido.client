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
import android.database.sqlite.SQLiteDatabase;

import jssi.store.model.Encrypted;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class EncryptedDao implements Serializable {

    private static final String TAG = EncryptedDao.class.getName();
    private SQLiteDatabase database;

    public EncryptedDao(DatabaseHelper helper) {
        database = helper.getWritableDatabase();
    }

    public long create(Encrypted tag)  {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Column.TagEncrypted.ITEM_ID, tag.getItemId());
        values.put(DatabaseHelper.Column.TagEncrypted.NAME, tag.getName());
        values.put(DatabaseHelper.Column.TagEncrypted.VALUE, tag.getValue());
        return database.insert(
                DatabaseHelper.Table.TAGS_ENCRYPTED,
                null,
                values);
    }

    public long create(Collection<Encrypted> tags)  {
        long result = 0;
        for(Encrypted tag : tags){
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.Column.TagEncrypted.ITEM_ID, tag.getItemId());
            values.put(DatabaseHelper.Column.TagEncrypted.NAME, tag.getName());
            values.put(DatabaseHelper.Column.TagEncrypted.VALUE, tag.getValue());
            result += database.insert(
                    DatabaseHelper.Table.TAGS_ENCRYPTED,
                    null,
                    values);
        }
        return result;
    }

    public int delete(Collection<Encrypted> tags)  {
        int result = 0;
        for(Encrypted tag : tags){
            String[] item_id = {String.valueOf(tag.getItemId())};
            result += database.delete(
                    DatabaseHelper.Table.TAGS_ENCRYPTED,
                    "item_id = ?",
                    item_id);
        }
        return result;
    }

    public int update(Collection<Encrypted> tags)  {
        int result = 0;
        for(Encrypted tag : tags){
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.Column.TagEncrypted.NAME, tag.getName());
            values.put(DatabaseHelper.Column.TagEncrypted.VALUE, tag.getValue());

            String[] item_id = {String.valueOf(tag.getItemId())};
            result += database.update(
                    DatabaseHelper.Table.TAGS_ENCRYPTED,
                    values, "item_id = ?",
                    item_id);
        }
        return result;
    }

    public List<Encrypted> queryForAll(int item_id) {
        List<Encrypted> encrypted = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseHelper.Table.TAGS_ENCRYPTED,
                null,
                DatabaseHelper.Column.TagEncrypted.ITEM_ID + " = ? ",
                new String[] { String.valueOf(item_id) },
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            Wrapper wrapper = new Wrapper(cursor);
            while (!wrapper.isAfterLast()) {
                Encrypted tag = wrapper.wrap();
                encrypted.add(tag);
                wrapper.moveToNext();
            }
        }

        cursor.close();
        return encrypted;
    }

    private class Wrapper extends CursorWrapper {

        Wrapper(Cursor cursor) {
            super(cursor);
        }

        Encrypted wrap() {
            int item_id  = getInt(getColumnIndex(DatabaseHelper.Column.TagEncrypted.ITEM_ID));
            byte[] name  = getBlob(getColumnIndex(DatabaseHelper.Column.TagEncrypted.NAME));
            byte[] value = getBlob(getColumnIndex(DatabaseHelper.Column.TagEncrypted.VALUE));
            return new Encrypted(item_id, name, value);
        }
    }
}
