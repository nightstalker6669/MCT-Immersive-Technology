package mctmods.immersivetechnology.client.models;

import mctmods.immersivetechnology.core.lib.ITLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = ITLib.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public record ITDynamicModel(ResourceLocation name) {
    private static final List<ResourceLocation> MODELS = new ArrayList<>();

    @SubscribeEvent public static void registerModels(ModelEvent.RegisterAdditional ev) { for (ResourceLocation model : MODELS) { ev.register(model); } }

    public ITDynamicModel(String desc) { this(ITLib.rl("dynamic/" + desc)); MODELS.add(name); }

    public BakedModel get() {
        final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        return blockRenderer.getBlockModelShaper().getModelManager().getModel(name);
    }

    public ResourceLocation getName() { return name; }
}
