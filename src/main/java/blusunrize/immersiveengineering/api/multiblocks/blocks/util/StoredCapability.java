package blusunrize.immersiveengineering.api.multiblocks.blocks.util;

import net.neoforged.neoforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public final class StoredCapability<T> {
    private final T value;

    public StoredCapability(T value) {
        this.value = value;
    }

    public T get(@Nullable Object ignored) {
        return value;
    }

    public <X> LazyOptional<X> cast(@Nullable Object ignored) {
        return LazyOptional.of(() -> (X) value);
    }
}
