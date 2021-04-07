package com.in726.app.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class contains data from user that should be in token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockUser {
    private String username;
    private String level;
}
