package com.zizhizhan.legacies.scan.resource.uri;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import com.zizhizhan.legacies.scan.resource.ScannerListener;
import com.zizhizhan.legacies.scan.util.Closing;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileSchemeScanner implements UriSchemeScanner {

	public Set<String> getSchemes() {
		return Collections.singleton("file");
	}

	public void scan(final URI u, final ScannerListener cfl) {
		final File f = new File(u.getPath());
		if (f.isDirectory()) {
			scanDirectory(f, cfl);
		} else {
			log.info("IGNORE {}.", f);
		}
	}

	private void scanDirectory(final File root, final ScannerListener cfl) {
		for (final File child : Objects.requireNonNull(root.listFiles())) {
			if (child.isDirectory()) {
				scanDirectory(child, cfl);
			} else if (cfl.onAccept(child.getName())) {
				try {
					new Closing(new BufferedInputStream(new FileInputStream(
							child))).f(new Closing.Closure() {
						public void f(final InputStream in) throws IOException {
							cfl.onProcess(child.getName(), in);
						}
					});
				} catch (IOException ex) {
					throw new RuntimeException("IO error when scanning jar file " + child, ex);
				}
			}
		}
	}

}
