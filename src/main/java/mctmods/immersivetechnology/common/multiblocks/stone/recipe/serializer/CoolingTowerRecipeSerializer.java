package mctmods.immersivetechnology.common.multiblocks.stone.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import mctmods.immersivetechnology.common.multiblocks.stone.recipe.CoolingTowerRecipe;
import mctmods.immersivetechnology.core.util.codec.ITRecipeCodecs;
import mctmods.immersivetechnology.core.registration.ITMultiblockProvider;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class CoolingTowerRecipeSerializer extends IERecipeSerializer<CoolingTowerRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, CoolingTowerRecipe> CODECS = DualCompositeMapCodecs.composite(
            DualCodecs.RESOURCE_LOCATION.optionalFieldOf("id", ITRecipeCodecs.UNBOUND_ID),
            CoolingTowerRecipe::id,
            ITRecipeCodecs.optionalFluidStack("output0"),
            recipe -> recipe.fluidOutput0,
            ITRecipeCodecs.optionalFluidStack("output1"),
            recipe -> recipe.fluidOutput1,
            ITRecipeCodecs.optionalFluidStack("output2"),
            recipe -> recipe.fluidOutput2,
            ITRecipeCodecs.FLUID_TAG_INPUT.codec().fieldOf("input0"),
            recipe -> recipe.input0,
            ITRecipeCodecs.FLUID_TAG_INPUT.codec().fieldOf("input1"),
            recipe -> recipe.input1,
            DualCodecs.INT.fieldOf("time"),
            recipe -> recipe.totalProcessTime,
            CoolingTowerRecipe::new
    );

    @Override public net.minecraft.world.item.ItemStack getIcon() { return ITMultiblockProvider.COOLING_TOWER.iconStack(); }

    @Override protected DualMapCodec<RegistryFriendlyByteBuf, CoolingTowerRecipe> codecs() { return CODECS; }
}
