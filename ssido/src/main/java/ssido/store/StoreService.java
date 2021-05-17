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

package ssido.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import jssi.did.Did;
import jssi.store.PreexistingEntityException;
import jssi.wallet.Wallet;
import jssi.wallet.record.WalletRecord;
import org.libsodium.jni.SodiumException;

import java.io.IOException;
import java.util.List;

/**
 * Created by ITON Solutions on 11/04/2019.
 */
public class StoreService {
    private static final String TAG = StoreService.class.getName();

    public static final String SSIDO_STORE = "ssido";
    private static final String SSIDO_FIRST_LAUNCH = "first.launch";
    private static final String SSIDO_USERNAME = "username";
    private static final String SSIDO_SEEN_BACKUP_PASSPHRASE = "seen.backup.passphrase";
    private static final String SSIDO_DID = "did";
    private static final String SSIDO_COUNTER = "counter";

    private SharedPreferences preference;

    public StoreService(Context context) {
        preference = context.getSharedPreferences(SSIDO_STORE, Context.MODE_PRIVATE);
    }

    public boolean isFirstLaunch() {
        return preference.getBoolean(SSIDO_FIRST_LAUNCH, true);
    }

    public void setCounter(int counter) {
        preference.edit().putInt(SSIDO_COUNTER, counter).apply();
    }

    public Integer getCounter() {
        return preference.getInt(SSIDO_COUNTER, 0);
    }

    public void setUsername(String username) {
        preference.edit().putString(SSIDO_USERNAME, username).apply();
    }

    public String getUsername() {
        return preference.getString(SSIDO_USERNAME, "");
    }

    public void setDid(Did did) {
        try {
            String result = new ObjectMapper().writeValueAsString(did);
            preference.edit()
                    .putBoolean(SSIDO_FIRST_LAUNCH, Boolean.FALSE)
                    .putInt(SSIDO_COUNTER, 0)
                    .putString(SSIDO_DID, result)
                    .apply();
        } catch(IOException e){
            Log.e(TAG, String.format("Error: %s", e.getCause().getMessage()));
        }
    }

    public Did getDid() {
        String did = preference.getString(SSIDO_DID, null);
        try {
            return did == null ? null : new ObjectMapper().readValue(did, Did.class);
        } catch(IOException e){
            Log.e(TAG, String.format("Error: %s", e.getCause().getMessage()));
            return null;
        }
    }

    public static void addRecord(Wallet wallet, String type, String name, String value) {

        WalletRecord record = new WalletRecord(type, name, value);
        try{
            wallet.addRecord(record);
        } catch(SodiumException | PreexistingEntityException e){
            Log.e(TAG, String.format("Error: %s", e.getCause().getMessage()));
        }
    }

    public static WalletRecord getRecord(Wallet wallet, String type) {
        try{
            List<WalletRecord> records = wallet.findRecords(type);
            return records.get(0);
        } catch(SodiumException e){
            Log.e(TAG, String.format("Error: %s", e.getCause().getMessage()));
            return null;
        }
    }
}
