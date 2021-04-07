package com.in726.app.email;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class reads data for connection to gmail service.
 */
public class EmailDataReader {

    /**
     * Method reads data to access mail box.
     *
     * @return data to access mail box.
     * @throws IOException exception of reading from the file.
     */
    public static EmailAccount readAccessData(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String email = br.readLine();
            String password = br.readLine();
            return new EmailAccount(email, password);
        }
    }
}
