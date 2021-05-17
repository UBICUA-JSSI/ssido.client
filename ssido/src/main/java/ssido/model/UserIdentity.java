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

import ssido.util.ByteArray;

import java.net.URL;

/**
 * Describes a user account, with which public key credentials can be
 * associated.
 *
 *
 *
 * @see
 * <a href="https://www.w3.org/TR/webauthn/#dictdef-publickeycredentialuserentity">§5.4.3.
 * User
 *
 * Account Parameters for Credential Generation (dictionary
 * PublicKeyCredentialUserEntity)
 *
 * </a>
 */
public final class UserIdentity implements PublicKeyCredentialEntity {

    /**
     * A human-palatable identifier for a user account. It is intended only for
     * display, i.e., aiding the user in
     *
     * determining the difference between user accounts with similar
     * {@link #displayName}s.
     *
     * <p>
     *
     * For example: "alexm", "alex.p.mueller@example.com" or "+14255551234".
     *
     * </p>
     */
    @NonNull
    private final String name;
    /**
     * A human-palatable name for the user account, intended only for display.
     * For example, "Alex P. Müller" or "田中 倫".
     *
     * The Relying Party SHOULD let the user choose this, and SHOULD NOT
     * restrict the choice more than necessary.
     *
     *
     *
     * <ul>
     *
     * <li>Relying Parties SHOULD perform enforcement, as prescribed in Section
     * 2.3 of [RFC8266] for the Nickname
     *
     * Profile of the PRECIS FreeformClass [RFC8264], when setting
     * {@link #displayName}'s value, or displaying the value
     *
     * to the user.</li>
     *
     * <li>Clients SHOULD perform enforcement, as prescribed in Section 2.3 of
     * [RFC8266] for the Nickname Profile of
     *
     * the PRECIS FreeformClass [RFC8264], on {@link #displayName}'s value prior
     * to displaying the value to the user or
     *
     * including the value as a parameter of the
     * <code>authenticatorMakeCredential</code> operation.</li>
     *
     * </ul>
     *
     *
     *
     * <p>
     *
     * When clients, client platforms, or authenticators display a
     * {@link #displayName}'s value, they should always use
     *
     * UI elements to provide a clear boundary around the displayed value, and
     * not allow overflow into other elements.
     *
     * </p>
     *
     *
     *
     * <p>
     *
     * Authenticators MUST accept and store a 64-byte minimum length for a
     * {@link #displayName} member's value.
     *
     * Authenticators MAY truncate a {@link #displayName} member's value to a
     * length equal to or greater than 64 bytes.
     *
     * </p>
     *
     *
     *
     * @see <a href="https://tools.ietf.org/html/rfc8264">RFC 8264</a>
     *
     * @see <a href="https://tools.ietf.org/html/rfc8266">RFC 8266</a>
     */
    @NonNull
    private final String displayName;
    /**
     * The
     * <a href="https://www.w3.org/TR/webauthn/#user-handle">user
     * handle</a> for the account,
     *
     * specified by the Relying Party.
     *
     *
     *
     * <p>
     *
     * A user handle is an opaque byte sequence with a maximum size of 64 bytes.
     * User handles are not meant to be
     *
     * displayed to users. The user handle SHOULD NOT contain personally
     * identifying information about the user, such as
     *
     * a username or e-mail address; see
     * <a href="https://www.w3.org/TR/webauthn/#sctn-user-handle-privacy">§14.9
     *
     * User Handle Contents</a> for details.
     *
     * </p>
     *
     *
     *
     * <p>
     *
     * To ensure secure operation, authentication and authorization decisions
     * MUST be made on the basis of this {@link
     *
     * #id} member, not the {@link #displayName} nor {@link #name} members. See
     * <a href="https://tools.ietf.org/html/rfc8266#section-6.1">Section
     *
     * 6.1 of RFC 8266</a>.
     *
     * </p>
     *
     *
     *
     * <p>
     *
     * An authenticator will never contain more than one credential for a given
     * Relying Party under the same user
     *
     * handle.
     *
     * </p>
     */
    @NonNull
    private final ByteArray id;
    /**
     * A URL which resolves to an image associated with the entity. For example,
     * this could be the user’s avatar.
     *
     *
     *
     * <p>
     * This URL MUST be an a priori authenticated URL. Authenticators MUST
     * accept and store a
     *
     * 128-byte minimum length for an icon member’s value. Authenticators MAY
     * ignore an icon member’s value if its
     *
     * length is greater than 128 bytes. The URL’s scheme MAY be "data" to avoid
     * fetches of the URL, at the cost of
     *
     * needing more storage.
     *
     * </p>
     */
    @NonNull
    private final Optional<URL> icon;

    @JsonCreator
    private UserIdentity(
            @NonNull @JsonProperty("name") String name,
            @NonNull @JsonProperty("displayName") String displayName,
            @NonNull @JsonProperty("id") ByteArray id,
            @JsonProperty("icon") URL icon) {
        this(name, displayName, id, Optional.fromNullable(icon));
    }

    public static UserIdentityBuilder.MandatoryStages builder() {
        return new UserIdentityBuilder.MandatoryStages();
    }

