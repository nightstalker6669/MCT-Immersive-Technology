package mctmods.immersivetechnology;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import mctmods.immersivetechnology.common.multiblocks.helper.ITQueueProcessor;
import mctmods.immersivetechnology.common.multiblocks.helper.ITTemplateMultiblock;
import mctmods.immersivetechnology.core.util.loot.ITLootFunctions;
import mctmods.immersivetechnology.core.ITClientConfig;
import mctmods.immersivetechnology.core.ITCommonConfig;
import mctmods.immersivetechnology.core.ITServerConfig;
import mctmods.immersivetechnology.core.lib.ITLib;
import mctmods.immersivetechnology.core.proxy.ClientProxySupplier;
import mctmods.immersivetechnology.core.proxy.CommonProxy;
import mctmods.immersivetechnology.core.registration.ITFluids;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import static mctmods.immersivetechnology.common.fluids.ITFluid.BUCKET_DISPENSE_BEHAVIOR;
import static mctmods.immersivetechnology.core.lib.ITLib.MODID;

@SuppressWarnings("unused")
@Mod(MODID)
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
public class ImmersiveTechnology {
    public static CommonProxy proxy;

    public ImmersiveTechnology(IEventBus modEventBus, ModContainer container, Dist dist) {
        proxy = dist == Dist.CLIENT ? ClientProxySupplier.get() : new CommonProxy();
        ITLib.IT_LOGGER.info("IT Starting");
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);
        ITLib.IT_LOGGER.info("Starting Proxy Mod Construction");
        CommonProxy.modConstruction(modEventBus);
        ITLootFunctions.init(modEventBus);
        container.registerConfig(ModConfig.Type.COMMON, ITCommonConfig.SPEC);
        container.registerConfig(ModConfig.Type.SERVER, ITServerConfig.SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, ITClientConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ITLib.IT_LOGGER.info("HELLO FROM COMMON SETUP");
        for (ITFluids.FluidEntry entry : ITFluids.ALL_ENTRIES) {
            DispenserBlock.registerBehavior(entry.getBucket(), BUCKET_DISPENSE_BEHAVIOR);
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", () -> (Function<Object, Void>) top -> {
                invokeTopRegistration(top);
                return null;
            });
        }
    }

    private static void invokeTopRegistration(Object top) {
        try {
            Class<?> helperClass = Class.forName("mctmods.immersivetechnology.core.integration.top.OneProbeHelper");
            helperClass.getMethod("register", Object.class).invoke(null, top);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Failed to register The One Probe integration", exception);
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        List<ITQueueProcessor> copy = new ArrayList<>(ITTemplateMultiblock.pendingQueues);
        copy.forEach(ITQueueProcessor::tick);
        ITTemplateMultiblock.pendingQueues.removeIf(ITQueueProcessor::isEmpty);
    }

    @SubscribeEvent public static void onServerStarting(ServerStartingEvent event) {
        ITLib.IT_LOGGER.info("HELLO FROM SERVER STARTING");
    }
}
