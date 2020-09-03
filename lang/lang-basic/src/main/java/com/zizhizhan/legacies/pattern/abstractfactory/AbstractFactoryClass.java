package com.zizhizhan.legacies.pattern.abstractfactory;

abstract class AbstractFactoryClass {
    abstract public AbstractDocument CreateDocument();

    abstract public AbstractView CreateView();

    abstract public AbstractWorkspace CreateWorkspace();
}
