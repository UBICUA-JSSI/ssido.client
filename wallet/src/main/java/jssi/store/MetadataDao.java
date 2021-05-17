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
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import jssi.store.model.Metadata;


public class MetadataDao {

    private static final String TAG = MetadataDao.class.getName();
    private SQLiteDatabase database;
    private DatabaseHelper helper;

    public MetadataDao(DatabaseHelper helper) {
        this.helper = helper;
        database = helper.getWritableDatabase();
    }

    public void create(Metadata metadata) {

        if(getCount() > 0){
            Log.e(TAG, String.format("Error: %s", "Metadata already exists"));
            return;
        }
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Column.Metadata.VALUE, metadata.getValue());
        database.insert(DatabaseHelper.Table.METADATA, null, values);
    }

    public Metadata getMetadata(int id) {
        Metadata result = null;

        Cursor cursor = database.query(
                DatabaseHelper.Table.METADATA,
                null,
                DatabaseHelper.Column.Metadata.ID + " = ? ",
                new String[] { String.valueOf(id) },
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            Wrapper wrapper = new Wrapper(cursor);
            result = wrapper.wrap();
        }

        cursor.close();
        return result;
    }

    public long getCount() {
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.Table.METADATA);
    }

    private class Wrapper extends CursorWrapper {

        public Wrapper(Cursor cursor) {
            super(cursor);
        }

        public Metadata wrap() {
            int id = getInt(getColumnIndex(DatabaseHelper.Column.Metadata.ID));
            byte[] value = getBlob(getColumnIndex(DatabaseHelper.Column.Metadata.VALUE));
            return new Metadata(id, value);
        }
    }
}
