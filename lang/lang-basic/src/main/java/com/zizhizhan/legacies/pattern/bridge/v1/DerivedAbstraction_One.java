package com.zizhizhan.legacies.pattern.bridge.v1;

class DerivedAbstraction_One extends Abstraction {
    @Override
    public void DumpString(String str) {
        str += ".com";
        impToUse.DoStringOp(str);
    }
}
