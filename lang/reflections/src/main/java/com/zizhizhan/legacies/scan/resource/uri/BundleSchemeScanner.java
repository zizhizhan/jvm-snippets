package com.zizhizhan.legacies.scan.resource.uri;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.zizhizhan.legacies.scan.resource.ScannerListener;
import com.zizhizhan.legacies.scan.util.Closing;

public class BundleSchemeScanner implements UriSchemeScanner {

    @Override
    public Set<String> getSchemes() {
        return new HashSet<>(Collections.singletonList("bundle"));
    }

    @Override
    public void scan(final URI u, final ScannerListener sl) {
        if (sl.onAccept(u.getPath())) {
            try {
                new Closing(new BufferedInputStream(u.toURL().openStream()))
                        .f(in -> sl.onProcess(u.getPath(), in));
            } catch (IOException ex) {
                throw new RuntimeException(
                        "IO error when scanning bundle class " + u, ex);
            }
        }
    }
}
