package com.zizhizhan.legacy.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.zizhizhan.legacy.compress.lzma.Decoder;
import com.zizhizhan.legacy.compress.lzma.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Lzma {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Lzma.class);
	
	private Lzma(){
		
	}
	
	public static void encode(byte[] data, File file) throws IOException {		
		FileOutputStream fos = new FileOutputStream(file);
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		try{
			encode(bais, fos);
		}finally{
			bais.close();
			fos.close();			
		}
	}
	
	public static void encode(InputStream in, OutputStream out) throws IOException{
		Encoder encoder = new Encoder();
		encoder.SetEndMarkerMode(true);
		encoder.WriteCoderProperties(out);
		long fileSize;
		fileSize = -1;
		for (int i = 0; i < 8; i++){
			out.write((int)(fileSize >>> (8 * i)) & 0xFF);
		}
		encoder.Code(in, out, -1, -1, new ICodeProgress() {
			public void SetProgress(long inSize, long outSize) {
				LOGGER.info("lzma progress: (" + inSize + ", " + outSize + ")");			
			}
		});
	}	
	
	public static byte[] decode(File file) throws IOException{		
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try{
			decode(fis, baos);
		}finally{
			baos.close();
			fis.close();
		}
		return baos.toByteArray();
	}
	
	public static void decode(InputStream in, OutputStream out) throws IOException{
		int propertiesSize = 5;
		byte[] properties = new byte[propertiesSize];
		if (in.read(properties, 0, propertiesSize) != propertiesSize){
			throw new IllegalStateException("input .lzma file is too short");
		}
		Decoder decoder = new Decoder();
		if (!decoder.SetDecoderProperties(properties)){
			throw new IllegalStateException("Incorrect stream properties");
		}
		long outSize = 0;
		for (int i = 0; i < 8; i++)
		{
			int v = in.read();
			if (v < 0){
				throw new IllegalStateException("Can't read stream size");
			}
			outSize |= ((long)v) << (8 * i);
		}
		if (!decoder.Code(in, out, outSize)){
			throw new IllegalStateException("Error in data stream");
		}
	}

}
