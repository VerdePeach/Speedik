package com.in726.app.security;

import com.in726.app.model.User;
import com.in726.app.model.sub_functional_model.Link;
import com.in726.app.tariff.TariffUtil;
import org.apache.commons.validator.UrlValidator;

import java.util.regex.Pattern;

/**
 * Class for validation user data
 */
public class UserDataValidator {

    /**
     * Method for username validation.
     *
     * @param username username
     * @return true or false
     */
    public static boolean isUsername(String username) {
        var pattern = Pattern.compile("^[a-zA-z0-9]{4,}$");
        var matcher = pattern.matcher(username);
        if (matcher.find())
            return true;
        else
            return false;
    }

    /**
     * Method for user email validation.
     *
     * @param email username
     * @return true or false
     */
    public static boolean isEmail(String email) {
        var pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        var matcher = pattern.matcher(email);
        if (matcher.find())
            return true;
        else
            return false;
    }

    /**
     * Method for user link validation.
     *
     * @param link url
     * @return true or false
     */
    public static boolean isLink(Link link, User user) {
        if (link == null) {
            return false;
        }
        var urlValidator = new UrlValidator();
        var patternName = Pattern.compile("^[a-zA-z0-9 ]+$");
        var patternWords = Pattern.compile("^[a-zA-z0-9\\-]+$");
        var matcher = patternName.matcher(link.getName());

        link.setUrl(link.getUrl().trim());
        link.setName(link.getName().trim());

        final boolean[] areWordsWrong = {false};
        link.getWords().forEach(w -> {
            if (!patternWords.matcher(w.getValue()).find()) {
                areWordsWrong[0] = true;
            }
        });

        if (!link.getUrl().equals("")
                && !link.getName().equals("")
                && matcher.find()
                && link.getSecondsToCheck() > 0
                && TariffUtil.checkLinkForTariff(link, user)
                && urlValidator.isValid(link.getUrl())
                && !areWordsWrong[0]) {
            return true;
        } else
            return false;
    }

    /**
     * Method to check keys of agent.
     *
     * @param publicKey
     * @param secretKey
     * @return
     */
    public static boolean isAgentWithKeys(String publicKey, String secretKey) {
        var pattern = Pattern.compile("^[a-zA-z0-9\\-]+$");
        if (pattern.matcher(publicKey).find() && pattern.matcher(secretKey).find())
            return true;
        else
            return false;
    }

    public static boolean isDashboardDescription(String description) {
        var pattern = Pattern.compile("(<script>)|(</script>)");
        if (pattern.matcher(description).find())
            return false;
        else
            return true;
    }
}
