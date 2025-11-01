package com.noodlegamer76.engine.gui.im.interaction;

import java.util.ArrayList;
import java.util.List;

public final class CursorStack {
    private static final List<CursorInfo> STACK = new ArrayList<>();

    private CursorStack() {}

    public static void push(CursorInfo cursor) {
        STACK.add(cursor);
    }

    public static CursorInfo pop() {
        if (STACK.isEmpty()) return null;
        return STACK.removeLast();
    }

    public static CursorInfo peek() {
        if (STACK.isEmpty()) return null;
        return STACK.getLast();
    }

    public static CursorInfo peekParent() {
        int size = STACK.size();
        if (size < 2) return null;
        return STACK.get(size - 2);
    }

    public static void clear() {
        STACK.clear();
    }

    public static boolean isEmpty() {
        return STACK.isEmpty();
    }

    public static int size() {
        return STACK.size();
    }
}
