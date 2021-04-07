package com.in726.app.security;

import java.util.Random;

/**
 * Class that creates random passwords
 */
public class PasswordCreator {

    /**
     * Method creates random password using english letters.
     * The length of a generated password is 10 characters.
     *
     * @return generated password
     */
    public static String createPassword() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
