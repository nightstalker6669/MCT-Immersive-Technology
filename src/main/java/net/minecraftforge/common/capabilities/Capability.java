package net.minecraftforge.common.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public final class Capability<T> {
    @Nullable
    private final BlockCapability<T, @Nullable Direction> blockCapability;
    @Nullable
    private final ItemCapability<? extends T, @Nullable Void> itemCapability;

    private Capability(@Nullable BlockCapability<T, @Nullable Direction> blockCapability, @Nullable ItemCapability<? extends T, @Nullable Void> itemCapability) {
        this.blockCapability = blockCapability;
        this.itemCapability = itemCapability;
    }

    public static <T> Capability<T> ofBlock(BlockCapability<T, @Nullable Direction> blockCapability) {
        return new Capability<>(blockCapability, null);
    }

    public static <T> Capability<T> ofItem(ItemCapability<? extends T, @Nullable Void> itemCapability) {
        return new Capability<>(null, itemCapability);
    }

    @Nullable
    public BlockCapability<T, @Nullable Direction> block() {
        return blockCapability;
    }

    public LazyOptional<T> get(@Nullable BlockEntity blockEntity, @Nullable Direction side) {
        if (blockEntity == null || blockCapability == null) {
            return LazyOptional.empty();
        }
        Level level = blockEntity.getLevel();
        if (level == null) {
            return LazyOptional.empty();
        }
        return get(level, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, side);
    }

    public LazyOptional<T> get(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity, @Nullable Direction side) {
        if (blockCapability == null) {
            return LazyOptional.empty();
        }
        return LazyOptional.ofNullable(level.getCapability(blockCapability, pos, state, blockEntity, side));
    }

    public LazyOptional<T> get(Level level, BlockPos pos, @Nullable Direction side) {
        if (blockCapability == null) {
            return LazyOptional.empty();
        }
        return LazyOptional.ofNullable(level.getCapability(blockCapability, pos, side));
    }

    public LazyOptional<T> get(ItemStack stack) {
        if (itemCapability == null) {
            return LazyOptional.empty();
        }
        return LazyOptional.ofNullable(stack.getCapability(itemCapability));
    }
}
