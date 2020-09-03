package com.zizhizhan.legacies.pattern.abstractfactory;

class ConcreteDocument extends AbstractDocument {
    @Override
    public void Dump() {
        System.out.println("Document Exists");
    }
}
