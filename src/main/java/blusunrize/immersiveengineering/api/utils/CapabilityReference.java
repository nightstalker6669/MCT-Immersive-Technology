package blusunrize.immersiveengineering.api.utils;

import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;

public final class CapabilityReference<T> implements Supplier<T> {
    private final Supplier<T> supplier;

    private CapabilityReference(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> CapabilityReference<T> of(Supplier<T> supplier) {
        return new CapabilityReference<>(supplier);
    }

    public static <T> CapabilityReference<T> forNeighbor(BlockEntity blockEntity, Capability<T> capability, Direction direction) {
        return new CapabilityReference<>(() -> capability.get(blockEntity.getLevel(), blockEntity.getBlockPos().relative(direction), direction.getOpposite()).orElse(null));
    }

    public boolean isPresent() {
        return getNullable() != null;
    }

    @Nullable
    public T getNullable() {
        return supplier.get();
    }

    @Override
    public T get() {
        return supplier.get();
    }
}
