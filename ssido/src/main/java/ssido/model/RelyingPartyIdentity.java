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
import com.google.common.base.Optional;

import java.net.URL;

/**
 * Used to supply additional Relying Party attributes when creating a new credential.
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#dictdef-publickeycredentialrpentity">§5.4.2. Relying
 * Party Parameters for Credential Generation (dictionary PublicKeyCredentialRpEntity)
 * </a>
 */
public final class RelyingPartyIdentity implements PublicKeyCredentialEntity {
    /**
     * The human-palatable name of the Relaying Party.
     *
     * <p>
     * For example: "ACME Corporation", "Wonderful Widgets, Inc." or "ОАО Примертех".
     * </p>
     */
    @NonNull
    private final String name;
    /**
     * A unique identifier for the Relying Party, which sets the <a href="https://www.w3.org/TR/webauthn/#rp-id">RP
     * ID</a>.
     *
     * @see <a href="https://www.w3.org/TR/webauthn/#rp-id">RP ID</a>
     */
    @NonNull
    private final String id;
    /**
     * A URL which resolves to an image associated with the entity. For example, this could be the Relying Party's
     * logo.
     *
     * <p>This URL MUST be an a priori authenticated URL. Authenticators MUST accept and store a
     * 128-byte minimum length for an icon member’s value. Authenticators MAY ignore an icon member’s value if its
     * length is greater than 128 bytes. The URL’s scheme MAY be "data" to avoid fetches of the URL, at the cost of
     * needing more storage.
     * </p>
     */
    @NonNull
    private final Optional<URL> icon;

    @JsonCreator
    private RelyingPartyIdentity(@NonNull @JsonProperty("name") String name, @NonNull @JsonProperty("id") String id, @JsonProperty("icon") URL icon) {
        this(name, id, Optional.fromNullable(icon));
    }

    public static RelyingPartyIdentityBuilder.MandatoryStages builder() {
        return new RelyingPartyIdentityBuilder.MandatoryStages();
    }


    public static class RelyingPartyIdentityBuilder {
        private String name;
        private String id;
        @NonNull
        private Optional<URL> icon = Optional.absent();


        public static class MandatoryStages {
            private RelyingPartyIdentityBuilder builder = new RelyingPartyIdentityBuilder();

            public StageId id(String id) {
                builder.id(id);
                return new StageId();
            }


            public class StageId {
                public RelyingPartyIdentityBuilder name(String name) {
                    return builder.name(name);
                }
            }
        }

        /**
         * A URL which resolves to an image associated with the entity. For example, this could be the Relying Party's
         * logo.
         *
         * <p>This URL MUST be an a priori authenticated URL. Authenticators MUST accept and store a
         * 128-byte minimum length for an icon member’s value. Authenticators MAY ignore an icon member’s value if its
         * length is greater than 128 bytes. The URL’s scheme MAY be "data" to avoid fetches of the URL, at the cost of
         * needing more storage.
         * </p>
         * @param icon
         * @return 
         */
        RelyingPartyIdentityBuilder icon(@NonNull Optional<URL> icon) {
            this.icon = icon;
            return this;
        }

        /**
         * A URL which resolves to an image associated with the entity. For example, this could be the Relying Party's
         * logo.
         *
         * <p>This URL MUST be an a priori authenticated URL. Authenticators MUST accept and store a
         * 128-byte minimum length for an icon member’s value. Authenticators MAY ignore an icon member’s value if its
         * length is greater than 128 bytes. The URL’s scheme MAY be "data" to avoid fetches of the URL, at the cost of
         * needing more storage.
         * </p>
         * @param icon
         * @return 
         */
        public RelyingPartyIdentityBuilder icon(@NonNull URL icon) {
            return this.icon(Optional.of(icon));
        }

        RelyingPartyIdentityBuilder() {
        }

        /**
         * The human-palatable name of the Relaying Party.
         *
         * <p>
         * For example: "ACME Corporation", "Wonderful Widgets, Inc." or "ОАО Примертех".
         * </p>
         * @param name
         * @return 
         */
        public RelyingPartyIdentityBuilder name(@NonNull final String name) {
            this.name = name;
            return this;
        }

        /**
         * A unique identifier for the Relying Party, which sets the <a href="https://www.w3.org/TR/webauthn/#rp-id">RP
         * ID</a>.
         *
         * @param id
         * @return 
         * @see <a href="https://www.w3.org/TR/webauthn/#rp-id">RP ID</a>
         */
        public RelyingPartyIdentityBuilder id(@NonNull final String id) {
            this.id = id;
            return this;
        }

        public RelyingPartyIdentity build() {
            return new RelyingPartyIdentity(name, id, icon);
        }

        @Override
        public String toString() {
            return "RelyingPartyIdentity.RelyingPartyIdentityBuilder(name=" + this.name + ", id=" + this.id + ", icon=" + this.icon + ")";
        }
    }

    /**
     * A unique identifier for the Relying Party, which sets the <a href="https://www.w3.org/TR/webauthn/#rp-id">RP
     * ID</a>.
     *
     * @return
     * @see <a href="https://www.w3.org/TR/webauthn/#rp-id">RP ID</a>
     */
    @NonNull
    public String getId() {
        return this.id;
    }

    private RelyingPartyIdentity(@NonNull final String name, @NonNull final String id, @NonNull final Optional<URL> icon) {
        this.name = name;
        this.id = id;
        this.icon = icon;
    }

    /**
     * The human-palatable name of the Relaying Party.
     *
     * <p>
     * For example: "ACME Corporation", "Wonderful Widgets, Inc." or "ОАО Примертех".
     * </p>
     */
    @Override
    @NonNull
    public String getName() {
        return this.name;
    }

    /**
     * A URL which resolves to an image associated with the entity. For example, this could be the Relying Party's
     * logo.
     *
     * <p>This URL MUST be an a priori authenticated URL. Authenticators MUST accept and store a
     * 128-byte minimum length for an icon member’s value. Authenticators MAY ignore an icon member’s value if its
     * length is greater than 128 bytes. The URL’s scheme MAY be "data" to avoid fetches of the URL, at the cost of
     * needing more storage.
     * </p>
     */
    @Override
    @NonNull
    public Optional<URL> getIcon() {
        return this.icon;
    }
}
