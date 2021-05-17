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
package jssi.wallet.crypto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;

/**
 *
 * @author ITON Solutions
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KeysMetadata {

    @JsonProperty("keys")
    @JsonSerialize(using = KeysSerializer.class)
    private byte[] keys;

    @JsonProperty("master_key_salt")
    @JsonSerialize(using = KeysSerializer.class)
    private byte[] masterKeySalt;

    @JsonCreator
    public KeysMetadata(@JsonProperty("keys") byte[] keys,  @JsonProperty("master_key_salt") byte[] masterKeySalt) {
        this.keys = keys;
        this.masterKeySalt = masterKeySalt;
    }

    public byte[] getKeys() {
        return keys;
    }

    public byte[] getMasterKeySalt() {
        return masterKeySalt;
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof KeysMetadata)){
            return false;
        }
        KeysMetadata other = (KeysMetadata) object;
        return Arrays.equals(keys, other.keys) && Arrays.equals(masterKeySalt, other.masterKeySalt);
    }
}
