package com.zizhizhan.legacies.crypto.internal;

import java.util.List;

public interface Encryption {

	String encrypt(List<Byte> inputBytes);

	List<Byte> decrypt(String input);

}
