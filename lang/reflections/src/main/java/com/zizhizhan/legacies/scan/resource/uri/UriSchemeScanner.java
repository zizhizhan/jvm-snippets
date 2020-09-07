package com.zizhizhan.legacies.scan.resource.uri;

import java.net.URI;
import java.util.Set;

import com.zizhizhan.legacies.scan.resource.ScannerListener;

public interface UriSchemeScanner {

    Set<String> getSchemes();

    void scan(URI u, ScannerListener sl);

}
