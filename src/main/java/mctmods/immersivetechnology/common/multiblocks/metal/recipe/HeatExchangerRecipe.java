package mctmods.immersivetechnology.common.multiblocks.metal.recipe;

import mctmods.immersivetechnology.compat.ie.crafting.FluidTagInput;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import com.google.common.collect.Lists;
import mctmods.immersivetechnology.core.registration.ITRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class HeatExchangerRecipe extends MultiblockRecipe {
    public static RegistryObject<IERecipeSerializer<HeatExchangerRecipe>> SERIALIZER;
    public static final CachedRecipeList<HeatExchangerRecipe> RECIPES = new CachedRecipeList<>(ITRecipeTypes.HEAT_EXCHANGER);

    public static float timeModifier = 1;
    public static float energyModifier = 1;

    public final FluidTagInput input0;
    public final FluidTagInput input1;
    public final FluidStack output0;
    public final FluidStack output1;

    int totalProcessTime;
    int totalProcessEnergy;
    private final ResourceLocation id;

    public HeatExchangerRecipe(ResourceLocation id, FluidTagInput input0, FluidTagInput input1, FluidStack output0, FluidStack output1, int energy, int time) {
        super(TagOutput.EMPTY, ITRecipeTypes.HEAT_EXCHANGER, time, energy, () -> new RecipeMultiplier(() -> 1, () -> 1));
        this.id = id;
        this.input0 = input0;
        this.input1 = input1;
        this.output0 = output0;
        this.output1 = output1 == null || output1.isEmpty() ? null : output1;
        this.totalProcessTime = time;
        this.totalProcessEnergy = energy;

        this.fluidInputList = Lists.<SizedFluidIngredient>newArrayList(this.input0.asSizedIngredient());
        if (this.input1 != null) this.fluidInputList.add(this.input1.asSizedIngredient());
        this.fluidOutputList = Lists.newArrayList(this.output0);
        if (this.output1 != null) this.fluidOutputList.add(this.output1);
    }

    public HeatExchangerRecipe modifyTimeAndEnergy(Function<Double, Double> time, Function<Double, Double> energy) {
        this.totalProcessTime = (int)Math.floor(time.apply((double)this.totalProcessTime));
        this.totalProcessEnergy = (int)Math.floor(energy.apply((double)this.totalProcessEnergy));
        return this;
    }

    public static HeatExchangerRecipe findRecipe(Level level, FluidStack input0, FluidStack input1) {
        for (RecipeHolder<HeatExchangerRecipe> holder : RECIPES.getRecipes(level)) {
            HeatExchangerRecipe recipe = holder.value();
            if (recipe.input0.test(input0) && (recipe.input1 == null || recipe.input1.test(input1))) return recipe;
        }
        return null;
    }

    @Override public int getMultipleProcessTicks() { return 0; }

    @Override public int getTotalProcessTime() { return this.totalProcessTime; }

    @Override public int getTotalProcessEnergy() { return this.totalProcessEnergy; }

    @Override @NotNull public ItemStack getResultItem(HolderLookup.Provider registryAccess) { return ItemStack.EMPTY; }

    @Override protected IERecipeSerializer<?> getIESerializer() { return SERIALIZER.get(); }

    public ResourceLocation id() { return id; }
}
