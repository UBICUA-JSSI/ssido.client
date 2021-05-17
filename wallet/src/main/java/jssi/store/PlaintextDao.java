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

import jssi.store.model.Plaintext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author ITON Solutions
 */
public class PlaintextDao implements Serializable {

    private static final String TAG = PlaintextDao.class.getName();
    private SQLiteDatabase database;

    public PlaintextDao(DatabaseHelper helper) {
        database = helper.getWritableDatabase();
    }

    public long create(Plaintext tag)  {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Column.TagPlaintext.ITEM_ID, tag.getItemId());
        values.put(DatabaseHelper.Column.TagPlaintext.NAME, tag.getName());
        values.put(DatabaseHelper.Column.TagPlaintext.VALUE, tag.getValue());
        return database.insert(
                DatabaseHelper.Table.TAGS_PLAINTEXT,
                null,
                values);
    }

    public long create(Collection<Plaintext> tags)  {
        long result = 0;
        for(Plaintext tag : tags){
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.Column.TagPlaintext.ITEM_ID, tag.getItemId());
            values.put(DatabaseHelper.Column.TagPlaintext.NAME, tag.getName());
            values.put(DatabaseHelper.Column.TagPlaintext.VALUE, tag.getValue());
            result += database.insert(
                    DatabaseHelper.Table.TAGS_PLAINTEXT,
                    null,
                    values);
        }
        return result;
    }

    public int update(Collection<Plaintext> tags)  {
        int result = 0;
        for(Plaintext tag : tags){
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.Column.TagPlaintext.NAME, tag.getName());
            values.put(DatabaseHelper.Column.TagPlaintext.VALUE, tag.getValue());

            String[] item_id = {String.valueOf(tag.getItemId())};
            result += database.update(
                    DatabaseHelper.Table.TAGS_PLAINTEXT,
                    values, "item_id = ?",
                    item_id);
        }
        return result;
    }

    public int delete(Collection<Plaintext> tags)  {
        int result = 0;
        for(Plaintext tag : tags){
            String[] item_id = {String.valueOf(tag.getItemId())};
            result += database.delete(
                    DatabaseHelper.Table.TAGS_PLAINTEXT,
                    "item_id = ?",
                    item_id);
        }
        return result;
    }

    public List<Plaintext> queryForAll(int item_id) {
        List<Plaintext> plaintext = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseHelper.Table.TAGS_PLAINTEXT,
                null,
                DatabaseHelper.Column.TagPlaintext.ITEM_ID + " = ? ",
                new String[] { String.valueOf(item_id) },
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            Wrapper wrapper = new Wrapper(cursor);
            while (!wrapper.isAfterLast()) {
                Plaintext tag = wrapper.wrap();
                plaintext.add(tag);
                wrapper.moveToNext();
            }
        }

        cursor.close();
        return plaintext;
    }

    private class Wrapper extends CursorWrapper {

        Wrapper(Cursor cursor) {
            super(cursor);
        }

        Plaintext wrap() {
            int item_id  = getInt(getColumnIndex(DatabaseHelper.Column.TagPlaintext.ITEM_ID));
            byte[] name  = getBlob(getColumnIndex(DatabaseHelper.Column.TagPlaintext.NAME));
            byte[] value = getBlob(getColumnIndex(DatabaseHelper.Column.TagPlaintext.VALUE));
            return new Plaintext(item_id, name, value);
        }
    }
}
