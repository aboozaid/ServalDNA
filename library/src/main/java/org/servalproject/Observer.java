package org.servalproject;

public interface Observer<T> {
	void updated(T obj);
}
