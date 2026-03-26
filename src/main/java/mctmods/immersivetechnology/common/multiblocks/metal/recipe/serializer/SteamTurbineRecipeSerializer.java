package mctmods.immersivetechnology.common.multiblocks.metal.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.SteamTurbineRecipe;
import mctmods.immersivetechnology.core.util.codec.ITRecipeCodecs;
import mctmods.immersivetechnology.core.registration.ITMultiblockProvider;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class SteamTurbineRecipeSerializer extends IERecipeSerializer<SteamTurbineRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, SteamTurbineRecipe> CODECS = DualCompositeMapCodecs.composite(
            DualCodecs.RESOURCE_LOCATION.optionalFieldOf("id", ITRecipeCodecs.UNBOUND_ID),
            SteamTurbineRecipe::id,
            ITRecipeCodecs.FLUID_TAG_INPUT.codec().fieldOf("input"),
            recipe -> recipe.input,
            ITRecipeCodecs.optionalFluidStack("output"),
            recipe -> recipe.fluidOutput,
            DualCodecs.INT.fieldOf("time"),
            SteamTurbineRecipe::getTotalProcessTime,
            DualCodecs.FLOAT.optionalFieldOf("torque", 1.0f),
            recipe -> recipe.torque,
            SteamTurbineRecipe::new
    );

    @Override public ItemStack getIcon() { return ITMultiblockProvider.STEAM_TURBINE.iconStack(); }

    @Override protected DualMapCodec<RegistryFriendlyByteBuf, SteamTurbineRecipe> codecs() { return CODECS; }
}
