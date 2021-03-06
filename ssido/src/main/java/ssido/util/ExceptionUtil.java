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


import android.util.Log;

public class ExceptionUtil {

    private static final String TAG = ExceptionUtil.class.getName();

    public static RuntimeException wrapAndLog(Log log, String message, Throwable t) {
        RuntimeException err = new RuntimeException(message, t);
        log.e(TAG, err.getMessage(), err);
        return err;
    }

    public static void assure(boolean condition, String failureMessageTemplate, Object... failureMessageArgs) {
        if (!condition) {
            throw new IllegalArgumentException(String.format(failureMessageTemplate, failureMessageArgs));
        }
    }

}
