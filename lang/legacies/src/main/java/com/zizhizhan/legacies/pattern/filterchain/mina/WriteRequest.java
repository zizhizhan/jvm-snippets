package com.zizhizhan.legacies.pattern.filterchain.mina;

public interface WriteRequest {

	WriteFuture getFuture();

	boolean isEncoded();

	Object getMessage();

}
