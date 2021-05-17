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

package ssido.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class CollectionUtil {

    /**
     * Make an unmodifiable shallow copy of the argument.
     *
     * @return A shallow copy of <code>m</code> which cannot be modified
     */
    public static <K, V> Map<K, V> immutableMap(Map<K, V> m) {
        return Collections.unmodifiableMap(new HashMap<>(m));
    }

    /**
     * Make an unmodifiable shallow copy of the argument.
     *
     * @return A shallow copy of <code>l</code> which cannot be modified
     */
    public static <T> List<T> immutableList(List<T> l) {
        return Collections.unmodifiableList(new ArrayList<>(l));
    }

    /**
     * Make an unmodifiable shallow copy of the argument.
     *
     * @return A shallow copy of <code>s</code> which cannot be modified
     */
    public static <T> Set<T> immutableSet(Set<T> s) {
        return Collections.unmodifiableSet(new HashSet<>(s));
    }

    /**
     * Make an unmodifiable shallow copy of the argument.
     *
     * @return A shallow copy of <code>s</code> which cannot be modified
     */
    public static <T> Set<T> immutableSortedSet(SortedSet<T> s) {
        return Collections.unmodifiableSortedSet(new TreeSet<>(s));
    }

}
