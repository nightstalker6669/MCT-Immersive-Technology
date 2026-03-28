package mctmods.immersivetechnology.compat.ie.multiblocks;

import mctmods.immersivetechnology.core.util.compat.LazyOptional;
import org.jetbrains.annotations.Nullable;

public final class StoredCapability<T> {
    private final T value;

    public StoredCapability(T value) {
        this.value = value;
    }

    @SuppressWarnings("unused")
    public T get(@Nullable Object ignored) {
        return value;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public <X> LazyOptional<X> cast(@Nullable Object ignored) {
        return LazyOptional.of(() -> (X) value);
    }
}
