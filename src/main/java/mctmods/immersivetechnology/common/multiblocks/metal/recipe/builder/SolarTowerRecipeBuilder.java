package mctmods.immersivetechnology.common.multiblocks.metal.recipe.builder;

import mctmods.immersivetechnology.compat.ie.crafting.builders.IEFinishedRecipe;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.SolarTowerRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class SolarTowerRecipeBuilder extends IEFinishedRecipe<SolarTowerRecipeBuilder> {
    private SolarTowerRecipeBuilder() { super(SolarTowerRecipe.SERIALIZER.get()); }

    public static SolarTowerRecipeBuilder builder() { return new SolarTowerRecipeBuilder(); }

    public SolarTowerRecipeBuilder addInput(TagKey<Fluid> tag, int amount) { return addFluidTag("input", tag, amount); }

    public SolarTowerRecipeBuilder addOutput(FluidStack output) { return addFluid("output", output); }

    public SolarTowerRecipeBuilder addOutput(Fluid fluid, int amount) { return addOutput(new FluidStack(fluid, amount)); }

    public SolarTowerRecipeBuilder setTime(int time) { return addWriter(json -> json.addProperty("time", time)); }

    public SolarTowerRecipeBuilder setRequiredTemp(double temp) { return addWriter(json -> json.addProperty("requiredTemp", temp)); }
}
