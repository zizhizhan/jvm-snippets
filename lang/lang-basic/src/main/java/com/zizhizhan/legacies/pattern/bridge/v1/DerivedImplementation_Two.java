package com.zizhizhan.legacies.pattern.bridge.v1;

class DerivedImplementation_Two extends Implementation {
    @Override
    public void DoStringOp(String str) {
        System.out.println("DerivedImplementation_Two - print string twice");
        System.out.println("string = " + str);
        System.out.println("string = " + str);
    }
}
