package com.bsbnb.creditregistry.client.language;

import com.bsbnb.creditregistry.client.util.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author Brian Wing Shun Chan
 * @author Andrius Vitkauskas
 */
public class LanguageUtil {

    public static String format(Locale locale, String pattern, Object argument) {
        return format(locale, pattern, new Object[]{argument}, true);
    }

    public static String format(
            Locale locale, String pattern, Object argument,
            boolean translateArguments) {

        return format(
                locale, pattern, new Object[]{argument}, translateArguments);
    }

    public static String format(Locale locale, String pattern, Object[] arguments) {
        return format(locale, pattern, arguments, true);
    }

    public static String format(
            Locale locale, String pattern, Object[] arguments,
            boolean translateArguments) {

        String value = null;

        try {
            pattern = get(locale, pattern);

            if ((arguments != null) && (arguments.length > 0)) {
                pattern = _escapePattern(pattern);

                Object[] formattedArguments = new Object[arguments.length];

                for (int i = 0; i < arguments.length; i++) {
                    if (translateArguments) {
                        formattedArguments[i] = get(
                                locale, arguments[i].toString());
                    } else {
                        formattedArguments[i] = arguments[i];
                    }
                }

                value = MessageFormat.format(pattern, formattedArguments);
            } else {
                value = pattern;
            }
        } catch (Exception e) {
            /*
             * if (_log.isWarnEnabled()) { _log.warn(e, e);
			}
             */
        }

        return value;
    }

    public static String get(Locale locale, String key) {
        return get(locale, key, key);
    }

    public static String get(Locale locale, String key, String defaultValue) {

        if (key == null) {
            return null;
        }

        String value = LanguageResources.getMessage(locale, key);

        while ((value == null) || value.equals(defaultValue)) {
            if ((key.length() > 0)
                    && (key.charAt(key.length() - 1) == CharPool.CLOSE_BRACKET)) {

                int pos = key.lastIndexOf(CharPool.OPEN_BRACKET);

                if (pos != -1) {
                    key = key.substring(0, pos);

                    value = LanguageResources.getMessage(locale, key);

                    continue;
                }
            }

            break;
        }

        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static Locale[] getAvailableLocales() {
        return _getInstance()._locales;
    }

    public static String getCharset(Locale locale) {
        return _getInstance()._getCharset(locale);
    }

    public static String getLanguageId(Locale locale) {
        return LocaleUtil.toLanguageId(locale);
    }

    public static Locale getLocale(String languageCode) {
        return _getInstance()._getLocale(languageCode);
    }

    public static boolean isAvailableLocale(Locale locale) {
        return _getInstance()._localesSet.contains(locale);
    }

    public static boolean isDuplicateLanguageCode(String languageCode) {
        return _getInstance()._duplicateLanguageCodes.contains(languageCode);
    }

    private static LanguageUtil _getInstance() {
        return _instance;
    }

    private LanguageUtil() {
        //TODO Либо вынести в файл констант, либо сделать тут константу
        String[] localesArray = new String[]{};

        _charEncodings = new HashMap<String, String>();
        _duplicateLanguageCodes = new HashSet<String>();
        _locales = new Locale[localesArray.length];
        _localesMap = new HashMap<String, Locale>(localesArray.length);
        _localesSet = new HashSet<Locale>(localesArray.length);

        for (int i = 0; i < localesArray.length; i++) {
            String languageId = localesArray[i];

            Locale locale = LocaleUtil.fromLanguageId(languageId);

            _charEncodings.put(locale.toString(), StringPool.UTF8);

            String language = languageId;

            int pos = languageId.indexOf(CharPool.UNDERLINE);

            if (pos > 0) {
                language = languageId.substring(0, pos);
            }

            if (_localesMap.containsKey(language)) {
                _duplicateLanguageCodes.add(language);
            }

            _locales[i] = locale;

            if (!_localesMap.containsKey(language)) {
                _localesMap.put(language, locale);
            }

            _localesSet.add(locale);
        }

    }

    private static String _escapePattern(String pattern) {
        return StringUtil.replace(
                pattern, StringPool.APOSTROPHE, StringPool.DOUBLE_APOSTROPHE);
    }

    private static String _get(Locale locale, String key, String defaultValue) throws Exception {
        if (key == null) {
            return null;
        }

        String value = null;

        //TODO Заменить на константу
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Language", locale);
        value = ResourceBundleUtil.getString(resourceBundle, key);

        /*
         * if (value != null) { value = LanguageResources.fixValue(value);
                }
         */

        if ((value == null) || value.equals(defaultValue)) {
            value = LanguageResources.getMessage(locale, key);
        }

        if ((value == null) || value.equals(defaultValue)) {
            if ((key.length() > 0)
                    && (key.charAt(key.length() - 1) == CharPool.CLOSE_BRACKET)) {

                int pos = key.lastIndexOf(CharPool.OPEN_BRACKET);

                if (pos != -1) {
                    key = key.substring(0, pos);

                    return _get(
                            locale, key, defaultValue);
                }
            }
        }

        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    private static String _getCharset(Locale locale) {
        return StringPool.UTF8;
    }

    private static Locale _getLocale(String languageCode) {
        return _getInstance()._localesMap.get(languageCode);
    }

    /*
     * private static Log _log = LogFactoryUtil.getLog(LanguageImpl.class);
     */
    private static LanguageUtil _instance = new LanguageUtil();
    private Map<String, String> _charEncodings;
    private Set<String> _duplicateLanguageCodes;
    private Locale[] _locales;
    private Map<String, Locale> _localesMap;
    private Set<Locale> _localesSet;
    
}