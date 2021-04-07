package com.in726.app.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.InternalServerErrorResponse;

import java.util.Optional;

/**
 * JWT handler
 */
public class JavalinJWT {

    private final static String CONTEXT_ATTRIBUTE = "jwt";
//    private final static String COOKIE_KEY = "jwt";

    /**
     * Checks JWT token in the context of request.
     *
     * @param context context.
     * @return true - if context contains token or false - in any other case.
     */
    public static boolean containsJWT(Context context) {
        return context.attribute(CONTEXT_ATTRIBUTE) != null;
    }

    /**
     * Adds to context the attribute jwt with token.
     *
     * @param context context.
     * @param jwt     token.
     * @return context.
     */
    public static Context addDecodedToContext(Context context, DecodedJWT jwt) {
        context.attribute(CONTEXT_ATTRIBUTE, jwt);
        return context;
    }

    /**
     * Gets jwt token from context.
     *
     * @param context context.
     * @return DecodedJWT - jwt token
     */
    public static DecodedJWT getDecodedFromContext(Context context) {
        Object attribute = context.attribute(CONTEXT_ATTRIBUTE);

        if (!DecodedJWT.class.isInstance(attribute)) {
            throw new InternalServerErrorResponse("The context carried invalid object as JavalinJWT");
        }

        return (DecodedJWT) attribute;
    }

    /**
     * Gets jwt token from Authorization header.
     *
     * @param context context.
     * @return jwt token.
     */
    public static Optional<String> getTokenFromHeader(Context context) {
        return Optional.ofNullable(context.header("Authorization"))
                .flatMap(header -> {
                    String[] split = header.split(" ");
                    if (split.length != 2 || !split[0].equals("Bearer")) {
                        return Optional.empty();
                    }

                    return Optional.of(split[1]);
                });
    }

    /**
     * Gets jwt token from cookies.
     *
     * @param context context.
     * @return jwt token.
     */
//    public static Optional<String> getTokenFromCookie(Context context) {
//        return Optional.ofNullable(context.cookie(COOKIE_KEY));
//    }

    /**
     * Adds jwt token to Cookies.
     *
     * @param context contest.
     * @param token   jwt token.
     * @return context.
     */
//    public static Context addTokenToCookie(Context context, String token) {
//        return context.cookie(COOKIE_KEY, token);
//    }

    /**
     * Creates handler for decoding jwt token from Authorisation header.
     *
     * @param jwtProvider jwt provider.
     * @return handler.
     */
    public static Handler createHeaderDecodeHandler(JWTProvider jwtProvider) {
        return context -> getTokenFromHeader(context)
                .flatMap(jwtProvider::validateToken)
                .ifPresent(jwt -> JavalinJWT.addDecodedToContext(context, jwt));
    }

    /**
     * Creates handler for decoding jwt token from Cookie.
     *
     * @param jwtProvider jwt provider.
     * @return handler.
     */
//    public static Handler createCookieDecodeHandler(JWTProvider jwtProvider) {
//        return context -> getTokenFromCookie(context)
//                .flatMap(jwtProvider::validateToken)
//                .ifPresent(jwt -> JavalinJWT.addDecodedToContext(context, jwt));
//    }
}