package net.minecraftforge.registries;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;

public class DeferredRegister<T> {
    private final net.neoforged.neoforge.registries.DeferredRegister<T> delegate;
    private final Map<ResourceLocation, RegistryObject<? extends T>> entries = new LinkedHashMap<>();
    private final Collection<RegistryObject<? extends T>> entriesView = Collections.unmodifiableCollection(entries.values());

    private DeferredRegister(net.neoforged.neoforge.registries.DeferredRegister<T> delegate) {
        this.delegate = delegate;
    }

    public static <T> DeferredRegister<T> create(Registry<T> registry, String namespace) {
        return new DeferredRegister<>(net.neoforged.neoforge.registries.DeferredRegister.create(registry, namespace));
    }

    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> key, String namespace) {
        return new DeferredRegister<>(net.neoforged.neoforge.registries.DeferredRegister.create(key, namespace));
    }

    public static <T> DeferredRegister<T> create(ResourceLocation registryName, String namespace) {
        return new DeferredRegister<>(net.neoforged.neoforge.registries.DeferredRegister.create(registryName, namespace));
    }

    public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
        var holder = delegate.register(name, supplier);
        RegistryObject<I> wrapped = new RegistryObject<>(holder);
        entries.put(wrapped.getId(), wrapped);
        return wrapped;
    }

    public void register(IEventBus eventBus) {
        delegate.register(eventBus);
    }

    public Collection<RegistryObject<? extends T>> getEntries() {
        return entriesView;
    }
}
