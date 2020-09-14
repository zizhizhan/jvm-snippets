package com.zizhizhan.legacies.pattern.bridge.v1;

class Abstraction {
    protected Implementation impToUse;

    public void SetImplementation(Implementation i) {
        impToUse = i;
    }

    public void DumpString(String str) {
        impToUse.DoStringOp(str);
    }
}
