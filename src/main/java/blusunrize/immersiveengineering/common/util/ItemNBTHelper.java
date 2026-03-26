package blusunrize.immersiveengineering.common.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class ItemNBTHelper {
    private ItemNBTHelper() {
    }

    public static boolean hasKey(ItemStack stack, String key) {
        return getTag(stack).contains(key);
    }

    public static boolean hasKey(ItemStack stack, String key, int type) {
        return getTag(stack).contains(key, type);
    }

    public static int getInt(ItemStack stack, String key) {
        return getTag(stack).getInt(key);
    }

    public static CompoundTag getTagCompound(ItemStack stack, String key) {
        return getTag(stack).getCompound(key);
    }

    private static CompoundTag getTag(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }
}
