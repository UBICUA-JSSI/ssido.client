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

import jssi.store.PreexistingEntityException;
import jssi.wallet.Wallet;
import jssi.wallet.crypto.Crypto;
import jssi.wallet.record.WalletRecord;
import jssi.wallet.util.Utils;
import org.libsodium.jni.SodiumException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.ObservableEmitter;

/**
 *
 * @author ITON Solutions
 */
public class Reader implements Runnable {

    private static final String TAG = Reader.class.getName();

    private Wallet wallet;
    private ObservableEmitter<Integer> emitter;
    private IOConfig config;

    public Reader(Wallet wallet, IOConfig config, ObservableEmitter<Integer> emitter) {
        this.wallet = wallet;
        this.config = config;
        this.emitter = emitter;
    }

    @Override
    public void run() {
        try {
            File file = new File(config.path);
            FileInputStream fis = new FileInputStream(file);
            ByteBuffer buffer;

            try (FileChannel channel = fis.getChannel()) {
                buffer = ByteBuffer.allocate((int) channel.size());
                channel.read(buffer);
            }
            buffer.flip();

            byte[] headerBytes = new byte[buffer.order(ByteOrder.LITTLE_ENDIAN).getInt()];
            buffer.get(headerBytes);

            Header header = new Header().deserialize(headerBytes, config.key);

            if (header.getVersion() != 0) {
                IOException e = new IOException(String.format("Invalid version %d, mus be 0", header.getVersion()));
                emitter.onError(e);
            }

            buffer = new Decrypter(header.getDerivationData().deriveMasterKey(),
                    header.getNonce(),
                    header.getChunkSize()).decrypt(buffer);

            byte[] hashBytes = new byte[0x20];
            buffer.get(hashBytes);

            byte[] hash = Crypto.hash256(headerBytes);

            if (!Arrays.equals(hashBytes, hash)) {
                IOException e = new IOException(String.format("Invalid hash %s, expected %s", Utils.toHex(hash), Utils.toHex(hashBytes)));
                emitter.onError(e);
            }

            List<WalletRecord> records = new ArrayList<>();
            int recordSize = buffer.order(ByteOrder.LITTLE_ENDIAN).getInt();

            while (recordSize > 0) {
                byte[] record_bytes = new byte[recordSize];
                buffer.get(record_bytes);
                records.add(new WalletRecord().deserialize(record_bytes));
                recordSize = buffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
            }

            int count = 0;
            for (WalletRecord record : records) {
                wallet.addRecord(record);
                emitter.onNext(++count);
            }
            emitter.onComplete();

        } catch (IOException | PreexistingEntityException | SodiumException e) {
            Log.e(TAG, String.format("Error %s", e.getMessage()));
            emitter.onError(e);
        }
    }
}
