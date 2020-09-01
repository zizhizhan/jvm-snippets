package com.zizhizhan.legacy.pattern.filterchain.mina;

public interface WriteRequest {

	WriteFuture getFuture();

	boolean isEncoded();

	Object getMessage();

}
