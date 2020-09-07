package com.zizhizhan.legacies.scan.resource.uri;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.zizhizhan.legacies.scan.resource.JarFileScanner;
import com.zizhizhan.legacies.scan.resource.ScannerListener;

public class VfsSchemeScanner implements UriSchemeScanner {

    public Set<String> getSchemes() {
        return new HashSet<String>(Arrays.asList("vfsfile", "vfszip"));
    }

    public void scan(final URI u, final ScannerListener sl) {
        if (u.getScheme().equalsIgnoreCase("vfsfile")) {
            new FileSchemeScanner().scan(u, sl);
        } else {
            final String su = u.toString();
            final int webInfIndex = su.indexOf("/WEB-INF/classes");
            if (webInfIndex != -1) {
                final String war = su.substring(0, webInfIndex);
                final String path = su.substring(webInfIndex + 1);

                final int warParentIndex = war.lastIndexOf('/');
                final String warParent = su.substring(0, warParentIndex);

                // Check is there is a war within an ear If so we need to load the ear then obtain the InputStream of the entry to the war
                if (warParent.endsWith(".ear")) {
                    final String warName = su.substring(warParentIndex + 1, war.length());
                    try {
                        JarFileScanner.scan(new URL(warParent.replace("vfszip", "file")).openStream(), "",
                                new ScannerListener() {
                                    public boolean onAccept(String name) {
                                        return name.equals(warName);
                                    }

                                    public void onProcess(String name, InputStream in) throws IOException {
                                        // This is required so that the underlying ear is not closed
                                        in = new FilterInputStream(in) {
                                            public void close() throws IOException { }
                                        };
                                        try {
                                            JarFileScanner.scan(in, path, sl);
                                        } catch (IOException ex) {
                                            throw new RuntimeException("IO error when scanning war " + u, ex);
                                        }
                                    }
                                });
                    } catch (IOException ex) {
                        throw new RuntimeException("IO error when scanning war " + u, ex);
                    }
                } else {
                    try {
                        JarFileScanner.scan(new URL(war.replace("vfszip", "file")).openStream(), path, sl);
                    } catch (IOException ex) {
                        throw new RuntimeException("IO error when scanning war " + u, ex);
                    }
                }
            } else {
                try {
                    JarFileScanner.scan(new URL(su).openStream(), "", sl);
                } catch (IOException ex) {
                    throw new RuntimeException("IO error when scanning jar " + u, ex);
                }
            }
        }
    }
}
