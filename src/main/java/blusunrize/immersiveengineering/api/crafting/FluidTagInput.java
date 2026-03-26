package mctmods.immersivetechnology.compat.ie.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public class FluidTagInput {
    private final TagKey<Fluid> tag;
    private final int amount;

    public FluidTagInput(TagKey<Fluid> tag, int amount) {
        this.tag = tag;
        this.amount = amount;
    }

    public static FluidTagInput deserialize(JsonObject json) {
        ResourceLocation tagId = ResourceLocation.parse(GsonHelper.getAsString(json, "tag"));
        int amount = GsonHelper.getAsInt(json, "amount");
        return new FluidTagInput(TagKey.create(Registries.FLUID, tagId), amount);
    }

    public static FluidTagInput read(FriendlyByteBuf buffer) {
        return new FluidTagInput(TagKey.create(Registries.FLUID, buffer.readResourceLocation()), buffer.readVarInt());
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(tag.location());
        buffer.writeVarInt(amount);
    }

    public boolean test(FluidStack stack) {
        return !stack.isEmpty() && stack.is(tag) && stack.getAmount() >= amount;
    }

    public boolean testIgnoringAmount(FluidStack stack) {
        return !stack.isEmpty() && stack.is(tag);
    }

    public SizedFluidIngredient asSizedIngredient() {
        return SizedFluidIngredient.of(tag, amount);
    }

    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("tag", tag.location().toString());
        json.addProperty("amount", amount);
        return json;
    }

    public TagKey<Fluid> getTag() {
        return tag;
    }

    public int getAmount() {
        return amount;
    }

    public List<FluidStack> getMatchingFluidStacks() {
        return BuiltInRegistries.FLUID.stream()
                .filter(fluid -> BuiltInRegistries.FLUID.wrapAsHolder(fluid).is(tag))
                .map(fluid -> new FluidStack(fluid, amount))
                .toList();
    }
}
