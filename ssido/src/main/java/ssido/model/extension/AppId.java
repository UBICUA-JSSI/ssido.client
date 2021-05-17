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
package ssido.model.extension;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.net.InetAddresses;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * A FIDO AppID verified to be syntactically valid.
 *
 * @see <a href="https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html">FIDO AppID and Facet Specification</a>
 */
@JsonSerialize(using = AppId.JsonSerializer.class)
public final class AppId {
    /**
     * The underlying string representation of this AppID.
     */
    private final String id;

    /**
     * Verify that the <code>appId</code> is a valid FIDO AppID, and wrap it as an {@link AppId}.
     *
     * @param appid
     * @throws InvalidAppIdException if <code>appId</code> is not a valid FIDO AppID.
     * @see <a href="https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html">FIDO AppID and Facet Specification</a>
     */
    @JsonCreator
    private AppId(@JsonProperty("appid") String appid) throws InvalidAppIdException {
        checkIsValid(appid);
        this.id = appid;
    }

    /**
     * Throws {@link InvalidAppIdException} if the given App ID is found to be incompatible with the U2F specification or any major
     * U2F Client implementation.
     *
     * @param appId the App ID to be validated
     */
    private static void checkIsValid(String appId) throws InvalidAppIdException {
        if (!appId.contains(":")) {
            throw new InvalidAppIdException("App ID does not look like a valid facet or URL. Web facets must start with \'https://\'.");
        }
        if (appId.startsWith("http:")) {
            throw new InvalidAppIdException("HTTP is not supported for App IDs (by Chrome). Use HTTPS instead.");
        }
        if (appId.startsWith("https://")) {
            URI url = checkValidUrl(appId);
            checkPathIsNotSlash(url);
            checkNotIpAddress(url);
        }
    }

    private static void checkPathIsNotSlash(URI url) throws InvalidAppIdException {
        if ("/".equals(url.getPath())) {
            throw new InvalidAppIdException("The path of the URL set as App ID is \'/\'. This is probably not what you want -- remove the trailing slash of the App ID URL.");
        }
    }

    private static URI checkValidUrl(String appId) throws InvalidAppIdException {
        try {
            return new URI(appId);
        } catch (URISyntaxException e) {
            throw new InvalidAppIdException("App ID looks like a HTTPS URL, but has syntax errors.", e);
        }
    }

    private static void checkNotIpAddress(URI url) throws InvalidAppIdException {
        if (InetAddresses.isInetAddress(url.getAuthority()) || (url.getHost() != null && InetAddresses.isInetAddress(url.getHost()))) {
            throw new InvalidAppIdException("App ID must not be an IP-address, since it is not supported (by Chrome). Use a host name instead.");
        }
    }


    static class JsonSerializer extends com.fasterxml.jackson.databind.JsonSerializer<AppId> {
        @Override
        public void serialize(AppId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getId());
        }
    }

    /**
     * The underlying string representation of this AppID.
     * @return 
     */
    public String getId() {
        return this.id;
    }
}
