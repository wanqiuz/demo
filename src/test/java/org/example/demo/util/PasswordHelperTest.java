package org.example.demo.util;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Test;

public class PasswordHelperTest {

    /** Two consecutive random token should be different */
    @Test
    public void randomToken() {
        String token1 = PasswordHelper.randomToken();
        String token2 = PasswordHelper.randomToken();
        assertNotEquals(token1, token2);
    }

    /** test if calculating hash of salt (int byte array) plus password (int byte array) is right */
    @Test
    public void calculateHash() {
        byte[] salt = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        String password = "4562499011323";
        byte[] hash =
                new byte[] {
                    -27, -58, 75, 31, 94, -45, 12, 100, -109, 26, -69, 68, -108, -11, 123, -85, -94,
                    126, 96, 114, 118, 39, 114, 2, -52, -21, -110, 36, -64, -55, -59, 84
                };
        byte[] result = PasswordHelper.calculateHash(password, salt);
        assertArrayEquals(hash, result);
    }

    /** test if password equals another password stored in salt and hash */
    @Test
    public void checkPassword() {
        byte[] salt = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        byte[] hash =
                new byte[] {
                    -27, -58, 75, 31, 94, -45, 12, 100, -109, 26, -69, 68, -108, -11, 123, -85, -94,
                    126, 96, 114, 118, 39, 114, 2, -52, -21, -110, 36, -64, -55, -59, 84
                };
        String password = "4562499011323";
        boolean result = PasswordHelper.checkPassword(password, salt, hash);
        Assert.assertTrue(result);
    }

    /** test logic of merging two byte array to a single byte array */
    @Test
    public void addBytes() {
        byte[] a = new byte[] {1, 2, 3};
        byte[] b = new byte[] {4, 5, 6};
        byte[] c = new byte[] {1, 2, 3, 4, 5, 6};
        try {
            Method declaredMethod =
                    PasswordHelper.class.getDeclaredMethod("addBytes", byte[].class, byte[].class);
            declaredMethod.setAccessible(true);
            byte[] merged = (byte[]) declaredMethod.invoke(PasswordHelper.class, a, b);
            assertArrayEquals(c, merged);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Assert.assertFalse(false);
        }
    }

    /** slow compare two byte array with different length */
    @Test
    public void differentLengthSlowEquals() {
        byte[] a = new byte[] {1, 2, 3};
        byte[] b = new byte[] {1, 2, 3, 4};
        boolean result = PasswordHelper.slowEquals(a, b);
        Assert.assertFalse(result);
    }

    /** slow compare two byte array with same length but different content */
    @Test
    public void sameLengthDifferentContentSlowEquals() {
        byte[] a = new byte[] {1, 2, 3};
        byte[] b = new byte[] {1, 2, 4};
        boolean result = PasswordHelper.slowEquals(a, b);
        Assert.assertFalse(result);
    }

    /** slow compare two byte array with same length and same content */
    @Test
    public void sameLengthSameContentSlowEquals() {
        byte[] a = new byte[] {1, 2, 3};
        byte[] b = new byte[] {1, 2, 3};
        boolean result = PasswordHelper.slowEquals(a, b);
        Assert.assertTrue(result);
    }
}
