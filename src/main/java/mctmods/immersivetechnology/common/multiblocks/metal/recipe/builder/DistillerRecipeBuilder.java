package mctmods.immersivetechnology.common.multiblocks.metal.recipe.builder;

import mctmods.immersivetechnology.compat.ie.crafting.FluidTagInput;
import mctmods.immersivetechnology.compat.ie.crafting.builders.IEFinishedRecipe;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.DistillerRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import java.util.Objects;

public class DistillerRecipeBuilder extends IEFinishedRecipe<DistillerRecipeBuilder> {
    public DistillerRecipeBuilder() {
        super(DistillerRecipe.SERIALIZER.get());
        this.maxInputCount = 2;
    }

    public static DistillerRecipeBuilder builder(FluidTagInput fluidIn, FluidStack primaryFluidOutput, int time, int energy) {
        return new DistillerRecipeBuilder().addFluidTag("input", fluidIn).addFluid("result", primaryFluidOutput).setTime(time).setEnergy(energy);
    }

    public DistillerRecipeBuilder setTime(int time) { return addWriter(json -> json.addProperty("time", time)); }

    public DistillerRecipeBuilder setEnergy(int energy) { return addWriter(json -> json.addProperty("energy", energy)); }

    public DistillerRecipeBuilder addItemOutput(ItemStack item, float chance) {
        return this.addWriter(jsonObject -> {
            com.google.gson.JsonObject itemStackJson = new com.google.gson.JsonObject();
            itemStackJson.addProperty("item", Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.getItem())).toString());
            if (item.getCount() > 1) itemStackJson.addProperty("count", item.getCount());

            com.google.gson.JsonObject itemOutputJson = new com.google.gson.JsonObject();
            itemOutputJson.add("item", itemStackJson);
            itemOutputJson.addProperty("chance", chance);
            jsonObject.add("item_output", itemOutputJson);
        });
    }
}
