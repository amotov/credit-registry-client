package com.bsbnb.creditregistry.client.language;

import com.bsbnb.creditregistry.client.util.*;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LanguageResources {

    public static void fixValues(Map<String, String> languageMap, Properties properties) {

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            languageMap.put(key, value);
        }
    }

    public static String getMessage(Locale locale, String key) {
        if (locale == null) {
            return null;
        }

        Map<String, String> languageMap = _languageMaps.get(locale);

        if (languageMap == null) {
            languageMap = _loadLocale(locale);
        }

        String value = languageMap.get(key);

        if (value == null) {
            return getMessage(getSuperLocale(locale), key);
        } else {
            return value;
        }
    }

    public static Locale getSuperLocale(Locale locale) {
        String variant = locale.getVariant();

        if (variant.length() > 0) {
            return new Locale(locale.getLanguage(), locale.getCountry());
        }

        String country = locale.getCountry();

        if (country.length() > 0) {
            Locale priorityLocale = LanguageUtil.getLocale(
                    locale.getLanguage());

            if ((priorityLocale != null) && (!locale.equals(priorityLocale))) {
                return new Locale(
                        priorityLocale.getLanguage(), priorityLocale.getCountry());
            }

            return LocaleUtil.fromLanguageId(locale.getLanguage());
        }

        String language = locale.getLanguage();

        if (language.length() > 0) {
            return _blankLocale;
        }

        return null;
    }

    public static Map<String, String> putLanguageMap(
            Locale locale, Map<String, String> languageMap) {

        Map<String, String> oldLanguageMap = _languageMaps.get(locale);

        if (oldLanguageMap == null) {
            _loadLocale(locale);
            oldLanguageMap = _languageMaps.get(locale);
        }

        Map<String, String> newLanguageMap = new HashMap<String, String>();

        if (oldLanguageMap != null) {
            newLanguageMap.putAll(oldLanguageMap);
        }

        newLanguageMap.putAll(languageMap);

        _languageMaps.put(locale, newLanguageMap);

        return oldLanguageMap;
    }

    public static void setConfig(String config) {
        _configNames = StringUtil.split(
                config.replace(CharPool.PERIOD, CharPool.SLASH));
    }

    private static Map<String, String> _loadLocale(Locale locale) {
        Map<String, String> languageMap = null;

        if (_configNames.length > 0) {
            String localeName = locale.toString();

            languageMap = new HashMap<String, String>();

            for (String name : _configNames) {
                StringBundler sb = new StringBundler(4);

                sb.append(name);

                if (localeName.length() > 0) {
                    sb.append(StringPool.UNDERLINE);
                    sb.append(localeName);
                }

                sb.append(".properties");

                Properties properties = _loadProperties(sb.toString());

                fixValues(languageMap, properties);
            }
        } else {
            languageMap = Collections.emptyMap();
        }

        _languageMaps.put(locale, languageMap);

        return languageMap;
    }

    private static Properties _loadProperties(String name) {
        Properties properties = new Properties();

        try {
            ClassLoader classLoader = LanguageResources.class.getClassLoader();

            URL url = classLoader.getResource(name);

            /*
             * if (_log.isInfoEnabled()) { _log.info("Attempting to load " +
             * name); }
             */

            if (url != null) {
                InputStream inputStream = url.openStream();

                properties = PropertiesUtil.load(inputStream, StringPool.UTF8);

                inputStream.close();

                /*
                 * if (_log.isInfoEnabled()) { _log.info( "Loading " + url + "
                 * with " + properties.size() + " values"); }
                 */
            }
        } catch (Exception e) {
            /*
             * if (_log.isWarnEnabled()) { _log.warn(e, e); }
             */
        }

        return properties;
    }
    //private static Log _log = LogFactoryUtil.getLog(LanguageResources.class);
    private static Locale _blankLocale = new Locale(StringPool.BLANK);
    private static String[] _configNames;
    private static Map<Locale, Map<String, String>> _languageMaps =
            new ConcurrentHashMap<Locale, Map<String, String>>(64);
}