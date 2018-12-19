package com.serg.cellauto.enumeration;

public enum State {
    FULL(1),
    EMPTY(0);

    private int value;

    State(int value) {
        this.value = value;
    }

    public int getState() {
        return value;
    }
}
