package mctmods.immersivetechnology.common.multiblocks.stone.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import mctmods.immersivetechnology.common.multiblocks.stone.recipe.AdvancedCokeOvenRecipe;
import mctmods.immersivetechnology.core.util.codec.ITRecipeCodecs;
import mctmods.immersivetechnology.core.registration.ITMultiblockProvider;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.Lazy;

public class AdvancedCokeOvenRecipeSerializer extends IERecipeSerializer<AdvancedCokeOvenRecipe> {
    public static final DualMapCodec<RegistryFriendlyByteBuf, AdvancedCokeOvenRecipe> CODECS = DualCompositeMapCodecs.composite(
            DualCodecs.RESOURCE_LOCATION.optionalFieldOf("id", ITRecipeCodecs.UNBOUND_ID),
            AdvancedCokeOvenRecipe::id,
            IngredientWithSize.CODECS.fieldOf("input"),
            recipe -> recipe.input,
            TagOutput.CODECS.fieldOf("result"),
            recipe -> new TagOutput(recipe.itemOutput.get()),
            DualCodecs.INT.fieldOf("time"),
            recipe -> recipe.time,
            DualCodecs.INT.fieldOf("creosote"),
            recipe -> recipe.creosoteOutput,
            (id, input, output, time, creosote) -> new AdvancedCokeOvenRecipe(id, input, Lazy.of(output::get), time, creosote)
    );

    @Override
    public ItemStack getIcon() { return ITMultiblockProvider.ADVANCED_COKE_OVEN.iconStack(); }

    @Override protected DualMapCodec<RegistryFriendlyByteBuf, AdvancedCokeOvenRecipe> codecs() { return CODECS; }
}
