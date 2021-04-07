package com.in726.app.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for main data of the user.
 */
@Data
@NoArgsConstructor
public class Persistent {
    private String username;
    private String password;
    private String email;
}
