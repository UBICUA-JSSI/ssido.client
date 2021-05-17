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
package jssi.wallet.io;

import android.util.Log;

import jssi.wallet.Wallet;
import jssi.wallet.crypto.Crypto;
import jssi.wallet.crypto.KeyDerivationData;
import jssi.wallet.record.WalletRecord;
import jssi.wallet.util.Utils;
import org.libsodium.jni.SodiumException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import io.reactivex.ObservableEmitter;

public class Writer implements Runnable {

    private static final String TAG = Writer.class.getName();
    
    private Wallet wallet;
    private ObservableEmitter<Integer> emitter;
    private IOConfig config;

    public Writer(Wallet wallet, IOConfig config, ObservableEmitter<Integer> emitter) {
        this.wallet = wallet;
        this.config = config;
        this.emitter = emitter;
    }

    @Override
    public void run() {

        try {
            File file = new File(config.path);
            if (!file.mkdirs()) {
                Log.e(TAG, "Directory not created");
            }

            int count = (int) wallet.count();
            Log.d(TAG, String.format("Total registers in database %d", count));

            KeyDerivationData data = new KeyDerivationData(config.key);
            Header header = new Header();
            byte[] header_bytes = header.serialize(data);
            List<WalletRecord> records = wallet.findAllRecords();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(Crypto.hash256(header_bytes));

            for (WalletRecord record : records) {
                byte[] decrypted = record.serialize();
                baos.write(Utils.toBytes(decrypted.length));
                baos.write(decrypted);
                emitter.onNext(count--);
            }

            baos.write(Utils.toBytes(0));

            byte[] encrypted = new Encrypter(header.getDerivationData().deriveMasterKey(),
                    header.getNonce(),
                    header.getChunkSize()).encrypt(ByteBuffer.wrap(baos.toByteArray()));

            baos = new ByteArrayOutputStream();
            baos.write(Utils.toBytes(header_bytes.length));
            baos.write(header_bytes);
            baos.write(encrypted);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                baos.writeTo(fos);
            }

            emitter.onComplete();

        } catch (IOException | SodiumException e) {
            Log.e(TAG, String.format("Error %s", e.getMessage()));
            emitter.onError(e);
        }
    }
}
