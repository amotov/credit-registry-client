package com.bsbnb.creditregistry.client.util;

//import com.sun.syndication.io.XmlReader;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Brian Wing Shun Chan
 */
public class InitUtil {

	public static synchronized void init() {
		if (_initialized) {
			return;
		}

		StopWatch stopWatch = null;

		if (_PRINT_TIME) {
			stopWatch = new StopWatch();

			stopWatch.start();
		}

		// Set the default locale used by Liferay. This locale is no longer set
		// at the VM level. See LEP-2584.

		/*String userLanguage = SystemProperties.get("user.language");
		String userCountry = SystemProperties.get("user.country");
		String userVariant = SystemProperties.get("user.variant");*/
                
                
                String userLanguage = "ru";
                String userCountry = "RU";
                String userVariant = "";

		LocaleUtil.setDefault(userLanguage, userCountry, userVariant);

		// Java properties

		JavaProps.isJDK5();

		if (_PRINT_TIME) {
			System.out.println(
				"InitAction takes " + stopWatch.getTime() + " ms");
		}

		_initialized = true;
	}

	public synchronized static void initWithSpring() {
		initWithSpring(false);
	}

	public synchronized static void initWithSpring(boolean force) {
		if (force) {
			_initialized = false;
		}

		if (_initialized) {
			return;
		}

		/*if (!_neverInitialized) {
			PropsUtil.reload();
		}
		else {
			_neverInitialized = false;
		}*/

		init();

		/*SpringUtil.loadContext();*/

		_initialized = true;
	}

	private static final boolean _PRINT_TIME = false;

	private static boolean _initialized;
	private static boolean _neverInitialized = true;

}