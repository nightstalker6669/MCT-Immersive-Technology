package mctmods.immersivetechnology.common.fluids.helper;

import mctmods.immersivetechnology.core.util.ITUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.function.Consumer;

public record ITSolarTank(ITMarkableFluidTank input, ITMarkableFluidTank output) {
    public static final int TANK_CAPACITY = 12000;

    public ITSolarTank(Consumer<Void> markDirty) { this(new ITMarkableFluidTank(TANK_CAPACITY, markDirty), new ITMarkableFluidTank(TANK_CAPACITY, markDirty)); }

    public static ITSolarTank makeClient() { return new ITSolarTank(v -> {}); }

    public Tag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("input", this.input.writeToNBT(ITUtils.serverRegistryAccess(), new CompoundTag()));
        tag.put("output", this.output.writeToNBT(ITUtils.serverRegistryAccess(), new CompoundTag()));
        return tag;
    }

    public void readNBT(CompoundTag tag) {
        this.input.readFromNBT(ITUtils.serverRegistryAccess(), tag.getCompound("input"));
        this.output.readFromNBT(ITUtils.serverRegistryAccess(), tag.getCompound("output"));
    }

    public int getCapacity() { return TANK_CAPACITY; }
}
