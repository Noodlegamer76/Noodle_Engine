package com.noodlegamer76.engine.core.network;

import java.util.List;

public interface SyncedVarOwner {
    void markDirty(SyncedVar<?> var);

    List<SyncedVar<?>> getSyncedData();
}

