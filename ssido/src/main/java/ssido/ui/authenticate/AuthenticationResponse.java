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

package ssido.ui.authenticate;

import android.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;

import ssido.Ssido;
import ssido.core.Authenticator;
import ssido.model.AssertionRequest;
import ssido.store.StoreService;
import ssido.util.ByteArray;

import java.io.IOException;

/**
 * Created by ITON Solutions on 03/10/2019.
 */
public class AuthenticationResponse {

    private AssertionRequest request;
    private Ssido app;

    public AuthenticationResponse(Ssido app, AssertionRequest request){
        this.app = app;
        this.request = request;
    }

    public String finish(byte[] publicKey, byte[] privateKey, String origin) throws IOException {

        String requestId = request.getRequestId().getBase64Url();
        ByteArray challenge = request.getPublicKeyCredentialRequestOptions().getChallenge();
        Optional<String> rpId = request.getPublicKeyCredentialRequestOptions().getRpId();

        // Internal Authenticator counter
        StoreService store = app.getService().getStoreService();
        Integer counter = store.getCounter();

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode tokenBinding = mapper.createObjectNode();
        tokenBinding.put("status", "supported");

        ObjectNode clientDataJson = mapper.createObjectNode();
        clientDataJson.put("challenge", challenge.getBase64Url());
        clientDataJson.put("origin", origin);
        clientDataJson.put("type", "webauthn.get");
        clientDataJson.set("tokenBinding", tokenBinding);
        clientDataJson.set("clientExtensions", mapper.createObjectNode());

        String clientDataString = new ObjectMapper().writeValueAsString(clientDataJson);

        Authenticator authenticator = Authenticator.builder()
                .publicKey(publicKey)
                .rpId(rpId.isPresent() ? rpId.get().getBytes() : new byte[0])
                .counter(counter++)
                .attestationStatement(privateKey, clientDataString.getBytes())
                .build();

        store.setCounter(counter);

        byte[] signature = authenticator.getAttestationStatement().getObjectNode().get("sig").binaryValue();

        ObjectNode authenticatorAssertionResponse = mapper.createObjectNode();
        authenticatorAssertionResponse.put("authenticatorData", Base64.encodeToString(authenticator.getAuthenticatorData().getBytes(), Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP));
        authenticatorAssertionResponse.put("clientDataJSON", new ByteArray(clientDataString.getBytes()).getBase64());
        authenticatorAssertionResponse.put("signature", Base64.encodeToString(signature, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP));

        ObjectNode publicKeyCredential = mapper.createObjectNode();
        publicKeyCredential.put("id", "iOOGPqcfeovZAXe2RSiCKUXKS5peMlPDfk7ib7Q1JfQ");
        publicKeyCredential.set("response", authenticatorAssertionResponse);
        publicKeyCredential.set("clientExtensionResults", mapper.createObjectNode());
        publicKeyCredential.put("type", "public-key");


        ObjectNode response = mapper.createObjectNode();
        response.put("requestId", requestId);
        response.set("credential", publicKeyCredential);

        return mapper.writeValueAsString(response);
    }
}
