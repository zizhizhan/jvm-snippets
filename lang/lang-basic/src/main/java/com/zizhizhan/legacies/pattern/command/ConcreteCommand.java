package com.zizhizhan.legacies.pattern.command;

class ConcreteCommand implements Command {

    private Receiver receiver;

    public void execute() {
        receiver.action();
    }

    public ConcreteCommand(Receiver receiver) {
        this.receiver = receiver;
    }


}
