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

package ssido.ui.register;

import android.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ssido.core.Authenticator;
import ssido.model.RegistrationRequest;
import ssido.model.RelyingPartyIdentity;
import ssido.util.ByteArray;

/**
 * Created by ITON Solutions on 03/10/2019.
 */
public class RegistrationResponse {

    private RegistrationRequest request;

    public RegistrationResponse(RegistrationRequest request){
        this.request = request;
    }

    public String finish(byte[] publicKey, byte[] privateKey, String origin) throws JsonProcessingException {

        String requestId = request.getRequestId().getBase64Url();
        ByteArray challenge = request.getPublicKeyCredentialCreationOptions().getChallenge();
        RelyingPartyIdentity identity = request.getPublicKeyCredentialCreationOptions().getRp();

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode tokenBinding = mapper.createObjectNode();
        tokenBinding.put("status", "supported");

        ObjectNode clientDataJson = mapper.createObjectNode();
        clientDataJson.put("challenge", challenge.getBase64Url());
        clientDataJson.put("origin", origin);
        clientDataJson.put("type", "webauthn.create");
        clientDataJson.set("tokenBinding", tokenBinding);
        clientDataJson.set("clientExtensions", mapper.createObjectNode());

        String clientDataString = new ObjectMapper().writeValueAsString(clientDataJson);

        Authenticator authenticator = Authenticator.builder()
                .publicKey(publicKey)
                .rpId(identity.getId().getBytes())
                .counter(0)
                .attestationStatement(privateKey, clientDataString.getBytes())
                .build();

        ObjectNode authenticationAttestationResponse = mapper.createObjectNode();
        authenticationAttestationResponse.put("attestationObject", Base64.encodeToString(authenticator.getAttestationObject().getBytes(), Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP));
        authenticationAttestationResponse.put("clientDataJSON", new ByteArray(clientDataString.getBytes()).getBase64());


        ObjectNode publicKeyCredential = mapper.createObjectNode();
        publicKeyCredential.put("id", "iOOGPqcfeovZAXe2RSiCKUXKS5peMlPDfk7ib7Q1JfQ");
        publicKeyCredential.set("response", authenticationAttestationResponse);
        publicKeyCredential.set("clientExtensionResults", mapper.createObjectNode());
        publicKeyCredential.put("type", "public-key");

        ObjectNode response = mapper.createObjectNode();
        response.put("requestId", requestId);
        response.set("credential", publicKeyCredential);

        return mapper.writeValueAsString(response);
    }
}
