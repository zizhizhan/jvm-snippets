package com.zizhizhan.legacy.pattern.abstractfactory;

class ConcreteFactory extends AbstractFactoryClass {
    @Override
    public AbstractDocument CreateDocument() {
        return new ConcreteDocument();
    }

    @Override
    public AbstractView CreateView() {
        return new ConcreteView();
    }

    @Override
    public AbstractWorkspace CreateWorkspace() {
        return new ConcreteWorkspace();
    }
}
