/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ucd.shortlink.admin.toolkit;

import java.security.SecureRandom;

/**
 * Short link group random ID generator
 */
public final class RandomGenerator {
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Random group ID generator
     *
     * @return random generated group id
     */
    public static String generateRandom() {
        return generateRandom(6);
    }

    /**
     * Random group ID generator with specified length
     *
     * @param length of generated random group id
     * @return random generated group id
     */
    public static String generateRandom(int length) {
        StringBuilder sub = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sub.append(CHARACTERS.charAt(randomIndex));
        }
        return sub.toString();
    }
}
