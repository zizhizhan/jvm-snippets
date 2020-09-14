package com.zizhizhan.legacies.pattern.state.v0;

class LockState implements State {
    StateDoor door;

    public LockState(StateDoor door) {
        super();
        this.door = door;
    }

    @Override
    public void coin() {
        door.open();
        door.setState("unlocked");
    }

    @Override
    public void pass() {
        door.alarm();
    }

}
