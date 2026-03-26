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

public class BoilerLiquidRecipe extends MultiblockRecipe {
    public static RegistryObject<IERecipeSerializer<BoilerLiquidRecipe>> SERIALIZER;
    public static final CachedRecipeList<BoilerLiquidRecipe> RECIPES = new CachedRecipeList<>(ITRecipeTypes.BOILER_LIQUID);

    public final FluidTagInput input;
    private final int time;
    private final double heatPerTick;
    private final double targetHeat;
    private final ResourceLocation id;

    public BoilerLiquidRecipe(ResourceLocation id, FluidTagInput input, int time, double heatPerTick, double targetHeat) {
        super(TagOutput.EMPTY, ITRecipeTypes.BOILER_LIQUID, time, 0, () -> new RecipeMultiplier(() -> 1, () -> 1));
        this.id = id;
        this.input = input;
        this.time = time;
        this.heatPerTick = heatPerTick;
        this.targetHeat = Math.min(targetHeat, HeatCapabilities.getMaxHeat());
        this.fluidInputList = java.util.List.of(this.input.asSizedIngredient());
    }

    public static BoilerLiquidRecipe findRecipe(Level level, FluidStack input) {
        for (RecipeHolder<BoilerLiquidRecipe> holder : RECIPES.getRecipes(level)) {
            BoilerLiquidRecipe recipe = holder.value();
            if (recipe.input.test(input)) return recipe;
        }
        return null;
    }

    @Override @NotNull public ItemStack getResultItem(HolderLookup.Provider registryAccess) { return ItemStack.EMPTY; }

    @Override protected IERecipeSerializer<?> getIESerializer() { return SERIALIZER.get(); }

    @Override public int getTotalProcessTime() { return time; }

    @Override public int getTotalProcessEnergy() { return 0; }

    @Override public int getMultipleProcessTicks() { return 0; }

    public double getHeatPerTick() { return heatPerTick; }

    public double getTargetHeat() { return targetHeat; }

    public ResourceLocation id() { return id; }
}
