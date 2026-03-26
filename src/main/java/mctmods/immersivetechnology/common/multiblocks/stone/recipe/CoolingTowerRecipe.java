package mctmods.immersivetechnology.common.multiblocks.stone.recipe;

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
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class CoolingTowerRecipe extends MultiblockRecipe {
    public static RegistryObject<IERecipeSerializer<CoolingTowerRecipe>> SERIALIZER;
    public static final CachedRecipeList<CoolingTowerRecipe> RECIPES = new CachedRecipeList<>(ITRecipeTypes.COOLING_TOWER);

    public final FluidStack fluidOutput0;
    public final FluidStack fluidOutput1;
    public final FluidStack fluidOutput2;
    public final FluidTagInput input0;
    public final FluidTagInput input1;
    public final int totalProcessTime;
    private final ResourceLocation id;

    public CoolingTowerRecipe(ResourceLocation id, FluidStack fluidOutput0, FluidStack fluidOutput1, FluidStack fluidOutput2, FluidTagInput input0, FluidTagInput input1, int time) {
        super(TagOutput.EMPTY, ITRecipeTypes.COOLING_TOWER, time, 0, () -> new RecipeMultiplier(() -> 1, () -> 1));
        this.id = id;
        this.fluidOutput0 = fluidOutput0;
        this.fluidOutput1 = fluidOutput1;
        this.fluidOutput2 = fluidOutput2;
        this.input0 = input0;
        this.input1 = input1;
        this.totalProcessTime = time;
        this.fluidInputList = Lists.newArrayList(input0.asSizedIngredient(), input1.asSizedIngredient());
        this.fluidOutputList = Lists.newArrayList(fluidOutput0, fluidOutput1, fluidOutput2);
    }

    public static CoolingTowerRecipe findRecipe(Level level, FluidStack fluidInput0, FluidStack fluidInput1) {
        if (fluidInput0.isEmpty() || fluidInput1.isEmpty()) return null;
        for (RecipeHolder<CoolingTowerRecipe> holder : RECIPES.getRecipes(level)) {
            CoolingTowerRecipe r = holder.value();
            if (r.input0.test(fluidInput0) && fluidInput0.getAmount() >= r.input0.getAmount() && r.input1.test(fluidInput1) && fluidInput1.getAmount() >= r.input1.getAmount()) return r;
        }
        return null;
    }

    @Override public @NotNull ItemStack getResultItem(HolderLookup.Provider registryAccess) { return ItemStack.EMPTY; }

    @Override protected IERecipeSerializer<?> getIESerializer() { return SERIALIZER.get(); }

    @Override public int getTotalProcessTime() { return totalProcessTime; }

    @Override public int getTotalProcessEnergy() { return 0; }

    @Override public int getMultipleProcessTicks() { return 0; }

    public ResourceLocation id() { return id; }
}
