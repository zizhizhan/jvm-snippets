package com.zizhizhan.legacies.scan;

import com.zizhizhan.legacies.scan.resource.PackageNamesScanner;
import com.zizhizhan.legacies.scan.resource.ScannerListener;

import java.io.InputStream;

public class ScanningTests {

    public static void main(String[] args) {
        PackageNamesScanner scanner = new PackageNamesScanner("com.zizhizhan");
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
