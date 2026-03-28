package mctmods.immersivetechnology.common.multiblocks.metal.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.DistillerRecipe;
import mctmods.immersivetechnology.core.util.codec.ITRecipeCodecs;
import mctmods.immersivetechnology.core.registration.ITMultiblockProvider;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class DistillerRecipeSerializer extends IERecipeSerializer<DistillerRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, DistillerRecipe> CODECS = DualCompositeMapCodecs.composite(
            DualCodecs.RESOURCE_LOCATION.optionalFieldOf("id", ITRecipeCodecs.UNBOUND_ID),
            DistillerRecipe::id,
            ITRecipeCodecs.FLUID_TAG_INPUT.codec().fieldOf("input"),
            recipe -> recipe.input,
            IEDualCodecs.FLUID_STACK.fieldOf("result"),
            recipe -> recipe.fluidOutput,
            ITRecipeCodecs.optionalChancedItemOutput("item_output"),
            recipe -> recipe.itemOutput.isEmpty() ? ITRecipeCodecs.ResourceBackedOutput.EMPTY : new ITRecipeCodecs.ResourceBackedOutput(new TagOutput(recipe.itemOutput), recipe.chance),
            DualCodecs.INT.fieldOf("time"),
            DistillerRecipe::getTotalProcessTime,
            DualCodecs.INT.fieldOf("energy"),
            DistillerRecipe::getTotalProcessEnergy,
            (id, input, fluidOutput, itemOutput, time, energy) -> new DistillerRecipe(id, input, fluidOutput, itemOutput.output().get(), itemOutput.chance(), time, energy)
    );

    @Override public ItemStack getIcon() { return ITMultiblockProvider.DISTILLER.iconStack(); }

    @Override protected DualMapCodec<RegistryFriendlyByteBuf, DistillerRecipe> codecs() { return CODECS; }
}
