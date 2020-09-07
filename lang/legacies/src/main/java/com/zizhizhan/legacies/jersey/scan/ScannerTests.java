package com.zizhizhan.legacies.jersey.scan;

import java.io.InputStream;

import com.sun.jersey.core.spi.scanning.PackageNamesScanner;
import com.sun.jersey.core.spi.scanning.ScannerListener;

public class ScannerTests {

    public static void main(String[] args) {
        PackageNamesScanner scanner = new PackageNamesScanner(new String[]{""});
        scanner.scan(new ScannerListener() {
            @Override
            public void onProcess(String name, InputStream in) {
                System.out.println(name);
            }

            @Override
            public boolean onAccept(String name) {
                System.out.println(name);
                return false;
            }
        });
    }

}
