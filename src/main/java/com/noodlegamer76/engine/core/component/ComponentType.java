package com.noodlegamer76.engine.core.component;


import com.noodlegamer76.engine.entity.GameObject;

import java.util.function.Supplier;

public class ComponentType<T extends Component> {
    private final InitComponents.ComponentSupplier<T> supplier;

    public ComponentType(InitComponents.ComponentSupplier<T> supplier) {
        this.supplier = supplier;
    }

    public Component create(GameObject gameObject) {
        return supplier.create(gameObject);
    }
}

