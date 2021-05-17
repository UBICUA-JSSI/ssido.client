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
package jssi.store.model;

import java.util.Collection;

public class Item {

    private Integer id;
    private byte[] type;
    private byte[] name;
    private byte[] value;
    private byte[] key;
    Collection<Plaintext> plaintext;
    Collection<Encrypted> encrypted;

    public Item(){}

    public Item(byte[] type, byte[] name, byte[] value, byte[] key) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.key = key;
    }

    public Item(int id, byte[] type, byte[] name, byte[] value, byte[] key) {
        this(type, name, value, key);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getType() {
        return type;
    }

    public void setType(byte[] type) {
        this.type = type;
    }

    public byte[] getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public Collection<Encrypted> getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Collection<Encrypted> encrypted) {
        this.encrypted = encrypted;
    }

    public Collection<Plaintext> getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(Collection<Plaintext> plaintext) {
        this.plaintext = plaintext;
    }

    @Override
    public String toString() {
        return "Item[ id=" + id + " ]";
    }
}
