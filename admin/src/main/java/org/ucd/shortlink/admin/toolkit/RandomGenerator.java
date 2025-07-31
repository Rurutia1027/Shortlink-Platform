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