    public static class UserIdentityBuilder {

        private String name;
        private String displayName;
        private ByteArray id;

        @NonNull
        private Optional<URL> icon = Optional.absent();

        public static class MandatoryStages {

            private UserIdentityBuilder builder = new UserIdentityBuilder();

            public StageName name(String name) {
                builder.name(name);
                return new StageName();
            }

            public class StageName {

                public StageDisplayName displayName(String displayName) {
                    builder.displayName(displayName);
                    return new StageDisplayName();
                }
            }

            public class StageDisplayName {

                public UserIdentityBuilder id(ByteArray id) {
                    return builder.id(id);
                }
            }
        }

        /**
         * A URL which resolves to an image associated with the entity. For
         * example, this could be the user’s avatar.
         *
         * <p>
         * This URL MUST be an a priori authenticated URL. Authenticators MUST
         * accept and store a 128-byte minimum length for an icon member’s
         * value. Authenticators MAY ignore an icon member’s value if its length
         * is greater than 128 bytes. The URL’s scheme MAY be "data" to avoid
         * fetches of the URL, at the cost of needing more storage.
         * </p>
         *
         * @param icon
         * @return
         */
        UserIdentityBuilder icon(@NonNull Optional<URL> icon) {
            this.icon = icon;
            return this;
        }

        /**
         * A URL which resolves to an image associated with the entity. For
         * example, this could be the user’s avatar.
         *
         * <p>
         * This URL MUST be an a priori authenticated URL. Authenticators MUST
         * accept and store a 128-byte minimum length for an icon member’s
         * value. Authenticators MAY ignore an icon member’s value if its length
         * is greater than 128 bytes. The URL’s scheme MAY be "data" to avoid
         * fetches of the URL, at the cost of needing more storage.
         * </p>
         *
         * @param icon
         * @return
         */
        public UserIdentityBuilder icon(@NonNull URL icon) {
            return this.icon(Optional.of(icon));
        }

        UserIdentityBuilder() {
        }

        /**
         * A human-palatable identifier for a user account. It is intended only
         * for display, i.e., aiding the user in
         *
         * determining the difference between user accounts with similar
         * {@link #displayName}s.
         *
         * <p>
         *
         * For example: "alexm", "alex.p.mueller@example.com" or "+14255551234".
         *
         * </p>
         *
         * @param name
         * @return
         */
        public UserIdentityBuilder name(@NonNull final String name) {
            this.name = name;
            return this;
        }

