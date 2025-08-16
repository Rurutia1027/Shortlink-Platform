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