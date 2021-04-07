package com.in726.app.email;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for generation hash code
 */
public class HashCodeGenerator {

    /**
     * Method creates hash for string using SHA-512 algorithm.
     *
     * @param str string to be hash
     * @return hash code
     */
    public static String createHashCode(String str, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            byte[] messageDigest = md.digest(str.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashText = no.toString(16);

//            while (hashText.length() < 32) {
//                hashText = "0" + hashText;
//            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
