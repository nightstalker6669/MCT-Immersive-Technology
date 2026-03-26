package net.minecraftforge.registries;

import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class RegistryObject<T> implements Supplier<T> {
    @SuppressWarnings("rawtypes")
    private final DeferredHolder holder;

    @SuppressWarnings("rawtypes")
    RegistryObject(DeferredHolder holder) {
        this.holder = Objects.requireNonNull(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        return (T)holder.get();
    }

    public ResourceLocation getId() {
        return holder.getId();
    }

    public boolean isPresent() {
        return holder.isBound();
    }
}
