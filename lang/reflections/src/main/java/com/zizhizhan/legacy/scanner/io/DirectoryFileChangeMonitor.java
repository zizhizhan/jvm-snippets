package com.zizhizhan.legacy.scanner.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.zizhizhan.legacy.scanner.Notifier;
import com.zizhizhan.legacy.scanner.util.CollectionUtils;
import com.zizhizhan.legacy.scanner.util.DateUtils;

public class DirectoryFileChangeMonitor {

	public enum Status {
		ADD, UPDATED, DELETED
	}

	protected static final Logger LOGGER = LoggerFactory.getLogger(DirectoryFileChangeMonitor.class);
	protected ConcurrentMap<String, Long> fileTimestampMap = new ConcurrentHashMap<String, Long>();
	protected ResourceLoader resourceLoader = new DefaultResourceLoader();
	protected List<String> contentRoots;
	protected Pattern fileMaskPattern;
	protected List<File> baseDirs;
	protected FileFilter directoryFilter;
	protected FileFilter fileFilter;
	protected List<FileChangeListener> listeners;


	public void start() throws IOException {
		directoryFilter = new FileFilter() {
			public boolean accept(File dir) {
				return dir.isDirectory();
			}
		};

		fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isFile() && (fileMaskPattern == null || fileMaskPattern.matcher(file.getName()).matches());
			}
		};

		baseDirs = new ArrayList<File>();

		if (contentRoots != null) {
			for (String contentRoot : contentRoots) {
				try {
					contentRoot = contentRoot.trim();
					File baseDir = resourceLoader.getResource(contentRoot).getFile();
					if (!baseDir.isDirectory()) {
						throw new FileNotFoundException("'" + contentRoot + "' is not a directory.");
					} else {
						LOGGER.info("Add directory: " + baseDir);						
						baseDirs.add(baseDir);
					}
				} catch (FileNotFoundException ex) {
					LOGGER.error(contentRoot + " is not a valid directory.  Can't listen for changes.", ex);
				}
			}
		}	
	}

	public void execute() {
		if (baseDirs != null) {
			try {
				Map<String, Status> changeList = new HashMap<String, Status>();
				for (File baseDir : baseDirs) {
					changeList.putAll(findChangedFiles(baseDir));
				}						
				if(!changeList.isEmpty()){
					sendChangeEvent(changeList);
				}
			} catch (Throwable t) {
				LOGGER.error("DirectoryFileChangeMonitor failed to execute", t);
			}
		}
	}

	protected void sendChangeEvent(final Map<String, Status> changeList) {
		new Notifier<FileChangeListener>(listeners) {
			protected void notify(FileChangeListener each) {
				each.itemChanged(changeList);				
			}
		}.run();
	}

	protected Map<String, Status> findChangedFiles(File baseDir) {
		Map<String, Status> changeList = new HashMap<String, Status>();
		Collection<String> checkedFiles = new HashSet<String>();
		findChangedAndNewFiles(baseDir, "", changeList, checkedFiles);
		Collection<String> deletedFiles = CollectionUtils.diff(fileTimestampMap.keySet(), checkedFiles);
		for (String fileId : deletedFiles) {
			fileTimestampMap.remove(fileId);
			changeList.put(fileId, Status.DELETED);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Deleted " + fileId);
			}
		}
		return changeList;
	}

	private void findChangedAndNewFiles(File baseDir, String path, Map<String, Status> changeList,
			Collection<String> checkedFiles) {

		File[] files = baseDir.listFiles(fileFilter);
		path += "/";

		// scan the files and check for modification/addition
		for (File file : files) {
			String fileId = path + file.getName();
			checkedFiles.add(fileId);
			Long current = fileTimestampMap.get(fileId);
			long lastModified = file.lastModified();
			if (current == null) {
				// new file				
				fileTimestampMap.put(fileId, lastModified);
				changeList.put(fileId, Status.ADD);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Add " + fileId + "-" + DateUtils.format(lastModified));
				}
			} else if (current != file.lastModified()) {
				// modified file
				fileTimestampMap.put(fileId, lastModified);
				changeList.put(fileId, Status.UPDATED);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Updated " + fileId + "-" + DateUtils.format(lastModified));
				}
			}
		}

		File[] subdirs = baseDir.listFiles(directoryFilter);
		for (File subdir : subdirs) {
			findChangedAndNewFiles(subdir, path + subdir.getName(), changeList, checkedFiles);
		}
	}

	public void setFileMask(String fileMask) {
		if (fileMask != null) {
			fileMaskPattern = Pattern.compile(fileMask);
		}
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setContentRoots(String... contentRoots) {
		this.contentRoots = Arrays.asList(contentRoots);
	}

	public void setListeners(FileChangeListener... listeners) {
		this.listeners = Arrays.asList(listeners);
	}

	
/*	
 	private Map<String, Status> findChangedAndNewFiles(File baseDir, String path) {
		Map<String, Status> changeList = new HashMap<String, DirectoryFileChangeMonitor.Status>();
		File[] files = baseDir.listFiles(fileFilter);
		if (path.length() > 0) {
			path += '/';
		}

		// scan the files and check for modification/addition
		for (File file : files) {
			String fileId = path + file.getName();
			Long current = fileTimestampMap.get(fileId);
			if (current == null) { // new file
				fileTimestampMap.put(fileId, file.lastModified());
				changeList.put(fileId, Status.ADD);
			} else if (current != file.lastModified()) { // modified file
				fileTimestampMap.put(fileId, file.lastModified());
				changeList.put(fileId, Status.UPDATED);
			}
		}

		File[] subdirs = baseDir.listFiles(directoryFilter);
		for (File subdir : subdirs) {
			changeList.putAll(findChangedAndNewFiles(subdir, path + subdir.getName()));
		}
		return changeList;
	}
*/
	 

}
