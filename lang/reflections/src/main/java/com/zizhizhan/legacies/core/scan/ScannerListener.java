package com.zizhizhan.legacies.core.scan;

import java.io.IOException;
import java.io.InputStream;

public interface ScannerListener {
	
	boolean onAccept(String name);
	
    void onProcess(String name, InputStream in) throws IOException;

}
