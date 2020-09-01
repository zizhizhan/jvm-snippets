package com.zizhizhan.legacy.pattern.bridge;

class Implementation {
    public void DoStringOp(String str) {
        System.out.println("Standard implementation - print string as is");
        System.out.println("string = " + str);
    }
}
