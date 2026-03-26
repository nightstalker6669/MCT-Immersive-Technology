package net.minecraftforge.common.capabilities;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;

public final class ForgeCapabilities {
    public static final Capability<IEnergyStorage> ENERGY = Capability.ofBlock(Capabilities.EnergyStorage.BLOCK);
    public static final Capability<IFluidHandler> FLUID_HANDLER = Capability.ofBlock(Capabilities.FluidHandler.BLOCK);
    public static final Capability<IItemHandler> ITEM_HANDLER = Capability.ofBlock(Capabilities.ItemHandler.BLOCK);
    public static final Capability<IFluidHandlerItem> FLUID_HANDLER_ITEM = Capability.ofItem(Capabilities.FluidHandler.ITEM);

    private ForgeCapabilities() {}
}
