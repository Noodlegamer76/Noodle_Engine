package com.noodlegamer76.engine.core.network;

public class SyncedVar<T> {
    private T value;
    private final GameObjectSerializer<T> serializer;
    private final SyncedVarOwner owner;

    public SyncedVar(SyncedVarOwner owner, T value, GameObjectSerializer<T> serializer) {
        this.serializer = serializer;
        this.owner = owner;
        this.value = value;
    }

    public void setValue(T value, boolean markDirty) {
        this.value = value;
        if (markDirty) {
            owner.markDirty(this);
        }
    }

    public T getValue() {
        return value;
    }

    public GameObjectSerializer<T> getSerializer() {
        return serializer;
    }
}
