package mctmods.immersivetechnology.common.multiblocks.stone.process;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessInMachine;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import blusunrize.immersiveengineering.common.register.IEFluids;
import mctmods.immersivetechnology.common.multiblocks.helper.ITFurnaceHandler;
import mctmods.immersivetechnology.common.multiblocks.stone.logic.AdvancedCokeOvenLogic;
import mctmods.immersivetechnology.common.multiblocks.stone.recipe.AdvancedCokeOvenRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import java.util.function.BiFunction;

public class AdvancedCokeOvenProcess extends MultiblockProcessInMachine<AdvancedCokeOvenRecipe> {
    private float processTick;
    private final int maxProcessTime;

    public AdvancedCokeOvenProcess(AdvancedCokeOvenRecipe recipe) { super(new RecipeHolder<>(recipe.id(), recipe), 0); this.maxProcessTime = recipe.getTotalProcessTime(); }

    @SuppressWarnings("unused")
    public AdvancedCokeOvenProcess(BiFunction<Level, ResourceLocation, AdvancedCokeOvenRecipe> getRecipe, CompoundTag data, HolderLookup.Provider provider) { super(getRecipe, data); this.processTick = data.getFloat("processTick"); this.maxProcessTime = data.getInt("maxProcessTime"); }

    @Override public void writeExtraDataToNBT(CompoundTag nbt, HolderLookup.Provider provider) { super.writeExtraDataToNBT(nbt, provider); nbt.putFloat("processTick", processTick); nbt.putInt("maxProcessTime", maxProcessTime); }

    @Override public void doProcessTick(ProcessContext.ProcessContextInMachine<AdvancedCokeOvenRecipe> context, IMultiblockLevel level) {
        if (getRecipe(level.getRawLevel()) == null) { this.clearProcess = true; return; }
        @SuppressWarnings("unchecked")
        ITFurnaceHandler.IFurnaceEnvironment<AdvancedCokeOvenRecipe> env = (ITFurnaceHandler.IFurnaceEnvironment<AdvancedCokeOvenRecipe>) context;
        double speed = env.getProcessSpeed(level);
        this.processTick += (float)speed;
        if (this.processTick >= this.maxProcessTime) { processFinish(context, level); this.clearProcess = true; }
    }

    @Override public boolean canProcess(ProcessContext.ProcessContextInMachine<AdvancedCokeOvenRecipe> context, Level level) { return true; }

    @Override protected void processFinish(ProcessContext.ProcessContextInMachine<AdvancedCokeOvenRecipe> context, IMultiblockLevel level) {
        AdvancedCokeOvenRecipe recipe = getRecipe(level.getRawLevel());
        if (recipe != null) {
            ItemStack input = context.getInventory().getStackInSlot(inputSlots[0]);
            input.shrink(recipe.input.getCount());
            ItemStack out = recipe.itemOutput.get().copy();
            ItemStack current = context.getInventory().getStackInSlot(AdvancedCokeOvenLogic.SLOT_OUTPUT);
            if (current.isEmpty()) { context.getInventory().setStackInSlot(AdvancedCokeOvenLogic.SLOT_OUTPUT, out); }
            else if (ItemStack.isSameItemSameComponents(current, out) && current.getCount() + out.getCount() <= current.getMaxStackSize()) { current.grow(out.getCount()); }
            FluidStack fluidOut = new FluidStack(IEFluids.CREOSOTE.getStill(), recipe.creosoteOutput);
            context.getInternalTanks()[0].fill(fluidOut.copy(), FluidAction.EXECUTE);
        }
    }

    public int getCurrentProcessTime() { return (int)processTick; }

    public int getMaxProcessTime() { return maxProcessTime; }
}

