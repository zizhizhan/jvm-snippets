package com.zizhizhan.legacies.crypto;

public class MD5 {

	/*
	 * 如果X、Y和Z的对应位是独立和均匀的，那么结果的每一位也应是独立和均匀的。
	 * F是一个逐位运算的函数。即，如果X，那么Y，否则Z。函数H是逐位奇偶操作符。
	 */

	public int f(int x, int y, int z) {
		return (x & y) | (~x & z);
	}

	public int g(int x, int y, int z) {
		return (x & z) | (y & ~z);
	}

	public int h(int x, int y, int z) {
		return x ^ y ^ z;
	}

	public int i(int x, int y, int z) {
		return y ^ (x | ~z);
	}

}
