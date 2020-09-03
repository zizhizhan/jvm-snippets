package com.zizhizhan.legacies.core.io;

import com.zizhizhan.legacies.core.Closure;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Closing<T extends Closeable> {

	private final T resource;
	private final boolean ignoreClosingException;

	public Closing(final T resource) {
		this(resource, false);
	}

	public Closing(final T resource, boolean ignoreClosingException) {
		this.resource = resource;
		this.ignoreClosingException = ignoreClosingException;
	}

	public static Closing<InputStream> with(final InputStream in) {
		return new Closing<InputStream>(in);
	}

	public static Closing<OutputStream> with(final OutputStream out) {
		return new Closing<OutputStream>(out);
	}

	public void f(final Closure<T> c) {
		if (resource != null) {
			try {
				c.f(resource);
			} finally {
				close(resource);
			}
		}
	}

	private void close(Closeable resource) {
		try {
			resource.close();
		} catch (IOException ex) {
			if (!ignoreClosingException) {
				throw new ClosingException("Can't close the resource instance " + resource, ex);
			}
		}
	}

}
