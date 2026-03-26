package mctmods.immersivetechnology.common.multiblocks.metal.recipe;

import blusunrize.immersiveengineering.api.crafting.FluidTagInput;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.TagOutputList;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import mctmods.immersivetechnology.core.registration.ITRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DistillerRecipe extends MultiblockRecipe {
    public static RegistryObject<IERecipeSerializer<DistillerRecipe>> SERIALIZER;
    public static final CachedRecipeList<DistillerRecipe> RECIPES = new CachedRecipeList<>(ITRecipeTypes.DISTILLER);

    public final FluidTagInput input;
    @Nullable public final FluidStack fluidOutput;
    public final ItemStack itemOutput;
    public final float chance;
    private final int time;
    private final int energy;
    private final ResourceLocation id;

    public DistillerRecipe(ResourceLocation id, FluidTagInput input, @Nullable FluidStack fluidOutput, ItemStack itemOutput, float chance, int time, int energy) {
        super(itemOutput.isEmpty()?TagOutput.EMPTY:new TagOutput(itemOutput), ITRecipeTypes.DISTILLER, time, energy, () -> new RecipeMultiplier(() -> 1, () -> 1));
        this.id = id;
        this.input = input;
        this.fluidOutput = fluidOutput;
        this.itemOutput = itemOutput;
        this.chance = chance;
        this.time = time;
        this.energy = energy;

        this.fluidInputList = java.util.List.of(this.input.asSizedIngredient());
        if (this.fluidOutput != null) this.fluidOutputList = java.util.List.of(this.fluidOutput);
        this.outputList = itemOutput.isEmpty()?TagOutputList.EMPTY:new TagOutputList(new TagOutput(itemOutput));
    }

    public static DistillerRecipe findRecipe(Level level, FluidStack inputFluid) {
        for (RecipeHolder<DistillerRecipe> holder : RECIPES.getRecipes(level)) {
            DistillerRecipe recipe = holder.value();
            if (recipe.input.test(inputFluid)) return recipe;
        }
        return null;
    }

    @Override public @NotNull ItemStack getResultItem(HolderLookup.Provider registryAccess) { return ItemStack.EMPTY; }

    @Override protected IERecipeSerializer<?> getIESerializer() { return SERIALIZER.get(); }

    @Override public int getTotalProcessTime() { return time; }

    @Override public int getTotalProcessEnergy() { return energy; }

    @Override public int getMultipleProcessTicks() { return 0; }

    public ResourceLocation id() { return id; }
}
