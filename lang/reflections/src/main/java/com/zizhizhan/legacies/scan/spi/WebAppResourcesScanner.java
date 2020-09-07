package com.zizhizhan.legacies.scan.spi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.servlet.ServletContext;

import com.zizhizhan.legacies.scan.resource.JarFileScanner;
import com.zizhizhan.legacies.scan.resource.ScannerListener;
import com.zizhizhan.legacies.scan.util.Closing;

public class WebAppResourcesScanner {
	
	private final String[] paths;
    private final ServletContext sc;

    public WebAppResourcesScanner(final String[] paths, final ServletContext sc) {
        this.paths = paths;
        this.sc = sc;
    }

    public void scan(final ScannerListener cfl) {
        for (final String path : paths) {
            scan(path, cfl);
        }
    }

    private void scan(final String root, final ScannerListener cfl) {
        @SuppressWarnings("unchecked")
		final Set<String> resourcePaths = sc.getResourcePaths(root);
        if(resourcePaths == null)
            return;
        for (final String resourcePath : resourcePaths) {
            if (resourcePath.endsWith("/")) {
                scan(resourcePath, cfl);
            } else if (resourcePath.endsWith(".jar")) {
                try {
                    new Closing(sc.getResourceAsStream(resourcePath)).f(new Closing.Closure() {
                        public void f(final InputStream in) throws IOException {
                            JarFileScanner.scan(in, "", cfl);
                        }
                    });
                } catch (IOException ex) {
                    throw new RuntimeException("IO error scanning jar " + resourcePath, ex);
                }
            } else if (cfl.onAccept(resourcePath)) {
                try {
                    new Closing(sc.getResourceAsStream(resourcePath)).f(new Closing.Closure() {
                        public void f(final InputStream in) throws IOException {
                            cfl.onProcess(resourcePath, in);
                        }
                    });
                } catch (IOException ex) {
                    throw new RuntimeException("IO error scanning resource " + resourcePath, ex);
                }
            }
        }
    }

}
