package mctmods.immersivetechnology.common.blocks.wooden.logic;

import mctmods.immersivetechnology.common.blocks.wooden.gui.CrateCreativeMenu;
import mctmods.immersivetechnology.core.util.TranslationKey;
import mctmods.immersivetechnology.core.registration.ITBlockEntities;
import mctmods.immersivetechnology.core.registration.ITMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class CrateCreativeBlockEntity extends BlockEntity implements MenuProvider, IItemHandlerModifiable {

    private ItemStack template = ItemStack.EMPTY;

    public CrateCreativeBlockEntity(BlockPos pos, BlockState state) {
        super(ITBlockEntities.CRATE_CREATIVE.get(), pos, state);
    }

    @Override protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.saveAdditional(tag, lookupProvider);
        if (!template.isEmpty()) {
            tag.put("template", template.saveOptional(lookupProvider));
        }
    }

    @Override protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.loadAdditional(tag, lookupProvider);
        if (tag.contains("template", Tag.TAG_COMPOUND)) { template = ItemStack.parseOptional(lookupProvider, tag.getCompound("template")); }
    }

    @Override @NotNull public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
        CompoundTag tag = super.getUpdateTag(lookupProvider);
        saveAdditional(tag, lookupProvider);
        return tag;
    }

    @Override public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        loadAdditional(tag, lookupProvider);
    }

    public void onBEPlaced(ItemStack stack) {
        CompoundTag customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (level != null && customData.contains("template", Tag.TAG_COMPOUND)) {
            template = ItemStack.parseOptional(level.registryAccess(), customData.getCompound("template"));
            setChanged();
        }
    }

    @Override @NotNull public Component getDisplayName() { return Component.translatable(TranslationKey.GUI_CRATE_CREATIVE.getLocation()); }

    @Override public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) { return CrateCreativeMenu.makeServer(ITMenuTypes.CRATE_CREATIVE.getType(), id, inv, this); }

    public boolean stillValid(Player player) {
        if (level != null && !level.isClientSide) {
            return !this.isRemoved() && player.distanceToSqr(Vec3.atCenterOf(getBlockPos())) <= 64.0D;
        }
        return false;
    }

    @Override public int getSlots() { return 1; }

    @Override @NotNull public ItemStack getStackInSlot(int slot) { return slot == 0 ? template.copy() : ItemStack.EMPTY; }

    @Override @NotNull public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot != 0 || stack.isEmpty()) { return stack; }
        if (simulate) { return ItemStack.EMPTY; }
        template = stack.copy();
        setChanged();
        return ItemStack.EMPTY;
    }

    @Override @NotNull public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != 0 || template.isEmpty() || amount <= 0) { return ItemStack.EMPTY; }
        ItemStack out = template.copy();
        out.setCount(Math.min(amount, template.getCount()));
        return out;
    }

    @Override public int getSlotLimit(int slot) { return slot == 0 ? (template.isEmpty() ? 64 : template.getMaxStackSize()) : 0; }

    @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) { return slot == 0 && !stack.isEmpty(); }

    @Override public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        if (slot == 0) {
            template = stack.copy();
            setChanged();
        }
    }
}
