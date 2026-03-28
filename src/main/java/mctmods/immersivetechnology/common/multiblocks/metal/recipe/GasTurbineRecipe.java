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

public class GasTurbineRecipe extends MultiblockRecipe {
    public static RegistryObject<IERecipeSerializer<GasTurbineRecipe>> SERIALIZER;
    public static final CachedRecipeList<GasTurbineRecipe> RECIPES = new CachedRecipeList<>(ITRecipeTypes.GAS_TURBINE);

    public final FluidTagInput input;
    @Nullable public final FluidStack fluidOutput;
    /**
     * Dimensionless torque multiplier applied to mechanical output while this recipe is used.
     *
     * <p>Defaults to {@code 1.0f} for backwards compatibility with older datapacks.</p>
     */
    public final float torque;
    private final int time;
    private final ResourceLocation id;

    public GasTurbineRecipe(ResourceLocation id, FluidTagInput input, @Nullable FluidStack fluidOutput, int time, float torque) {
        super(TagOutput.EMPTY, ITRecipeTypes.GAS_TURBINE, time, 0, () -> new RecipeMultiplier(() -> 1, () -> 1));
        this.id = id;
        this.input = input;
        this.fluidOutput = fluidOutput == null || fluidOutput.isEmpty() ? null : fluidOutput;
        this.time = time;
        this.torque = torque;
        this.fluidInputList = java.util.List.of(this.input.asSizedIngredient());
        this.fluidOutputList = this.fluidOutput == null ? java.util.List.of() : java.util.List.of(this.fluidOutput);
    }

    @Override protected IERecipeSerializer<?> getIESerializer() { return SERIALIZER.get(); }

    @Override @NotNull public ItemStack getResultItem(HolderLookup.Provider registryAccess) { return ItemStack.EMPTY; }

    public boolean matches(FluidStack fluid) { return input.test(fluid); }

    public static GasTurbineRecipe findRecipe(Level level, FluidStack fluid, @Nullable GasTurbineRecipe hint) {
        if (hint != null && hint.matches(fluid)) return hint;
        for (RecipeHolder<GasTurbineRecipe> holder : RECIPES.getRecipes(level)) {
            GasTurbineRecipe recipe = holder.value();
            if (recipe.matches(fluid)) return recipe;
        }
        return null;
    }

    @Override public int getTotalProcessTime() { return time; }

    @Override public int getTotalProcessEnergy() { return 0; }

    @Override public int getMultipleProcessTicks() { return 0; }

    public ResourceLocation id() { return id; }
}
