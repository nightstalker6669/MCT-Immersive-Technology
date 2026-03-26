package mctmods.immersivetechnology.common.multiblocks.metal.recipe.serializer;

import mctmods.immersivetechnology.compat.ie.crafting.FluidTagInput;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.GasTurbineRecipe;
import mctmods.immersivetechnology.core.util.codec.ITRecipeCodecs;
import mctmods.immersivetechnology.core.registration.ITMultiblockProvider;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class GasTurbineRecipeSerializer extends IERecipeSerializer<GasTurbineRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, GasTurbineRecipe> CODECS = DualCompositeMapCodecs.composite(
            DualCodecs.RESOURCE_LOCATION.optionalFieldOf("id", ITRecipeCodecs.UNBOUND_ID),
            GasTurbineRecipe::id,
            ITRecipeCodecs.FLUID_TAG_INPUT.codec().fieldOf("input"),
            recipe -> recipe.input,
            ITRecipeCodecs.optionalFluidStack("output"),
            recipe -> recipe.fluidOutput,
            DualCodecs.INT.fieldOf("time"),
            GasTurbineRecipe::getTotalProcessTime,
            DualCodecs.FLOAT.optionalFieldOf("torque", 1.0f),
            recipe -> recipe.torque,
            GasTurbineRecipe::new
    );

    @Override public ItemStack getIcon() { return ITMultiblockProvider.GAS_TURBINE.iconStack(); }

    @Override protected DualMapCodec<RegistryFriendlyByteBuf, GasTurbineRecipe> codecs() { return CODECS; }
}
