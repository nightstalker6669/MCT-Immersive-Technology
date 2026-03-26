package mctmods.immersivetechnology.common.multiblocks.metal.recipe;

import mctmods.immersivetechnology.compat.ie.crafting.FluidTagInput;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
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

public class SolarMelterRecipe extends MultiblockRecipe {
    public static RegistryObject<IERecipeSerializer<SolarMelterRecipe>> SERIALIZER;
    public static final CachedRecipeList<SolarMelterRecipe> RECIPES = new CachedRecipeList<>(ITRecipeTypes.SOLAR_MELTER);

    public final FluidTagInput input;
    public final FluidStack fluidOutput;
    private final int time;
    public final double requiredTemp;
    private final ResourceLocation id;

    public SolarMelterRecipe(ResourceLocation id, FluidTagInput input, @Nullable FluidStack fluidOutput, int time, double requiredTemp) {
        super(TagOutput.EMPTY, ITRecipeTypes.SOLAR_MELTER, time, 0, () -> new RecipeMultiplier(() -> 1, () -> 1));
        this.id = id;
        this.input = input;
        this.fluidOutput = fluidOutput;
        this.time = time;
        this.requiredTemp = requiredTemp;
        this.fluidInputList = java.util.List.of(this.input.asSizedIngredient());
        this.fluidOutputList = fluidOutput == null ? java.util.List.of() : java.util.List.of(fluidOutput);
    }

    @Nullable public static SolarMelterRecipe findRecipe(Level level, FluidStack fluid) {
        if (fluid == null || fluid.isEmpty()) return null;
        for (RecipeHolder<SolarMelterRecipe> holder : RECIPES.getRecipes(level)) {
            SolarMelterRecipe recipe = holder.value();
            if (recipe.input.testIgnoringAmount(fluid) && fluid.getAmount() >= recipe.input.getAmount()) return recipe;
        }
        return null;
    }

    @Override public @NotNull ItemStack getResultItem(HolderLookup.Provider registryAccess) { return ItemStack.EMPTY; }

    @Override protected IERecipeSerializer<?> getIESerializer() { return SERIALIZER.get(); }

    @Override public int getMultipleProcessTicks() { return 0; }

    @Override public int getTotalProcessTime() { return time; }

    @Override public int getTotalProcessEnergy() { return 0; }

    public ResourceLocation id() { return id; }
}
