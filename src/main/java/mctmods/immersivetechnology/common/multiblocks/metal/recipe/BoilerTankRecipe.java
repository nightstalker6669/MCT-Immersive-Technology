package mctmods.immersivetechnology.common.multiblocks.metal.recipe;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import com.immersiveconvergence.api.HeatCapabilities;
import mctmods.immersivetechnology.compat.ie.crafting.FluidTagInput;
import mctmods.immersivetechnology.core.registration.ITRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class BoilerTankRecipe extends MultiblockRecipe {
    public static RegistryObject<IERecipeSerializer<BoilerTankRecipe>> SERIALIZER;
    public static final CachedRecipeList<BoilerTankRecipe> RECIPES = new CachedRecipeList<>(ITRecipeTypes.BOILER_TANK);

    public final FluidTagInput input;
    public final FluidStack output;
    public final double requiredHeat;
    private final int time;
    private final ResourceLocation id;

    public BoilerTankRecipe(ResourceLocation id, FluidTagInput input, FluidStack output, int time, double requiredHeat) {
        super(TagOutput.EMPTY, ITRecipeTypes.BOILER_TANK, time, 0, () -> new RecipeMultiplier(() -> 1, () -> 1));
        this.id = id;
        this.input = input;
        this.output = output;
        this.time = time;
        this.requiredHeat = Math.min(requiredHeat, HeatCapabilities.getMaxHeat());
        this.fluidInputList = java.util.List.of(this.input.asSizedIngredient());
        this.fluidOutputList = java.util.List.of(this.output);
    }

    @Override protected IERecipeSerializer<?> getIESerializer() {
        return SERIALIZER.get();
    }

    @Override @NotNull public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return ItemStack.EMPTY;
    }

    public static BoilerTankRecipe findRecipe(Level level, FluidStack input) {
        for (RecipeHolder<BoilerTankRecipe> holder : RECIPES.getRecipes(level)) {
            BoilerTankRecipe recipe = holder.value();
            if (recipe.input.test(input)) return recipe;
        }
        return null;
    }

    @Override public int getTotalProcessTime() {
        return time;
    }

    @Override public int getTotalProcessEnergy() {
        return 0;
    }

    @Override public int getMultipleProcessTicks() {
        return 0;
    }

    public ResourceLocation id() { return id; }
}
