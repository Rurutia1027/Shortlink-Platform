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

package org.ucd.shortlink.project.toolkit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HashUtilTest {
    @Test
    void testHashToBase62Consistency() {
        String input = "shortlink";
        String hash1 = HashUtil.hashToBase62(input);
        String hash2 = HashUtil.hashToBase62(input);

        assertNotNull(hash1);
        assertNotNull(hash2);
        assertFalse(hash1.isEmpty());
        assertEquals(hash1, hash2); // deterministic
    }

    @Test
    void testHashToBase62DifferentInputs() {
        String hash1 = HashUtil.hashToBase62("abc");
        String hash2 = HashUtil.hashToBase62("def");

        assertNotEquals(hash1, hash2); // different strings produce different outputs
    }

    @Test
    void testHashToBase62EmptyString() {
        String hash = HashUtil.hashToBase62("");
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    void testHashToBase62NegativeHashConversion() {
        // MurmurHash might generate negative number internally
        String hash = HashUtil.hashToBase62("negative-test");
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }
}