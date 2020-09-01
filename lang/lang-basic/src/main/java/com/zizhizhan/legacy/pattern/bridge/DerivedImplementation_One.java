package com.zizhizhan.legacy.pattern.bridge;

class DerivedImplementation_One extends Implementation {
    @Override
    public void DoStringOp(String str) {
        System.out.println("DerivedImplementation_One - don't print string");
    }
}
