package com.zizhizhan.legacies.pattern.bridge;

class Abstraction {
    protected Implementation impToUse;

    public void SetImplementation(Implementation i) {
        impToUse = i;
    }

    public void DumpString(String str) {
        impToUse.DoStringOp(str);
    }
}
