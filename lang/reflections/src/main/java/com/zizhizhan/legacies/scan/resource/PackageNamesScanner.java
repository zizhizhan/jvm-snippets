package com.zizhizhan.legacies.scan.resource;

import java.io.IOException;
import java.lang.reflect.ReflectPermission;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.zizhizhan.legacies.scan.util.ReflectionHelper;
import com.zizhizhan.legacies.scan.resource.uri.FileSchemeScanner;
import com.zizhizhan.legacies.scan.resource.uri.JarZipSchemeScanner;
import com.zizhizhan.legacies.scan.resource.uri.UriSchemeScanner;
import com.zizhizhan.legacies.scan.resource.uri.VfsSchemeScanner;

public class PackageNamesScanner implements Scanner {

    private final String[] packages;
    private final ClassLoader classloader;
    private final Map<String, UriSchemeScanner> scanners;

    public PackageNamesScanner(final String... packages) {
        this(ReflectionHelper.getContextClassLoader(), packages);
    }

    public PackageNamesScanner(final ClassLoader classloader, final String[] packages) {
        this.packages = packages;
        this.classloader = classloader;

        this.scanners = new HashMap<String, UriSchemeScanner>();
        add(new JarZipSchemeScanner());
        add(new FileSchemeScanner());
        add(new VfsSchemeScanner());

    }

    private void add(final UriSchemeScanner ss) {
        for (final String s : ss.getSchemes()) {
            scanners.put(s.toLowerCase(), ss);
        }
    }

    @Override
    public void scan(final ScannerListener cfl) {
        for (final String p : packages) {
            try {
                final Enumeration<URL> urls = ResourcesProvider.getInstance().getResources(p.replace('.', '/'),
                        classloader);
                while (urls.hasMoreElements()) {
                    try {
                        scan(toURI(urls.nextElement()), cfl);
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException("Error when converting a URL to a URI", ex);
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException("IO error when package scanning jar", ex);
            }
        }
    }

    public static abstract class ResourcesProvider {

        private static volatile ResourcesProvider provider;

        private static ResourcesProvider getInstance() {
            // Double-check idiom for lazy initialization
            ResourcesProvider result = provider;

            if (result == null) { // first check without locking
                synchronized (ResourcesProvider.class) {
                    result = provider;
                    if (result == null) { // second check with locking
                        provider = result = new ResourcesProvider() {
                            @Override
                            public Enumeration<URL> getResources(String name, ClassLoader cl) throws IOException {
                                return cl.getResources(name);
                            }
                        };
                    }
                }

            }
            return result;
        }

        private static void setInstance(ResourcesProvider provider) throws SecurityException {
            SecurityManager security = System.getSecurityManager();
            if (security != null) {
                ReflectPermission rp = new ReflectPermission("suppressAccessChecks");
                security.checkPermission(rp);
            }
            synchronized (ResourcesProvider.class) {
                ResourcesProvider.provider = provider;
            }
        }

        public abstract Enumeration<URL> getResources(String name, ClassLoader cl) throws IOException;
    }

    public static void setResourcesProvider(ResourcesProvider provider) throws SecurityException {
        ResourcesProvider.setInstance(provider);
    }

    private void scan(final URI u, final ScannerListener cfl) {
        final UriSchemeScanner ss = scanners.get(u.getScheme().toLowerCase());
        if (ss != null) {
            ss.scan(u, cfl);
        } else {
            throw new RuntimeException("The URI scheme " + u.getScheme() + " of the URI " + u
                    + " is not supported. Package scanning deployment is not" + " supported for such URIs."
                    + "\nTry using a different deployment mechanism such as"
                    + " explicitly declaring root resource and provider classes"
                    + " using an extension of javax.ws.rs.core.Application");
        }
    }

    private URI toURI(URL url) throws URISyntaxException {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            // Work around bug where some URLs are incorrectly encoded.
            // This can occur when certain class loaders are utilized
            // to obtain URLs for resources.
            return URI.create(toExternalForm(url));
        }
    }

    private String toExternalForm(URL u) {
        // pre-compute length of StringBuffer
        int len = u.getProtocol().length() + 1;
        if (u.getAuthority() != null && u.getAuthority().length() > 0) {
            len += 2 + u.getAuthority().length();
        }
        if (u.getPath() != null) {
            len += u.getPath().length();
        }
        if (u.getQuery() != null) {
            len += 1 + u.getQuery().length();
        }
        if (u.getRef() != null) {
            len += 1 + u.getRef().length();
        }

        StringBuffer result = new StringBuffer(len);
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
