package mctmods.immersivetechnology.core.util.codec;

import mctmods.immersivetechnology.compat.ie.crafting.FluidTagInput;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Optional;

public final class ITRecipeCodecs {
    public static final ResourceLocation UNBOUND_ID = ResourceLocation.fromNamespaceAndPath("immersivetechnology", "unbound");
    public static final DualMapCodec<RegistryFriendlyByteBuf, FluidTagInput> FLUID_TAG_INPUT = DualCompositeMapCodecs.composite(
            IEDualCodecs.tag(Registries.FLUID).fieldOf("tag"),
            FluidTagInput::getTag,
            DualCodecs.INT.fieldOf("amount"),
            FluidTagInput::getAmount,
            FluidTagInput::new
    );

    public static final DualMapCodec<RegistryFriendlyByteBuf, FluidStack> REQUIRED_FLUID_STACK = IEDualCodecs.FLUID_STACK.fieldOf("fluid");

    private ITRecipeCodecs() {
    }

    public static DualMapCodec<RegistryFriendlyByteBuf, FluidStack> optionalFluidStack(String name) {
        return IEDualCodecs.FLUID_STACK.optionalFieldOf(name).map(
                optional -> optional.orElse(FluidStack.EMPTY),
                value -> value == null || value.isEmpty() ? Optional.empty() : Optional.of(value)
        );
    }

    public static DualMapCodec<RegistryFriendlyByteBuf, ResourceBackedOutput> optionalChancedItemOutput(String name) {
        return ResourceBackedOutput.CODECS.codec().optionalFieldOf(name).map(
                optional -> optional.orElse(ResourceBackedOutput.EMPTY),
                value -> value == null || value.output().get().isEmpty() ? Optional.empty() : Optional.of(value)
        );
    }

    public record ResourceBackedOutput(TagOutput output, float chance) {
        public static final ResourceBackedOutput EMPTY = new ResourceBackedOutput(TagOutput.EMPTY, 0f);
        public static final DualMapCodec<RegistryFriendlyByteBuf, ResourceBackedOutput> CODECS = DualCompositeMapCodecs.composite(
                TagOutput.CODECS.fieldOf("item"),
                ResourceBackedOutput::output,
                DualCodecs.FLOAT.optionalFieldOf("chance", 0f),
                ResourceBackedOutput::chance,
                ResourceBackedOutput::new
        );
    }
}
