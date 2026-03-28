package mctmods.immersivetechnology.common.blocks.metal.logic;

import blusunrize.immersiveengineering.api.fluid.IFluidPipe;
import blusunrize.immersiveengineering.common.blocks.metal.FluidPipeBlockEntity;
import blusunrize.immersiveengineering.common.util.Utils;
import java.text.DecimalFormat;
import mctmods.immersivetechnology.common.blocks.helper.ITBlockInterfaces;
import mctmods.immersivetechnology.core.network.ITOSDRequestMessage;
import mctmods.immersivetechnology.core.network.ITPacketHandler;
import mctmods.immersivetechnology.core.util.TranslationKey;
import mctmods.immersivetechnology.core.util.ITUtils;
import mctmods.immersivetechnology.core.ITClientConfig;
import mctmods.immersivetechnology.core.ITServerConfig;
import mctmods.immersivetechnology.core.registration.ITBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.SoundActions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import mctmods.immersivetechnology.core.util.compat.LazyOptional;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

public class BarrelCreativeBlockEntity extends OSDCommonBlockEntity implements ITBlockInterfaces.IBlockEntityDrop, ITBlockInterfaces.IPlayerInteraction {
    private FluidStack selectedFluid = FluidStack.EMPTY;

    private static final int CREATIVE_BARREL_OUTPUT_AMOUNT = ITServerConfig.creativeBarrelOutputAmount;

    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> new IFluidHandler() {
        @Override public int getTanks() { return 1; }

        @Override @NotNull public FluidStack getFluidInTank(int tank) {
            if (selectedFluid.isEmpty()) { return FluidStack.EMPTY; }
            return selectedFluid.copyWithAmount(Integer.MAX_VALUE);
        }

        @Override public int getTankCapacity(int tank) { return Integer.MAX_VALUE; }

        @Override public boolean isFluidValid(int tank, @NotNull FluidStack stack) { return false; }

        @Override public int fill(FluidStack resource, FluidAction action) { return 0; }

        @Override @NotNull public FluidStack drain(FluidStack resource, FluidAction action) {
            if (selectedFluid.isEmpty() || !ITUtils.sameFluidComponents(selectedFluid, resource)) { return FluidStack.EMPTY; }
            return selectedFluid.copyWithAmount(resource.getAmount());
        }

        @Override public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (selectedFluid.isEmpty()) { return FluidStack.EMPTY; }
            return selectedFluid.copyWithAmount(maxDrain);
        }
    });

    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,##0.###");

    public BarrelCreativeBlockEntity(BlockPos pos, BlockState state) { super(ITBlockEntities.BARREL_CREATIVE.get(), pos, state); }

    @Override public void tickServer() {
        if (!selectedFluid.isEmpty()) {
            long thisTickOutput = 0;
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = worldPosition.relative(dir);
                assert level != null;
                BlockEntity neighbor = level.getBlockEntity(neighborPos);
                boolean isPipe = neighbor instanceof FluidPipeBlockEntity;
                FluidStack fs = selectedFluid.copy();
                fs.setAmount(CREATIVE_BARREL_OUTPUT_AMOUNT);
                boolean hadTag = ITUtils.fluidHasCustomTag(fs, IFluidPipe.NBT_PRESSURIZED);
                if (isPipe && !hadTag) { ITUtils.putFluidCustomBoolean(fs, IFluidPipe.NBT_PRESSURIZED, true); }
                LazyOptional<IFluidHandler> cap = LazyOptional.ofNullable(FluidUtil.getFluidHandler(level, neighborPos, dir.getOpposite()).orElse(null));
                if (!cap.isPresent()) { continue; }
                IFluidHandler handler = cap.orElseThrow(AssertionError::new);
                int accepted = handler.fill(fs, FluidAction.SIMULATE);
                if (!hadTag) { ITUtils.removeFluidCustomTag(fs, IFluidPipe.NBT_PRESSURIZED); }
                if (accepted <= 0) { continue; }
                FluidStack toFill = Utils.copyFluidStackWithAmount(fs, accepted, false);
                if (isPipe) { ITUtils.putFluidCustomBoolean(toFill, IFluidPipe.NBT_PRESSURIZED, true); }
                int filled = handler.fill(toFill, FluidAction.EXECUTE);
                thisTickOutput += filled;
            }
            acceptedAmount += thisTickOutput;
        }
        super.tickServer();
    }

    @Override public void readCustomNBT(@NotNull CompoundTag nbt, boolean descPacket) {
        if (nbt.contains("SelectedFluid")) {
            HolderLookup.Provider lookupProvider = level!=null?level.registryAccess(): ITUtils.serverRegistryAccess();
            selectedFluid = FluidStack.parseOptional(lookupProvider, nbt.getCompound("SelectedFluid"));
        }
    }

    @Override public void writeCustomNBT(@NotNull CompoundTag nbt, boolean descPacket) {
        if (!selectedFluid.isEmpty()) {
            HolderLookup.Provider lookupProvider = level!=null?level.registryAccess(): ITUtils.serverRegistryAccess();
            nbt.put("SelectedFluid", selectedFluid.saveOptional(lookupProvider));
        }
    }

    @Override public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) { return fluidHandler.cast(); }
        return super.getCapability(cap, side);
    }

    @Override public boolean interact(@NotNull Direction side, @NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack heldItem, float hitX, float hitY, float hitZ) {
        FluidStack contained = FluidUtil.getFluidContained(heldItem).orElse(FluidStack.EMPTY);
        if (!contained.isEmpty()) {
            setOutputFluid(contained);
            if (level != null && !level.isClientSide) {
                SoundEvent sound = contained.getFluid().getFluidType().getSound(player, level, worldPosition, SoundActions.BUCKET_EMPTY);
                if (sound == null) { sound = contained.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY; }
                level.playSound(null, worldPosition, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return true;
        } else if (player.isShiftKeyDown()) {
            setOutputFluid(FluidStack.EMPTY);
            return true;
        }
        return FluidUtil.interactWithFluidHandler(player, hand, fluidHandler.orElseThrow(RuntimeException::new));
    }

    @Override public TranslationKey text() { return TranslationKey.OVERLAY_OSD_BARREL_NORMAL_FIRST_LINE; }

    @Override public Component[] getOverlayText(@NotNull Player player, @NotNull HitResult rtr, boolean hammer) {
        if (rtr.getType() == HitResult.Type.MISS) { return null; }
        assert level != null;
        if (level.isClientSide && requestCooldown == 0) {
            ITPacketHandler.sendToServer(new ITOSDRequestMessage(worldPosition));
            requestCooldown = 20;
        }
        if (selectedFluid.isEmpty()) { return new Component[]{Component.translatable(TranslationKey.GUI_EMPTY.text())}; }
        Component fluidName = ITUtils.fluidDisplayName(selectedFluid);
        double rawValue = ITClientConfig.perTickTrashCans ? (double)lastAcceptedAmount / 20.0 : lastAcceptedAmount;
        String value = NUMBER_FORMAT.format(rawValue);
        return new Component[]{Component.translatable(text().text(), fluidName, value)};
    }

    @Override public void getBlockEntityDrop(@NotNull LootContext context, @NotNull Consumer<ItemStack> drop) {
        ItemStack stack = new ItemStack(getBlockState().getBlock(), 1);
        CompoundTag tag = new CompoundTag();
        HolderLookup.Provider lookupProvider = level!=null?level.registryAccess(): ITUtils.serverRegistryAccess();
        saveAdditional(tag, lookupProvider);
        if (!tag.isEmpty()) { ITUtils.setItemCustomTag(stack, tag); }
        drop.accept(stack);
    }

    @Override public void onBEPlaced(BlockPlaceContext ctx) { onBEPlaced(ctx.getItemInHand()); }

    public void setOutputFluid(FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            this.selectedFluid = FluidStack.EMPTY;
        } else {
            this.selectedFluid = fluidStack.copy();
            this.selectedFluid.setAmount(1);
        }
        setChanged();
    }

    @Override protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.saveAdditional(tag, lookupProvider);
        if (!selectedFluid.isEmpty()) { tag.put("SelectedFluid", selectedFluid.saveOptional(lookupProvider)); }
    }

    @Override protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.loadAdditional(tag, lookupProvider);
        if (tag.contains("SelectedFluid")) {
            selectedFluid = FluidStack.parseOptional(lookupProvider, tag.getCompound("SelectedFluid"));
        }
    }

    public void onBEPlaced(ItemStack stack) {
        CompoundTag tag = ITUtils.getItemCustomTag(stack);
        if (tag.contains("SelectedFluid")) {
            HolderLookup.Provider lookupProvider = level!=null?level.registryAccess(): ITUtils.serverRegistryAccess();
            selectedFluid = FluidStack.parseOptional(lookupProvider, tag.getCompound("SelectedFluid"));
        }
    }

    @Override public void invalidateCapabilities() {
        super.invalidateCapabilities();
        fluidHandler.invalidate();
    }
}
