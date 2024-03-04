package org.kopi.ebics.messages.h005;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    /**
     * Default locale used for setting language
     * can be overridden by Locale.setDefault(...)
     */
    private static final Locale locale = Locale.getDefault();

    /**
     * Return the corresponding value of a given key and string parameter.
     *
     * @param key        the given key
     * @param bundleName the bundle name
     * @param param      the string parameter
     * @return the corresponding key value
     */
    public static String getString(String key, String bundleName, String param) {
        return getString(key, bundleName, locale, param);
    }

    /**
     * Return the corresponding value of a given key and integer parameter.
     *
     * @param key        the given key
     * @param bundleName the bundle name
     * @param param      the int parameter
     * @return the corresponding key value
     */
    public static String getString(String key, String bundleName, int param) {
        return getString(key, bundleName, locale, param);
    }

    /**
     * Return the corresponding value of a given key and parameters.
     *
     * @param key        the given key
     * @param bundleName the bundle name
     * @return the corresponding key value
     */
    public static String getString(String key, String bundleName) {
        return getString(key, bundleName, locale);
    }

    /**
     * Return the corresponding value of a given key and string parameter.
     *
     * @param key        the given key
     * @param bundleName the bundle name
     * @param locale     the bundle locale
     * @param param      the parameter
     * @return the corresponding key value
     */
    public static String getString(String key, String bundleName, Locale locale, String param) {
        try {

            ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, locale);
            return MessageFormat.format(resourceBundle.getString(key), param);
        } catch (MissingResourceException e) {
            return "!!" + key + "!!";
        } catch (NullPointerException npe) {
            return (param != null) ? "!!" + key + " with param " + param + "!!" : "!!" + key + "!!";
        }
    }

    /**
     * Return the corresponding value of a given key and integer parameter.
     *
     * @param key        the given key
     * @param bundleName the bundle name
     * @param locale     the bundle locale
     * @param param      the parameter
     * @return the corresponding key value
     */
    public static String getString(String key, String bundleName, Locale locale, int param) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, locale);
            return MessageFormat.format(resourceBundle.getString(key), param);
        } catch (MissingResourceException | NullPointerException e) {
            return "!!" + key + "!!";
        }
    }

    /**
     * Return the corresponding value of a given key and parameters.
     *
     * @param key        the given key
     * @param bundleName the bundle name
     * @param locale     the bundle locale
     * @return the corresponding key value
     */
    public static String getString(String key, String bundleName, Locale locale) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, locale);
            return resourceBundle.getString(key);
        } catch (MissingResourceException | NullPointerException e) {
            return "!!" + key + "!!";
        }
    }
}
