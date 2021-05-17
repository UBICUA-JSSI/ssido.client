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

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import jssi.store.DatabaseHelper;
import jssi.store.MetadataDao;
import jssi.store.model.Metadata;
import jssi.wallet.crypto.KeyDerivationData;
import jssi.wallet.crypto.Keys;
import jssi.wallet.crypto.KeysMetadata;
import jssi.wallet.io.IOConfig;
import org.libsodium.api.Crypto_randombytes;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static org.libsodium.jni.SodiumConstants.CRYPTO_AEAD_CHACHA20POLY1305_IETF_KEYBYTES;
/**
 *
 * @author ITON Solutions
 */
public class WalletService {
    
    private static final String TAG = WalletService.class.getName();
    
    private KeysMetadata keysMetadata;
    private KeyDerivationData keyDerivationData;
    private Keys keys;
    private final WalletCredential credential;
    private final Context context;
    private final DatabaseHelper helper;
    private Wallet wallet;
    
    public WalletService(final Context context, final WalletCredential credential, DatabaseHelper helper) {
        this.credential = credential;
        this.context = context;
        this.helper = helper;
    }
    
    public Observable<Wallet> open(){
        if(wallet == null) {
            Log.d(TAG, "Open wallet");
            return Observable.fromCallable(() -> {
                Metadata metadata = new MetadataDao(helper).getMetadata(1);
                keysMetadata = new ObjectMapper()
                        .readerFor(KeysMetadata.class)
                        .readValue(metadata.getValue());
                keyDerivationData = new KeyDerivationData(credential.key, keysMetadata);
                keys = new Keys().deserialize(keysMetadata.getKeys(), keyDerivationData.deriveMasterKey());
                wallet = new Wallet(credential.id, keys, helper);
                return wallet;
            });
        } else {
            Log.d(TAG, "Wallet already open");
            return Observable.just(wallet);
        }
    }

    public Observable<Boolean> close(){
        wallet = null;
        return Observable.just(Boolean.TRUE);
    }
    
    public Observable<Integer> export(final IOConfig config) {
        return open().flatMap((Function<Wallet, Observable<Integer>>) wallet -> {
            WalletExport export = new WalletExport(wallet);
            return export.export(config);
        });
    }
    
    public Observable<Integer> restore(final IOConfig config) {
        
        return open().flatMap((Function<Wallet, Observable<Integer>>) wallet -> {
            WalletImport restore = new WalletImport(wallet);
            return restore.restore(config);
        });
    }
    
    public Observable<Boolean> create() {
        return Observable.fromCallable(() -> {

            byte[] salt = new byte[CRYPTO_AEAD_CHACHA20POLY1305_IETF_KEYBYTES];
            Crypto_randombytes.buf(salt);
            keyDerivationData = new KeyDerivationData(credential.key, salt);
            keys = new Keys().init();
            keysMetadata = new KeysMetadata(keys.serialize(keyDerivationData.deriveMasterKey()), salt);

            Metadata metadata = new Metadata(keysMetadata.toString().getBytes());
            DatabaseHelper helper = new DatabaseHelper("backup", context);
            new MetadataDao(helper).create(metadata);
            return Boolean.TRUE;
        });
    }

    public Wallet getWallet() {
        return wallet;
    }
}
