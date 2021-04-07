package com.in726.app.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.in726.app.database.service.UserService;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Access manager for jwt usage
 */
public class JWTAccessManager implements AccessManager {
    private String userRoleClaim;
    private Map<String, Role> rolesMapping;
    private Role defaultRole;

    private static UserService userService = new UserService();

    /**
     * Creates JWTAccessManager
     *
     * @param userRoleClaim list of roles for users.
     * @param rolesMapping  mapper of role name and role.
     * @param defaultRole   role by default accesses for everyone
     */
    public JWTAccessManager(String userRoleClaim, Map<String, Role> rolesMapping, Role defaultRole) {
        this.userRoleClaim = userRoleClaim;
        this.rolesMapping = rolesMapping;
        this.defaultRole = defaultRole;
    }

    /**
     * Extracts role from token.
     *
     * @param context context of request.
     * @return role.
     */
    private Role extractRole(Context context) {
        if (!JavalinJWT.containsJWT(context)) {
            return defaultRole;
        }

        DecodedJWT jwt = JavalinJWT.getDecodedFromContext(context);
        String userLevel = jwt.getClaim(userRoleClaim).asString();

        return Optional.ofNullable(rolesMapping.get(userLevel)).orElse(defaultRole);
    }

    /**
     * Method manage role of user.
     *
     * @param handler        context handler.
     * @param context        context of request.
     * @param permittedRoles set of allowed roles.
     * @throws Exception handler exception.
     */
    @Override
    public void manage(Handler handler, Context context, Set<Role> permittedRoles) throws Exception {
        Role role = extractRole(context);
        if (permittedRoles.contains(role)) {
            handler.handle(context);
        } else {
            context.status(401).result("Unauthorized user");
        }
    }

    /**
     * Method checks is the user id and user token are bind.
     *
     * @param token  jwt token
     * @param userId user id
     * @return true or false
     */
    public static boolean isTokenBelongsToUser(DecodedJWT token, int userId) {
        var usernameFromToken = token.getClaim("username").asString();
        var user = userService.getUserById(userId);
        if (user != null && user.getUsername().equals(usernameFromToken))
            return true;
        else
            return false;
    }

    /**
     * Method checks is the user belongs to token.
     *
     * @param token  jwt token
     * @param username username
     * @return true or false
     */
    public static boolean isTokenBelongsToUser(DecodedJWT token, String username) {
        var usernameFromToken = token.getClaim("username").asString();
        if (username.equals(usernameFromToken))
            return true;
        else
            return false;
    }
}
