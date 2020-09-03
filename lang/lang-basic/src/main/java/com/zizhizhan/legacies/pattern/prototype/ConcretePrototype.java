package com.zizhizhan.legacies.pattern.prototype;

public class ConcretePrototype extends AbstractPrototype{
    @Override
    public AbstractPrototype CloneYourself() throws CloneNotSupportedException {
        return ((AbstractPrototype)clone());
    }
}