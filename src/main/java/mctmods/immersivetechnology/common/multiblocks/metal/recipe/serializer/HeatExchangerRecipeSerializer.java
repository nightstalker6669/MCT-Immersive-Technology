package mctmods.immersivetechnology.common.multiblocks.metal.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.HeatExchangerRecipe;
import mctmods.immersivetechnology.core.util.codec.ITRecipeCodecs;
import mctmods.immersivetechnology.core.registration.ITMultiblockProvider;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class HeatExchangerRecipeSerializer extends IERecipeSerializer<HeatExchangerRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, HeatExchangerRecipe> CODECS = DualCompositeMapCodecs.composite(
            DualCodecs.RESOURCE_LOCATION.optionalFieldOf("id", ITRecipeCodecs.UNBOUND_ID),
            HeatExchangerRecipe::id,
            ITRecipeCodecs.FLUID_TAG_INPUT.codec().fieldOf("input0"),
            recipe -> recipe.input0,
            ITRecipeCodecs.FLUID_TAG_INPUT.codec().optionalFieldOf("input1").map(optional -> optional.orElse(null), value -> java.util.Optional.ofNullable(value)),
            recipe -> recipe.input1,
            IEDualCodecs.FLUID_STACK.fieldOf("output0"),
            recipe -> recipe.output0,
            ITRecipeCodecs.optionalFluidStack("output1"),
            recipe -> recipe.output1,
            DualCodecs.INT.fieldOf("energy"),
            HeatExchangerRecipe::getTotalProcessEnergy,
            DualCodecs.INT.fieldOf("time"),
            HeatExchangerRecipe::getTotalProcessTime,
            (id, input0, input1, output0, output1, energy, time) ->
                    new HeatExchangerRecipe(id, input0, input1, output0, output1, energy, time)
                            .modifyTimeAndEnergy(t -> t * HeatExchangerRecipe.timeModifier, e -> e * HeatExchangerRecipe.energyModifier)
    );

    @Override public ItemStack getIcon() { return ITMultiblockProvider.HEAT_EXCHANGER.iconStack(); }

    @Override protected DualMapCodec<RegistryFriendlyByteBuf, HeatExchangerRecipe> codecs() { return CODECS; }
}
