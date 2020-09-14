package com.zizhizhan.legacies.pattern.state.v0;

/**
 * event / action / nextstate
 * <p>
 * lock
 * coin / open / unlock
 * pass / alarm / -
 * <p>
 * unlock
 * coin / thanks / -
 * pass / close / locked
 */

class UnlockState implements State {

    StateDoor door;

    public UnlockState(StateDoor door) {
        super();
        this.door = door;
    }

    @Override
    public void coin() {
        door.thanks();
    }

    @Override
    public void pass() {
        door.close();
        door.setState("locked");
    }
}
