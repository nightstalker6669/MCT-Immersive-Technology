package mctmods.immersivetechnology.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class ITUtils {
    public static void dropStackAtPos(Level world, BlockPos pos, ItemStack stack) { Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack); }

    public static boolean sameFluidComponents(FluidStack first, FluidStack second) {
        return FluidStack.isSameFluidSameComponents(first, second);
    }

    public static boolean sameFluidComponentsAndAmount(FluidStack first, FluidStack second) {
        return FluidStack.matches(first, second);
    }

    public static Component fluidDisplayName(FluidStack stack) {
        return stack.getHoverName();
    }

    public static FluidStack copyFluidStackWithAmount(FluidStack stack, int amount, boolean stripPressure) {
        FluidStack copy = stack.copyWithAmount(amount);
        if (stripPressure) {
            removeFluidCustomTag(copy, "pressurized");
        }
        return copy;
    }

    public static boolean fluidHasCustomTag(FluidStack stack, String key) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(key);
    }

    public static void putFluidCustomBoolean(FluidStack stack, String key, boolean value) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putBoolean(key, value);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static void removeFluidCustomTag(FluidStack stack, String key) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.remove(key);
        if (tag.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }

    public static CompoundTag getFluidCustomTag(FluidStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static CompoundTag getOrCreateFluidCustomTag(FluidStack stack) {
        CompoundTag tag = getFluidCustomTag(stack);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return tag;
    }

    public static CompoundTag getItemCustomTag(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void setItemCustomTag(ItemStack stack, CompoundTag tag) {
        if (tag.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }

    public static HolderLookup.Provider serverRegistryAccess() {
        if (ServerLifecycleHooks.getCurrentServer()==null) {
            throw new IllegalStateException("No active server is available for registry-aware serialization");
        }
        return ServerLifecycleHooks.getCurrentServer().registryAccess();
    }
}
