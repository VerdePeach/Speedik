package com.in726.app.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.in726.app.Speedik;
import com.in726.app.database.service.UserService;
import com.in726.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

/**
 * Class for change user password.
 */
@Data
@NoArgsConstructor
public class PasswordChanger {
    private String oldPassword;
    private String newPassword;

    @JsonIgnore
    private UserService userService = new UserService();

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(Speedik.class);

    private boolean isOldPasswordRight(User user) throws NoSuchAlgorithmException {
        encodePasswords();
        if (user.getPassword().equals(oldPassword)) {
            return true;
        }
        return false;
    }

    private void encodePasswords() throws NoSuchAlgorithmException {
        oldPassword = PasswordEncoder.hashPassword(oldPassword);
        newPassword = PasswordEncoder.hashPassword(newPassword);
    }

    /**
     * Method changes old user password to new user password.
     *
     * @param user user whose password is changing.
     * @throws Exception old password does not match.
     */
    public void changePassword(User user) throws Exception {
        if (isOldPasswordRight(user)) {
            user.setPassword(newPassword);
            userService.update(user);
            logger.info("Password successfully updated. username " + user.getUsername() + " id " + user.getId());
        } else {
            logger.error("Old password is not matched. " + user.getUsername() + " id " + user.getId());
            //TODO: create custom exception for password matching.
            throw new Exception("Old password does not match.");
        }
    }
}
