package com.in726.app.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encoder for user passwords.
 */
public class PasswordEncoder {

    /**
     * Creates a hash from the user`s password.
     *
     * @param password user password
     * @return hash of password.
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashText = no.toString(16);
//            while (hashText.length() < 32) {
//                hashText = "0" + hashText;
//            }
        return hashText;

    }
}
