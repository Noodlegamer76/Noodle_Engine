package com.noodlegamer76.engine.gui.im.widget;

public class ImVar<T> {
    private T value;

    public ImVar(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
