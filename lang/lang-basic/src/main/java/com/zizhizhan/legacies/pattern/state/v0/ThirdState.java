package com.zizhizhan.legacies.pattern.state.v0;

class ThirdState implements State {

    StateDoor door;

    public ThirdState(StateDoor door) {
        super();
        this.door = door;
    }

    @Override
    public void coin() {
        door.setState("locked");
    }

    @Override
    public void pass() {
        door.setState("locked");
    }
}
