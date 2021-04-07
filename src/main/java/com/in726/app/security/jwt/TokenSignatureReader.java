package com.in726.app.security.jwt;

import java.io.*;

/**
 * Class for reading token signature.
 */
public class TokenSignatureReader {

    private static String signature;

    /**
     * Method reads signature from file.
     *
     * @return read signature.
     * @throws IOException reading exception.
     */
    public static String readSignature() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("tokenSignature.txt"))) {
            signature = br.readLine();
            return signature;
        }
    }
}
