package com.in726.app.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class provides model of data for connection to gmail service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAccount {
    private String email;
    private String password;
}
