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

package ssido.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import ssido.R;
import ssido.Ssido;
import ssido.store.StoreService;
import ssido.ui.launcher.LauncherActivity;
import ssido.ui.permission.Constants;
import ssido.ui.permission.Permission;
import ssido.ui.permission.PermissionResponse;
import ssido.ui.permission.PermissionResultCallback;
import jssi.crypto.CryptoService;
import jssi.store.DatabaseHelper;
import jssi.wallet.Wallet;
import jssi.wallet.WalletCredential;
import jssi.wallet.WalletService;
import org.libsodium.jni.NaCl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ITON Solutions on 26/09/2019.
 */
public class SsidoService extends Service {

    private static final String TAG = SsidoService.class.getName();

    private final IBinder binder = new SsidoBinder(this);
    private Ssido app;
    private WalletService walletService;
    private CryptoService cryptoService;
    private StoreService storeService;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        Log.d(TAG, "Ssido service unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Start Ssido service");
        app = (Ssido) getApplication();

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        requestPermissions(true, setPermissionOn(app, permissions));
    }

    private void requestPermissions(boolean async, List<String> permissions) {

        if (async) {

            AsyncTask task = new AsyncTask<Object, Object, Boolean>() {

                @Override
                protected Boolean doInBackground(Object... params) {
                    PermissionResponse response = null;
                    try {
                        response = Permission.getPermission(app,
                                permissions.toArray(new String[permissions.size()]),
                                Constants.DEFAULT_REQUEST_CODE,
                                getResources().getString(R.string.app_name),
                                getResources().getString(R.string.permission_needs),
                                R.drawable.ic_launcher).call();
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage());
                    }

                    return response.isGranted();
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    super.onPostExecute(result);
                    Toast.makeText(SsidoService.this, "Permissions Granted " + result, Toast.LENGTH_SHORT).show();
                    launch();
                }
            };
            task.execute();

        } else {

            Permission.getPermission(app,
                    permissions.toArray(new String[permissions.size()]),
                    Constants.DEFAULT_REQUEST_CODE,
                    getResources().getString(R.string.app_name),
                    getResources().getString(R.string.permission_needs),
                    R.drawable.ic_launcher).enqueue(new PermissionResultCallback() {
                @Override
                public void onComplete(PermissionResponse response) {
                    Toast.makeText(SsidoService.this, "Permissions Granted " + response.isGranted(), Toast.LENGTH_SHORT).show();
                    launch();
                }
            });
        }
    }

    private void launch() {

        Log.d(TAG, "Start Wallet service");
        WalletCredential credential = new WalletCredential("ubicua", "wallet_key");
        DatabaseHelper helper = new DatabaseHelper("ubicua.db", app);
        walletService = new WalletService(app, credential, helper);

        walletService.open().subscribeOn(Schedulers.newThread()).subscribe(new Observer<Wallet>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                Log.d(TAG, "Received SUBSCRIBED event");
                NaCl.sodium();
            }

            @Override
            public void onNext(Wallet wallet) {
                Log.d(TAG, String.format("Wallet opened: id=%s", wallet.getId()));
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, String.format("Error: %s", e.getMessage()));
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Received COMPLETED event");
                Log.d(TAG, "Start Crypto service");
                cryptoService = new CryptoService();
                storeService = new StoreService(app);
                Intent intent = new Intent();
                intent.setAction(LauncherActivity.BROADCAST_ACTION);
                sendBroadcast(intent);
                Log.d(TAG, "Send broadcast");
            }
        });

    }

    private List setPermissionOn(Context context, List<String> permissions) {

        List<String> result = new ArrayList();

        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                result.add(permission);
            }
        }
        return result;
    }

    private boolean hasPermission(Context context, String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public WalletService getWalletService() {
        return walletService;
    }

    public CryptoService getCryptoService() {
        return cryptoService;
    }

    public StoreService getStoreService() {
        return storeService;
    }
}
