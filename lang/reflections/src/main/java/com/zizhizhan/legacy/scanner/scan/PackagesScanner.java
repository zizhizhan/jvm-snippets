package com.zizhizhan.legacy.scanner.scan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizhizhan.legacy.scanner.Closure;
import com.zizhizhan.legacy.scanner.ClosureException;
import com.zizhizhan.legacy.scanner.io.Closing;
import com.zizhizhan.legacy.reflect.ReflectionHelper;


public class PackagesScanner implements Scanner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ClassLoader classloader;
	private final String[] packages;

	public PackagesScanner(String... packages) {
		this(ReflectionHelper.getContextClassLoader(), packages);
	}

	public PackagesScanner(ClassLoader classloader, String... packages) {
		this.classloader = classloader;
		this.packages = packages;
	}

	public void scan(ScannerListener sl) {		
		for (String p : packages) {
			try {
				Enumeration<URL> urls = classloader.getResources(p.replace(".", "/"));
				if (urls != null) {
					while (urls.hasMoreElements()) {
						URL url = urls.nextElement();
						scan(toURI(url), sl);
					}
				}
			} catch (Exception e) {
				logger.warn("Can't find resource for " + p, e);
			} 
		}
		
	}

	private void scan(URI uri, final ScannerListener sl) {
		String scheme = uri.getScheme();
		if("file".equalsIgnoreCase(scheme)){
			FileScanner.scan(new File(uri.getPath()), sl);
		}else if("zip".equalsIgnoreCase(scheme) || "jar".equalsIgnoreCase(scheme)){
			final String ssp = uri.getRawSchemeSpecificPart();
	        final String jarUrlString = ssp.substring(0, ssp.lastIndexOf('!'));
	        final String parent = ssp.substring(ssp.lastIndexOf('!') + 2);
	        try {
	            closing(jarUrlString).f(new Closure<InputStream>() {
	                public void f(final InputStream in) {
	                    try {
							JarFileScanner.scan(in, parent, sl);
						} catch (IOException e) {						
							throw new ClosureException(e);
						}
	                }
	            });
	        } catch (Exception ex) {
	            throw new RuntimeException("IO error when scanning jar " + uri, ex);
	        }
		}		
	}
	
	protected Closing<InputStream> closing(String jarUrlString) throws IOException {
		return new Closing<InputStream>(new URL(jarUrlString).openStream());
	}

	private URI toURI(URL url) throws URISyntaxException {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {			
			return URI.create(toExternalForm(url));
		}
	}

	private String toExternalForm(URL u) {
		StringBuffer result = new StringBuffer();
		result.append(u.getProtocol());
		result.append(":");
		if (u.getAuthority() != null && u.getAuthority().length() > 0) {
			result.append("//");
			result.append(u.getAuthority());
		}
		if (u.getPath() != null) {
			result.append(u.getPath());
		}
		if (u.getQuery() != null) {
			result.append('?');
			result.append(u.getQuery());
		}
		if (u.getRef() != null) {
			result.append("#");
			result.append(u.getRef());
		}
		return result.toString();
	}

}
