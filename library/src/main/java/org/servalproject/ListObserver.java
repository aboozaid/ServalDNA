package org.servalproject;

/**
 * Created by jeremy on 11/05/16.
 */
public interface ListObserver<T> {
	void added(T obj);

	void removed(T obj);

	void updated(T obj);

	void reset();
}
