package mctmods.immersivetechnology.compat.ie.crafting.builders;

import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import com.google.gson.JsonObject;
import mctmods.immersivetechnology.compat.ie.crafting.FluidTagInput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings({"rawtypes", "unchecked"})
public class IEFinishedRecipe<T extends IEFinishedRecipe<T>> {
    protected final RecipeSerializer<?> serializer;
    protected int maxInputCount = 1;
    private final List<Consumer<JsonObject>> writers = new ArrayList<>();

    protected IEFinishedRecipe(RecipeSerializer<?> serializer) {
        this.serializer = serializer;
    }

    protected T addWriter(Consumer<JsonObject> writer) {
        writers.add(writer);
        return (T)this;
    }

    protected T addIngredient(String key, IngredientWithSize ingredient) {
        return addWriter(json -> json.add(key, ingredient.serialize()));
    }

    protected T addItem(String key, ItemLike item) {
        return addItem(key, new ItemStack(item));
    }

    protected T addItem(String key, ItemStack stack) {
        return addWriter(json -> {
            JsonObject value = new JsonObject();
            value.addProperty("item", Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(stack.getItem())).toString());
            if (stack.getCount() > 1) {
                value.addProperty("count", stack.getCount());
            }
            json.add(key, value);
        });
    }

    protected T addFluid(String key, Fluid fluid, int amount) {
        return addFluid(key, new FluidStack(fluid, amount));
    }

    protected T addFluid(String key, FluidStack stack) {
        return addWriter(json -> {
            JsonObject value = new JsonObject();
            value.addProperty("id", Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(stack.getFluid())).toString());
            value.addProperty("amount", stack.getAmount());
            json.add(key, value);
        });
    }

    protected T addFluidTag(String key, TagKey<Fluid> fluidTag, int amount) {
        return addFluidTag(key, new FluidTagInput(fluidTag, amount));
    }

    protected T addFluidTag(String key, FluidTagInput input) {
        return addWriter(json -> json.add(key, input.serialize()));
    }

    public void build(Consumer out, ResourceLocation id) {
        JsonObject json = new JsonObject();
        json.addProperty("type", Objects.requireNonNull(BuiltInRegistries.RECIPE_SERIALIZER.getKey(serializer)).toString());
        json.addProperty("id", id.toString());
        for (Consumer<JsonObject> writer : writers) {
            writer.accept(json);
        }
        out.accept(new GeneratedRecipe(id, json));
    }

    public record GeneratedRecipe(ResourceLocation id, JsonObject json) {
    }
}
