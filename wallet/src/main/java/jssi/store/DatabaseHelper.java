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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import jssi.wallet.WalletConstants;

/**
 * Created by ITON Solutions on 05/04/2019.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    public DatabaseHelper(String database, Context context) {
        super(context, WalletConstants.WALLET_DIR + database, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
//        itemsTable(database);
//        metadataTable(database);
//        encryptedTable(database);
//        plaintextTable(database);
//        indexOnItemsTable(database);
//        indexOnEncryptedTable(database);
//        indexOnPlaintextTable(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }

    public static class Table {
        public static final String ITEMS = "items";
        public static final String METADATA = "metadata";
        public static final String TAGS_ENCRYPTED = "tags_encrypted";
        public static final String TAGS_PLAINTEXT = "tags_plaintext";
    }

    public static class Column {
        public static class Item {
            public static final String ID   = "id";
            public static final String TYPE = "type";
            public static final String NAME = "name";
            public static final String VALUE = "value";
            public static final String KEY   = "key";
        }

        public static class Metadata {
            public static final String ID    = "id";
            public static final String VALUE = "value";
        }

        public static class TagEncrypted{
            public static final String ITEM_ID = "item_id";
            public static final String NAME = "name";
            public static final String VALUE = "value";
        }

        public static class TagPlaintext{
            public static final String ITEM_ID = "item_id";
            public static final String NAME = "name";
            public static final String VALUE = "value";
        }
    }

    private void itemsTable(SQLiteDatabase database) {
        final String create = "CREATE TABLE "
                + Table.ITEMS + " ("
                + Column.Item.ID + " INTEGER NOT NULL, "
                + Column.Item.TYPE + " NOT NULL, "
                + Column.Item.NAME + " NOT NULL, "
                + Column.Item.VALUE + " NOT NULL, "
                + Column.Item.KEY + " NOT NULL, "
                + "PRIMARY KEY(" + Column.Item.ID + ")"
                + ");";
        database.execSQL(create);
    }

    private void indexOnItemsTable(SQLiteDatabase database) {
        database.execSQL("CREATE UNIQUE INDEX idx_items_type_name ON " + Table.ITEMS + "(" + Column.Item.TYPE + ", " + Column.Item.NAME + ");");
    }

    private void metadataTable(SQLiteDatabase database) {
        final String create = "CREATE TABLE "
                + Table.METADATA + " ("
                + Column.Metadata.ID + " INTEGER NOT NULL, "
                + Column.Metadata.VALUE + " NOT NULL, "
                + "PRIMARY KEY(" + Column.Metadata.ID + ")"
                + ");";
        database.execSQL(create);
    }

    private void encryptedTable(SQLiteDatabase database) {
        final String create = "CREATE TABLE "
                + Table.TAGS_ENCRYPTED + " ("
                + Column.TagEncrypted.NAME + " NOT NULL, "
                + Column.TagEncrypted.VALUE + " NOT NULL, "
                + Column.TagEncrypted.ITEM_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + Column.TagEncrypted.ITEM_ID + ") REFERENCES " + Table.ITEMS + "(" + Column.Item.ID + ") ON DELETE CASCADE ON UPDATE CASCADE, "
                + "PRIMARY KEY(" + Column.TagEncrypted.NAME + ", " + Column.TagEncrypted.ITEM_ID + ")"
                + ");";
        database.execSQL(create);
    }

    private void indexOnEncryptedTable(SQLiteDatabase database) {
        database.execSQL("CREATE INDEX idx_tags_encrypted_name ON " + Table.TAGS_ENCRYPTED + "(" + Column.TagEncrypted.NAME + ");");
        database.execSQL("CREATE INDEX idx_tags_encrypted_value ON " + Table.TAGS_ENCRYPTED + "(" + Column.TagEncrypted.VALUE + ");");
        database.execSQL("CREATE INDEX idx_tags_encrypted_item_id ON " + Table.TAGS_ENCRYPTED + "(" + Column.TagEncrypted.ITEM_ID + ");");
    }

    private void plaintextTable(SQLiteDatabase database) {
        final String create = "CREATE TABLE "
                + Table.TAGS_PLAINTEXT + " ("
                + Column.TagPlaintext.NAME + " TEXT NOT NULL, "
                + Column.TagPlaintext.VALUE + " TEXT NOT NULL, "
                + Column.TagPlaintext.ITEM_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + Column.TagPlaintext.ITEM_ID + ") REFERENCES " + Table.ITEMS + "(" + Column.Item.ID + ") ON DELETE CASCADE ON UPDATE CASCADE, "
                + "PRIMARY KEY(" + Column.TagPlaintext.NAME + ", " + Column.TagPlaintext.ITEM_ID + ")"
                + ");";
        database.execSQL(create);
    }

    private void indexOnPlaintextTable(SQLiteDatabase database) {
        database.execSQL("CREATE INDEX idx_tags_plaintext_name ON " + Table.TAGS_PLAINTEXT + "(" + Column.TagPlaintext.NAME + ");");
        database.execSQL("CREATE INDEX idx_tags_plaintext_value ON " + Table.TAGS_PLAINTEXT + "(" + Column.TagPlaintext.VALUE + ");");
        database.execSQL("CREATE INDEX idx_tags_plaintext_item_id ON " + Table.TAGS_PLAINTEXT + "(" + Column.TagPlaintext.ITEM_ID + ");");
    }
}
