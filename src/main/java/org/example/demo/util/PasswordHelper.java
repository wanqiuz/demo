package org.example.demo.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * main class for hash of password and secure compare
 */
public class PasswordHelper {

    //    private static final ThreadLocal<Random> SECURE_RANDOM =
    //            ThreadLocal.withInitial(
    //                    () -> {
    //                        final SecureRandom rnd = new SecureRandom();
    //                        rnd.nextBoolean();
    //                        return rnd;
    //                    });
    //
    //    public static String randomToken() {
    //        byte[] bytes = new byte[20];
    //        SECURE_RANDOM.get().nextBytes(bytes);
    //        return new String(bytes, StandardCharsets.UTF_8);
    //    }
    //
    //    public static byte[] randomSalt() {
    //        byte[] bytes = new byte[20];
    //        SECURE_RANDOM.get().nextBytes(bytes);
    //        return bytes;
    //    }

    private static final int TOKEN_BYTES = 150;

    private static final int SALT_BYTES = 30;

    /** Due to homework requirements, use Random instead of SecureRandom */
    public static String randomToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        ThreadLocalRandom.current().nextBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Generate random salt
     *
     * @return random salt
     */
    public static byte[] randomSalt() {
        byte[] bytes = new byte[SALT_BYTES];
        ThreadLocalRandom.current().nextBytes(bytes);
        return bytes;
    }

    /**
     * Calculate hash of salt (int byte array) plus password (int byte array) Using 'SHA-256'
     *
     * @return hash
     */
    public static byte[] calculateHash(String password, byte[] salt) {
        final byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        }

        return md.digest(addBytes(passwordBytes, salt));
    }

    /**
     * @param password plaintext password
     * @param salt salt of previous password
     * @param hash hash of previous password
     * @return if password correct
     */
    public static boolean checkPassword(String password, byte[] salt, byte[] hash) {
        byte[] newHash = calculateHash(password, salt);
        return slowEquals(newHash, hash);
    }

    /**
     * merge two byte array to a single byte array
     *
     * @param a byte array a
     * @param b byte array b
     * @return merged byte array
     */
    private static byte[] addBytes(byte[] a, byte[] b) {
        byte[] data3 = new byte[a.length + b.length];
        System.arraycopy(a, 0, data3, 0, a.length);
        System.arraycopy(b, 0, data3, a.length, b.length);
        return data3;
    }

    /**
     * Compare if two byte array are equal This process does not end quickly because of the
     * difference in the preceding characters, but character by character, until the end Caller
     * ensure a and b is not null
     *
     * @param a byte array a
     * @param b byte array b
     * @return if two byte array are equal
     */
    public static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
