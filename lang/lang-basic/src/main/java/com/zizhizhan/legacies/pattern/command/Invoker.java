package com.zizhizhan.legacies.pattern.command;

class Invoker {

    private Command cmd;

    public Invoker(Command cmd) {
        this.cmd = cmd;
    }

    public void executeCommand() {
        cmd.execute();
    }

}
