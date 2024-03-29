package com.bsbnb.creditregistry.client.util.memory;

import java.lang.ref.SoftReference;

/**
 * @author Shuyang Zhou
 */
public class SoftReferenceThreadLocal<T> extends ThreadLocal<T> {

	@Override
	public T get() {
		SoftReference<T> softReference = _softReferenceThreadLocal.get();

		if (softReference == _nullSoftReference) {
			return null;
		}

		T value = null;

		if (softReference != null) {
			value = softReference.get();
		}

		if (value == null) {
			value = initialValue();

			set(value);
		}

		return value;
	}

	@Override
	public void remove() {
		_softReferenceThreadLocal.remove();
	}

	@Override
	public void set(T value) {
		if (value == null) {
			_softReferenceThreadLocal.set((SoftReference<T>)_nullSoftReference);
		}
		else {
			_softReferenceThreadLocal.set(new SoftReference<T>(value));
		}
	}

	private static SoftReference<Object> _nullSoftReference =
		new SoftReference<Object>(null);

	private ThreadLocal<SoftReference<T>> _softReferenceThreadLocal =
		new ThreadLocal<SoftReference<T>>();

}