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

import java.util.Collections;
import java.util.Set;

/**
 * Contains <a href="https://www.w3.org/TR/webauthn/#client-extension-input">client extension
 * inputs</a> to a
 * <code>navigator.credentials.create()</code> operation. All members are optional.
 *
 * <p>
 * The authenticator extension inputs are derived from these client extension inputs.
 * </p>
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#extensions">§9. WebAuthn Extensions</a>
 */
public final class RegistrationExtensionInputs implements ExtensionInputs {
    @Override
    public Set<String> getExtensionIds() {
        return Collections.emptySet();
    }

    public static class RegistrationExtensionInputsBuilder {
        RegistrationExtensionInputsBuilder() {
        }

        public RegistrationExtensionInputs build() {
            return new RegistrationExtensionInputs();
        }
    }

      public static RegistrationExtensionInputsBuilder builder() {
        return new RegistrationExtensionInputsBuilder();
    }

    private RegistrationExtensionInputs() {
    }
}
