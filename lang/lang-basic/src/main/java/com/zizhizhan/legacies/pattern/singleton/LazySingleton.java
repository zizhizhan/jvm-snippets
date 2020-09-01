package com.zizhizhan.legacies.pattern.singleton;

public class LazySingleton {

    static {
        System.out.println("LazySingleton static initialize.");
    }

    private LazySingleton() {
        System.out.println("instance initialize.");
    }

    static class SingletonHolder {
        final static LazySingleton s_singleton = new LazySingleton();

        static {
            System.out.println("Holder static initialize.");
        }
    }

    public static LazySingleton getSingleton() {
        return SingletonHolder.s_singleton;
    }

    public static void main(String[] args) {

        new LazySingleton();
        new LazySingleton();
        new LazySingleton();

        for (int i = 0; i < 10; i++) {
			LazySingleton.getSingleton();
		}
    }

}
