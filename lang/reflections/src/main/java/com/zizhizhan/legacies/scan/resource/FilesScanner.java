package com.zizhizhan.legacies.scan.resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.zizhizhan.legacies.scan.util.Closing;

public class FilesScanner implements Scanner {

    private final File[] files;

    public FilesScanner(final File[] files) {
        this.files = files;
    }

    public void scan(final ScannerListener cfl) {
        for (final File f : files) {
            scan(f, cfl);
        }
    }

    private void scan(final File f, final ScannerListener cfl) {
        if (f.isDirectory()) {
            scanDir(f, cfl);
        } else if (f.getName().endsWith(".jar") || f.getName().endsWith(".zip")) {
            try {
                JarFileScanner.scan(f, "", cfl);
            } catch (IOException ex) {
                throw new RuntimeException("IO error when scanning jar file " + f, ex);
            }
        } else {
            // TODO log
        }
    }

    private void scanDir(final File root, final ScannerListener cfl) {
        for (final File child : Objects.requireNonNull(root.listFiles())) {
            if (child.isDirectory()) {
                scanDir(child, cfl);
            } else if (child.getName().endsWith(".jar")) {
                try {
                    JarFileScanner.scan(child, "", cfl);
                } catch (IOException ex) {
                    throw new RuntimeException(
                            "IO error when scanning jar file " + child, ex);
                }
            } else if (cfl.onAccept(child.getName())) {
                try {
                    new Closing(new BufferedInputStream(new FileInputStream(
                            child))).f(new Closing.Closure() {

                        public void f(InputStream in) throws IOException {
                            cfl.onProcess(child.getName(), in);
                        }
                    });
                } catch (IOException ex) {
                    throw new RuntimeException("IO error when scanning file "
                            + child, ex);
                }
            }
        }
    }

}
