package com.zizhizhan.legacies.compress;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressorDemo {

	public static void main(String[] args) throws UnsupportedEncodingException, DataFormatException {
		String test = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz."
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz."
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz."
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz."
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz."
				+ "abc,ABC,abc,abc,abc,ABC,AAA,bcdefghijklmnopqrstuvwxyz";
		byte[] input = test.getBytes("iso8859-1");
		System.out.println(input.length);
		byte[] output = compress(input);
		System.out.println(output.length);
		
		byte[] bytes = decompress(output);
		System.out.println(bytes.length);
	}

	public static byte[] compress(byte[] input) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

		Deflater compressor = new Deflater();

		try {
			compressor.setLevel(Deflater.BEST_COMPRESSION);
			compressor.setInput(input, 0, input.length);
			compressor.finish();

			final byte[] buf = new byte[1024];
			while (!compressor.finished()) {
				int count = compressor.deflate(buf);
				bos.write(buf, 0, count);
			}
		} finally {
			compressor.end();
		}

		return bos.toByteArray();
	}

	public static byte[] decompress(byte[] input) throws DataFormatException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

		Inflater decompressor = new Inflater();

		try {
			decompressor.setInput(input);

			final byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				int count = decompressor.inflate(buf);
				bos.write(buf, 0, count);
			}
		} finally {
			decompressor.end();
		}

		return bos.toByteArray();
	}

}
