package mctmods.immersivetechnology.core.registration;

import com.immersiveconvergence.api.HeatCapabilities;
import com.immersiveconvergence.api.MechanicalCapabilities;
import mctmods.immersivetechnology.core.lib.ITLib;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = ITLib.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ITCapabilities {
    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ITBlockEntities.TRASH_FLUID.get(), (be, side) -> be);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ITBlockEntities.TRASH_ENERGY.get(), (be, side) -> be);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ITBlockEntities.TRASH_ITEM.get(), (be, side) -> be);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ITBlockEntities.CRATE_CREATIVE.get(), (be, side) -> be);
        event.registerBlockEntity(HeatCapabilities.HEAT_PROVIDER_CAPABILITY, ITBlockEntities.HEAT_CREATIVE.get(), (be, side) -> be.getHeatProvider(side));
        event.registerBlockEntity(MechanicalCapabilities.MECHANICAL_PROVIDER_CAPABILITY, ITBlockEntities.ROTOR_CREATIVE.get(), (be, side) -> be.getMechanicalProvider(side));
    }
}
