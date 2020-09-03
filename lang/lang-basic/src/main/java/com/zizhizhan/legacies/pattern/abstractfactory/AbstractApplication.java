package com.zizhizhan.legacies.pattern.abstractfactory;

abstract class AbstractApplication {
    protected AbstractDocument doc;
    protected AbstractWorkspace workspace;
    protected AbstractView view;

    public void ConstructObjects(AbstractFactoryClass factory) {
        doc = factory.CreateDocument();
        workspace = factory.CreateWorkspace();
        view = factory.CreateView();
    }

    abstract public void Dump();

    public void DumpState() {
        if (doc != null) doc.Dump();
        if (workspace != null) workspace.Dump();
        if (view != null) view.Dump();
    }
}