        /**
         * A human-palatable name for the user account, intended only for
         * display. For example, "Alex P. Müller" or "田中 倫".
         *
         * The Relying Party SHOULD let the user choose this, and SHOULD NOT
         * restrict the choice more than necessary.
         *
         *
         *
         * <ul>
         *
         * <li>Relying Parties SHOULD perform enforcement, as prescribed in
         * Section 2.3 of [RFC8266] for the Nickname
         *
         * Profile of the PRECIS FreeformClass [RFC8264], when setting
         * {@link #displayName}'s value, or displaying the value
         *
         * to the user.</li>
         *
         * <li>Clients SHOULD perform enforcement, as prescribed in Section 2.3
         * of [RFC8266] for the Nickname Profile of
         *
         * the PRECIS FreeformClass [RFC8264], on {@link #displayName}'s value
         * prior to displaying the value to the user or
         *
         * including the value as a parameter of the
         * <code>authenticatorMakeCredential</code> operation.</li>
         *
         * </ul>
         *
         *
         *
         * <p>
         *
         * When clients, client platforms, or authenticators display a
         * {@link #displayName}'s value, they should always use
         *
         * UI elements to provide a clear boundary around the displayed value,
         * and not allow overflow into other elements.
         *
         * </p>
         *
         *
         *
         * <p>
         *
         * Authenticators MUST accept and store a 64-byte minimum length for a
         * {@link #displayName} member's value.
         *
         * Authenticators MAY truncate a {@link #displayName} member's value to
         * a length equal to or greater than 64 bytes.
         *
         * </p>
         *
         *
         *
         * @param displayName
         * @return 
         * @see <a href="https://tools.ietf.org/html/rfc8264">RFC 8264</a>
         *
         * @see <a href="https://tools.ietf.org/html/rfc8266">RFC 8266</a>
         */
        UserIdentityBuilder displayName(@NonNull final String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * The
         * <a href="https://www.w3.org/TR/webauthn/#user-handle">user
         * handle</a> for the account,
         *
         * specified by the Relying Party.
         *
         *
         *
         * <p>
         *
         * A user handle is an opaque byte sequence with a maximum size of 64
         * bytes. User handles are not meant to be
         *
         * displayed to users. The user handle SHOULD NOT contain personally
         * identifying information about the user, such as
         *
         * a username or e-mail address; see
         * <a href="https://www.w3.org/TR/webauthn/#sctn-user-handle-privacy">§14.9
         *
         * User Handle Contents</a> for details.
         *
         * </p>
         *
         *
         *
         * <p>
         *
         * To ensure secure operation, authentication and authorization
         * decisions MUST be made on the basis of this {@link
         *
         * #id} member, not the {@link #displayName} nor {@link #name} members.
         * See <a href="https://tools.ietf.org/html/rfc8266#section-6.1">Section
         *
         * 6.1 of RFC 8266</a>.
         *
         * </p>
         *
         *
         *
         * <p>
         *
         * An authenticator will never contain more than one credential for a
         * given Relying Party under the same user
         *
         * handle.
         *
         * </p>
         * @param id
         * @return 
         */
        public UserIdentityBuilder id(@NonNull final ByteArray id) {
            this.id = id;
            return this;
        }

        public UserIdentity build() {
            return new UserIdentity(name, displayName, id, icon);
        }
    }

    /**
     * A human-palatable name for the user account, intended only for display.
     * For example, "Alex P. Müller" or "田中 倫".
     *
     * The Relying Party SHOULD let the user choose this, and SHOULD NOT
     * restrict the choice more than necessary.
     *
     *
     *
     * <ul>
     *
     * <li>Relying Parties SHOULD perform enforcement, as prescribed in Section
     * 2.3 of [RFC8266] for the Nickname
     *
     * Profile of the PRECIS FreeformClass [RFC8264], when setting
     * {@link #displayName}'s value, or displaying the value
     *
     * to the user.</li>
     *
     * <li>Clients SHOULD perform enforcement, as prescribed in Section 2.3 of
     * [RFC8266] for the Nickname Profile of
     *
     * the PRECIS FreeformClass [RFC8264], on {@link #displayName}'s value prior
     * to displaying the value to the user or
     *
     * including the value as a parameter of the
     * <code>authenticatorMakeCredential</code> operation.</li>
     *
     * </ul>
     *
     *
     *
     * <p>
     *
     * When clients, client platforms, or authenticators display a
     * {@link #displayName}'s value, they should always use
     *
     * UI elements to provide a clear boundary around the displayed value, and
     * not allow overflow into other elements.
     *
     * </p>
     *
     *
     *
     * <p>
     *
     * Authenticators MUST accept and store a 64-byte minimum length for a
     * {@link #displayName} member's value.
     *
     * Authenticators MAY truncate a {@link #displayName} member's value to a
     * length equal to or greater than 64 bytes.
     *
     * </p>
     *
     *
     *
     * @return 
     * @see <a href="https://tools.ietf.org/html/rfc8264">RFC 8264</a>
     *
     * @see <a href="https://tools.ietf.org/html/rfc8266">RFC 8266</a>
     */
    @NonNull
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * The
     * <a href="https://www.w3.org/TR/webauthn/#user-handle">user
     * handle</a> for the account,
     *
     * specified by the Relying Party.
     *
     *
     *
     * <p>
     *
     * A user handle is an opaque byte sequence with a maximum size of 64 bytes.
     * User handles are not meant to be
     *
     * displayed to users. The user handle SHOULD NOT contain personally
     * identifying information about the user, such as
     *
     * a username or e-mail address; see
     * <a href="https://www.w3.org/TR/webauthn/#sctn-user-handle-privacy">§14.9
     *
     * User Handle Contents</a> for details.
     *
     * </p>
     *
     *
     *
     * <p>
     *
     * To ensure secure operation, authentication and authorization decisions
     * MUST be made on the basis of this {@link
     *
     * #id} member, not the {@link #displayName} nor {@link #name} members. See
     * <a href="https://tools.ietf.org/html/rfc8266#section-6.1">Section
     *
     * 6.1 of RFC 8266</a>.
     *
     * </p>
     *
     *
     *
     * <p>
     *
     * An authenticator will never contain more than one credential for a given
     * Relying Party under the same user
     *
     * handle.
     *
     * </p>
     * @return 
     */
    @NonNull
    public ByteArray getId() {
        return this.id;
    }

    private UserIdentity(
            @NonNull final String name,
            @NonNull final String displayName,
            @NonNull final ByteArray id,
            @NonNull final Optional<URL> icon) {
        
        this.name = name;
        this.displayName = displayName;
        this.id = id;
        this.icon = icon;
    }

    /**
     * A human-palatable identifier for a user account. It is intended only for
     * display, i.e., aiding the user in
     *
     * determining the difference between user accounts with similar
     * {@link #displayName}s.
     *
     * <p>
     *
     * For example: "alexm", "alex.p.mueller@example.com" or "+14255551234".
     *
     * </p>
     */
    @Override
    @NonNull
    public String getName() {
        return this.name;
    }

    /**
     * A URL which resolves to an image associated with the entity. For example,
     * this could be the user’s avatar.
     *
     *
     *
     * <p>
     * This URL MUST be an a priori authenticated URL. Authenticators MUST
     * accept and store a
     *
     * 128-byte minimum length for an icon member’s value. Authenticators MAY
     * ignore an icon member’s value if its
     *
     * length is greater than 128 bytes. The URL’s scheme MAY be "data" to avoid
     * fetches of the URL, at the cost of
     *
     * needing more storage.
     *
     * </p>
     */
    @Override
    @NonNull
    public Optional<URL> getIcon() {
        return this.icon;
    }
}
