package com.zizhizhan.legacies.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.Writer;

@Slf4j
public class CommonsLogWriter extends Writer {

	private final StringBuffer buffer = new StringBuffer();


	public void write(char ch) {
		if (ch == '\n' && this.buffer.length() > 0) {
			log.debug(this.buffer.toString());
			this.buffer.setLength(0);
		}
		else {
			this.buffer.append((char) ch);
		}
	}

	public void write(char[] buffer, int offset, int length) {
		for (int i = 0; i < length; i++) {
			char ch = buffer[offset + i];
			if (ch == '\n' && this.buffer.length() > 0) {
				log.debug(this.buffer.toString());
				this.buffer.setLength(0);
			}
			else {
				this.buffer.append((char) ch);
			}
		}
	}

	public void flush() {
	}

	public void close() {
	}

}
