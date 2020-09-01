package com.zizhizhan.legacy.scanner.io;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class DefaultFileChangeListener implements FileChangeListener{
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void itemChanged(Map<String, DirectoryFileChangeMonitor.Status> changeList) {
		/*for(String fileId : changeList.keySet()){
			logger.info(String.format("%s: %s", fileId, changeList.get(fileId)));
		}*/
		logger.info("\n\n\n");
	}
}
