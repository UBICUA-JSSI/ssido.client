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

package ssido.wss;

import android.util.Log;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by ITON Solutions on 21/09/2019.
 */
public class WSClient {

    private static final String TAG = WSClient.class.getName();

    HostnameVerifier verifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    X509TrustManager manager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    };

    public OkHttpClient getClient() {
        OkHttpClient client = null;


        try {
            TrustManager[] managers = new TrustManager[]{manager};
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, managers, new SecureRandom());
            SSLSocketFactory factory = context.getSocketFactory();

            client = new OkHttpClient.Builder()
                    .sslSocketFactory(factory, manager)
                    .hostnameVerifier(verifier)
                    .build();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            Log.e(TAG, String.format("Error: %s", e.getMessage()));
        }

        return client;
    }

}
