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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;

import ssido.util.JsonLongSerializable;
import ssido.util.JsonLongSerializer;

/**
 * A number identifying a cryptographic algorithm. The algorithm identifiers SHOULD be values registered in the IANA
 * COSE Algorithms registry, for instance, -7 for "ES256" and -257 for "RS256".
 *
 * @see <a href="https://www.w3.org/TR/webauthn/#typedefdef-cosealgorithmidentifier">§5.10.5.
 * Cryptographic Algorithm Identifier (typedef COSEAlgorithmIdentifier)</a>
 */
@JsonSerialize(using = JsonLongSerializer.class)
public enum COSEAlgorithmIdentifier implements JsonLongSerializable {
    EdDSA(-8),
    ES256(-7),
    RS256(-257);

    private final long id;

    COSEAlgorithmIdentifier(long id) {
        this.id = id;
    }

    private static Optional<COSEAlgorithmIdentifier> fromString(long id) {
        Optional<COSEAlgorithmIdentifier> result = Optional.absent();
        for(COSEAlgorithmIdentifier item : values()){
            if(item.id == id){
                result = Optional.of(item);
            }
        }
        return result;
    }

    @JsonCreator
    private static COSEAlgorithmIdentifier fromJsonString(long id) {
        Optional<COSEAlgorithmIdentifier> result = fromString(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new IllegalArgumentException("Unknown COSE algorithm identifier: " + id);
    }

    @Override
    public long toJsonNumber() {
        return id;
    }

    public long getId() {
        return this.id;
    }
}
