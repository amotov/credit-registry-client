package com.bsbnb.creditregistry.client.tools;

import com.bsbnb.creditregistry.client.util.Validator;
import java.util.HashMap;

public class ArgumentsMap extends HashMap<String, String> {

	@Override
	public String get(Object key) {
		String value = super.get(key);

		if (Validator.isNull(value)) {
			value = System.getProperty((String)key);
		}

		return value;
	}

}