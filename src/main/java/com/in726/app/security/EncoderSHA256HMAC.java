package com.in726.app.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Class for encoding in SHA256 HMAC algorithm.
 */
public class EncoderSHA256HMAC {

    /**
     * Method encode string by secret key with SHA256 HMAC.
     *
     * @param secretKey
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String encodeString(String secretKey, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        final Charset asciiCs = Charset.forName("US-ASCII");
        final Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        final SecretKeySpec secret_key =
                new javax.crypto.spec.SecretKeySpec(asciiCs.encode(secretKey).array(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        final byte[] mac_data = sha256_HMAC.doFinal(asciiCs.encode(str).array());
        String result = "";
        for (final byte element : mac_data) {
            result += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
