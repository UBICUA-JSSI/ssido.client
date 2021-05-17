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
package ssido.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;

import ssido.model.extension.AppId;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains <a href="https://www.w3.org/TR/webauthn/#client-extension-input">client extension
 * inputs</a> to a
 * <code>navigator.credentials.get()</code> operation. All members are optional.
 *
 * <p>
 * The authenticator extension inputs are derived from these client extension inputs.
 * </p>
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#extensions">§9. WebAuthn Extensions</a>
 */

@JsonDeserialize(using = AssertionExtensionInputs.JsonDeserializer.class)
public final class AssertionExtensionInputs implements ExtensionInputs {
    /**
     * The input to the FIDO AppID Extension (<code>appid</code>).
     *
     * <p>
     * This extension allows WebAuthn Relying Parties that have previously registered a credential using the legacy FIDO
     * JavaScript APIs to request an assertion. The FIDO APIs use an alternative identifier for Relying Parties called
     * an <a href="https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html">AppID</a>,
     * and any credentials created using those APIs will be scoped to that identifier. Without this extension, they
     * would need to be re-registered in order to be scoped to an RP ID.
     * </p>
     * <p>
     * This extension does not allow FIDO-compatible credentials to be created. Thus, credentials created with WebAuthn
     * are not backwards compatible with the FIDO JavaScript APIs.
     * </p>
     *
     * @see <a href="https://www.w3.org/TR/webauthn/#sctn-appid-extension">§10.1. FIDO AppID Extension
     * (appid)</a>
     */
    @NonNull
    private final Optional<AppId> appid;

    private AssertionExtensionInputs(@NonNull Optional<AppId> appid) {
        this.appid = appid;
    }

    @JsonCreator
    private AssertionExtensionInputs(@JsonProperty("appid") AppId appid) {
        this(Optional.fromNullable(appid));
    }

    @Override
    public Set<String> getExtensionIds() {
        Set<String> ids = new HashSet<>();
        appid.transform(id -> ids.add("appid"));
        return ids;
    }


    public static class AssertionExtensionInputsBuilder {
        private Optional<AppId> appid = Optional.absent();

        /**
         * The input to the FIDO AppID Extension (<code>appid</code>).
         *
         * <p>
         * This extension allows WebAuthn Relying Parties that have previously registered a credential using the legacy FIDO
         * JavaScript APIs to request an assertion. The FIDO APIs use an alternative identifier for Relying Parties called
         * an <a href="https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html">AppID</a>,
         * and any credentials created using those APIs will be scoped to that identifier. Without this extension, they
         * would need to be re-registered in order to be scoped to an iRP ID.
         * </p>
         * <p>
         * This extension does not allow FIDO-compatible credentials to be created. Thus, credentials created with WebAuthn
         * are not backwards compatible with the FIDO JavaScript APIs.
         * </p>
         *
         * @param appid
         * @return 
         * @see <a href="https://www.w3.org/TR/webauthn/#sctn-appid-extension">§10.1. FIDO AppID Extension
         * (appid)</a>
         */
        public AssertionExtensionInputsBuilder appid(@NonNull Optional<AppId> appid) {
            this.appid = appid;
            return this;
        }

        /**
         * The input to the FIDO AppID Extension (<code>appid</code>).
         *
         * <p>
         * This extension allows WebAuthn Relying Parties that have previously registered a credential using the legacy FIDO
         * JavaScript APIs to request an assertion. The FIDO APIs use an alternative identifier for Relying Parties called
         * an <a href="https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html">AppID</a>,
         * and any credentials created using those APIs will be scoped to that identifier. Without this extension, they
         * would need to be re-registered in order to be scoped to an RP ID.
         * </p>
         * <p>
         * This extension does not allow FIDO-compatible credentials to be created. Thus, credentials created with WebAuthn
         * are not backwards compatible with the FIDO JavaScript APIs.
         * </p>
         *
         * @param appid
         * @return 
         * @see <a href="https://www.w3.org/TR/webauthn/#sctn-appid-extension">§10.1. FIDO AppID Extension
         * (appid)</a>
         */
        public AssertionExtensionInputsBuilder appid(@NonNull AppId appid) {
            this.appid(Optional.of(appid));
            return this;
        }

        AssertionExtensionInputsBuilder() {
        }

        public AssertionExtensionInputs build() {
            return new AssertionExtensionInputs(appid.isPresent() ? appid.get() : null);
        }
    }

    public static AssertionExtensionInputsBuilder builder() {
        return new AssertionExtensionInputsBuilder();
    }

    public AssertionExtensionInputsBuilder toBuilder() {
        return new AssertionExtensionInputsBuilder().appid(appid);
    }

    static class JsonDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<AssertionExtensionInputs> {

        @Override
        public AssertionExtensionInputs deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            AppId appid = parser.getCodec().readValue(parser, AppId.class);
            return AssertionExtensionInputs.builder().appid(appid).build();
        }
    }

    /**
     * The input to the FIDO AppID Extension (<code>appId</code>).
     *
     * <p>
     * This extension allows WebAuthn Relying Parties that have previously registered a credential using the legacy FIDO
     * JavaScript APIs to request an assertion. The FIDO APIs use an alternative identifier for Relying Parties called
     * an <a href="https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html">AppID</a>,
     * and any credentials created using those APIs will be scoped to that identifier. Without this extension, they
     * would need to be re-registered in order to be scoped to an RP ID.
     * </p>
     * <p>
     * This extension does not allow FIDO-compatible credentials to be created. Thus, credentials created with WebAuthn
     * are not backwards compatible with the FIDO JavaScript APIs.
     * </p>
     *
     * @return 
     * @see <a href="https://www.w3.org/TR/webauthn/#sctn-appid-extension">§10.1. FIDO AppID Extension
     * (appid)</a>
     */
    @NonNull
    public Optional<AppId> getAppid() {
        return appid;
    }
}
