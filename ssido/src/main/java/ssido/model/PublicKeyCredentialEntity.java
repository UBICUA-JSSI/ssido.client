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

import com.google.common.base.Optional;
import java.net.URL;


/**
 * Describes a user account, or a WebAuthn Relying Party, which a public key credential is associated with or scoped to,
 * respectively.
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#dictdef-publickeycredentialentity">§5.4.1. Public Key
 * Entity Description (dictionary PublicKeyCredentialEntity)
 * </a>
 */
public interface PublicKeyCredentialEntity {

    /**
     * A human-palatable name for the entity. Its function depends on what the PublicKeyCredentialEntity represents:
     *
     * <ul>
     * <li>When inherited by PublicKeyCredentialRpEntity it is a human-palatable identifier for the Relying Party,
     * intended only for display. For example, "ACME Corporation", "Wonderful Widgets, Inc." or "ОАО Примертех".
     * <ul>
     * <li>Relying Parties SHOULD perform enforcement, as prescribed in Section 2.3 of [RFC8266] for the Nickname
     * Profile of the PRECIS FreeformClass [RFC8264], when setting name's value, or displaying the value to the
     * user.</li>
     * <li>Clients SHOULD perform enforcement, as prescribed in Section 2.3 of [RFC8266] for the Nickname Profile of
     * the PRECIS FreeformClass [RFC8264], on name's value prior to displaying the value to the user or including the
     * value as a parameter of the authenticatorMakeCredential operation.</li>
     * </ul>
     * </li>
     * <li>When inherited by PublicKeyCredentialUserEntity, it is a human-palatable identifier for a user account. It
     * is intended only for display, i.e., aiding the user in determining the difference between user accounts with
     * similar displayNames. For example, "alexm", "alex.p.mueller@example.com" or "+14255551234".
     * <ul>
     * <li>The Relying Party MAY let the user choose this value. The Relying Party SHOULD perform enforcement, as
     * prescribed in Section 3.4.3 of [RFC8265] for the UsernameCasePreserved Profile of the PRECIS IdentifierClass
     * [RFC8264], when setting name's value, or displaying the value to the user.</li>
     * <li>Clients SHOULD perform enforcement, as prescribed in Section 3.4.3 of [RFC8265] for the
     * UsernameCasePreserved Profile of the PRECIS IdentifierClass [RFC8264], on name's value prior to displaying the
     * value to the user or including the value as a parameter of the authenticatorMakeCredential operation.</li>
     * </ul>
     * </ul>
     * <p>
     * When clients, client platforms, or authenticators display a name's value, they should always use UI elements to
     * provide a clear boundary around the displayed value, and not allow overflow into other elements.
     * </p>
     * <p>
     * Authenticators MUST accept and store a 64-byte minimum length for a name member’s value. Authenticators MAY
     * truncate a name member’s value to a length equal to or greater than 64 bytes.
     * </p>
     *
     * @return 
     * @see <a href="https://tools.ietf.org/html/rfc8264">RFC 8264</a>
     * @see <a href="https://tools.ietf.org/html/rfc8265">RFC 8265</a>
     */
    String getName();

    /**
     * A serialized URL which resolves to an image associated with the entity.
     *
     * <p>
     * For example, this could be a user's avatar or a Relying Party's logo. This URL MUST be an a priori authenticated
     * URL. Authenticators MUST accept and store a 128-byte minimum length for an icon member's value. Authenticators
     * MAY ignore an icon member's value if its length is greater than 128 bytes. The URL's scheme MAY be "data" to
     * avoid fetches of the URL, at the cost of needing more storage.
     * </p>
     * @return 
     */
    Optional<URL> getIcon();

}
