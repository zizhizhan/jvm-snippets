package com.zizhizhan.legacy.scanner.io;

import java.util.Map;

import com.zizhizhan.legacy.scanner.io.DirectoryFileChangeMonitor.Status;

public interface FileChangeListener {
	
	void itemChanged(Map<String, Status> changeList);

}
