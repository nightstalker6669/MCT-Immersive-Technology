package mctmods.immersivetechnology.common.multiblocks.metal.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import mctmods.immersivetechnology.common.multiblocks.metal.logic.BoilerLiquidLogic;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.BoilerLiquidRecipe;
import mctmods.immersivetechnology.core.util.codec.ITRecipeCodecs;
import mctmods.immersivetechnology.core.registration.ITMultiblockProvider;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class BoilerLiquidRecipeSerializer extends IERecipeSerializer<BoilerLiquidRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, BoilerLiquidRecipe> CODECS = DualCompositeMapCodecs.composite(
            DualCodecs.RESOURCE_LOCATION.optionalFieldOf("id", ITRecipeCodecs.UNBOUND_ID),
            BoilerLiquidRecipe::id,
            ITRecipeCodecs.FLUID_TAG_INPUT.codec().fieldOf("input"),
            recipe -> recipe.input,
            DualCodecs.INT.fieldOf("time"),
            BoilerLiquidRecipe::getTotalProcessTime,
            DualCodecs.DOUBLE.fieldOf("heatPerTick"),
            BoilerLiquidRecipe::getHeatPerTick,
            DualCodecs.DOUBLE.optionalFieldOf("targetHeat", BoilerLiquidLogic.DEFAULT_WORKING_HEAT_LEVEL),
            BoilerLiquidRecipe::getTargetHeat,
            BoilerLiquidRecipe::new
    );

    @Override public ItemStack getIcon() { return ITMultiblockProvider.BOILER_LIQUID.iconStack(); }

    @Override protected DualMapCodec<RegistryFriendlyByteBuf, BoilerLiquidRecipe> codecs() { return CODECS; }
}
