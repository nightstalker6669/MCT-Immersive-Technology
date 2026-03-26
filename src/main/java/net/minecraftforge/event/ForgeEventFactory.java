package net.minecraftforge.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;

public final class ForgeEventFactory {
    private ForgeEventFactory() {}

    public static boolean canCreateFluidSource(Level level, BlockPos pos, BlockState state, boolean canConvertToSource) {
        return canConvertToSource && EventHooks.canCreateFluidSource(level, pos, state);
    }
}
