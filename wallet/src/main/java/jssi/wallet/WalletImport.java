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

import jssi.wallet.io.IOConfig;
import jssi.wallet.io.Reader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 *
 * @author ITON Solutions
 */
class WalletImport {
    private static final String TAG = WalletImport.class.getName();
    
    private final Wallet wallet;
    
    WalletImport(final Wallet wallet){
        this.wallet = wallet;
    }

    private class Emitter implements ObservableOnSubscribe<Integer> {

        IOConfig config;
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Emitter(IOConfig config) {
            this.config = config;
        }

        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) {
            executor.execute(new Reader(wallet, config, emitter));
        }
    }

    Observable<Integer> restore(IOConfig config) {
        return Observable.create(new Emitter(config));
    }

}
    
    
