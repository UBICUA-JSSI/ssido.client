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
package jssi.did;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author UBICUA
 */
public class TheirDid {

    public static final String TYPE = "Indy::TheirDid";

    public String did;
    public String verkey;

    @JsonCreator
    public TheirDid(@JsonProperty("did") String did, @JsonProperty("verkey") String verkey){
        this.did = did;
        this.verkey = verkey;
    }
    
    @Override
    public String toString(){
        return String.format("TheirDid: {%s, verkey: %s}", did, verkey);
    }
}
