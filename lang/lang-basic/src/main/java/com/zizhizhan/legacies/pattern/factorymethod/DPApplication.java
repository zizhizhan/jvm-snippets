package com.zizhizhan.legacies.pattern.factorymethod;

abstract class DPApplication {
    protected DPDocument doc;

    abstract public void CreateDocument();

    public void ConstructObjects() {
        System.out.println("Create DPDocument");
        CreateDocument();
    }

    abstract public void Dump();
}
