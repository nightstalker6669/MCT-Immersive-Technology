package net.neoforged.neoforge.common.util;

import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public final class LazyOptional<T> {
    private static final LazyOptional<?> EMPTY = new LazyOptional<>(null, false);

    @Nullable
    private Supplier<? extends T> supplier;
    @Nullable
    private T value;
    private boolean initialized;
    private boolean valid;

    private LazyOptional(@Nullable Supplier<? extends T> supplier, boolean valid) {
        this.supplier = supplier;
        this.valid = valid;
    }

    public static <T> LazyOptional<T> of(Supplier<? extends T> supplier) {
        return new LazyOptional<>(supplier, true);
    }

    public static <T> LazyOptional<T> ofNullable(@Nullable T value) {
        if (value == null) {
            return empty();
        }
        LazyOptional<T> result = new LazyOptional<>(null, true);
        result.value = value;
        result.initialized = true;
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> LazyOptional<T> empty() {
        return (LazyOptional<T>) EMPTY;
    }

    @Nullable
    private T getValueInternal() {
        if (!valid) {
            return null;
        }
        if (!initialized) {
            initialized = true;
            if (supplier != null) {
                value = supplier.get();
            }
        }
        return value;
    }

    public boolean isPresent() {
        return getValueInternal() != null;
    }

    public Optional<T> resolve() {
        return Optional.ofNullable(getValueInternal());
    }

    public T orElse(@Nullable T other) {
        T current = getValueInternal();
        return current != null ? current : other;
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        T current = getValueInternal();
        if (current != null) {
            return current;
        }
        throw exceptionSupplier.get();
    }

    @SuppressWarnings("unchecked")
    public <R> LazyOptional<R> cast() {
        if (!valid) {
            return empty();
        }
        return LazyOptional.of(() -> (R) getValueInternal());
    }

    public void invalidate() {
        valid = false;
        value = null;
        supplier = null;
        initialized = true;
    }
}
