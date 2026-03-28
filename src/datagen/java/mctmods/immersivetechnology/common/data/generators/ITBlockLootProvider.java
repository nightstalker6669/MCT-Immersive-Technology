package mctmods.immersivetechnology.common.data.generators;

import mctmods.immersivetechnology.core.util.loot.ITBEDropLootEntry;
import mctmods.immersivetechnology.core.registration.ITBlocks;
import mctmods.immersivetechnology.core.registration.ITMultiblockProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class ITBlockLootProvider extends BlockLootSubProvider {
    public ITBlockLootProvider(HolderLookup.Provider provider) { super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider); }

    @Override protected void generate() {
        registerEntity(ITBlocks.Metal.BARREL_CREATIVE.getRegObject());
        registerEntity(ITBlocks.Metal.BARREL_OPEN.getRegObject());
        registerEntity(ITBlocks.Metal.BARREL_STEEL.getRegObject());

        dropSelf(ITBlocks.Wooden.CRATE_CREATIVE.get());
        dropSelf(ITBlocks.Metal.ADVANCED_COKE_OVEN_BASEHEATER.get());
        dropSelf(ITBlocks.Metal.HEAT_CREATIVE.get());
        dropSelf(ITBlocks.Metal.ROTOR_CREATIVE.get());
        dropSelf(ITBlocks.Metal.TRASH_ENERGY.get());
        dropSelf(ITBlocks.Metal.TRASH_FLUID.get());
        dropSelf(ITBlocks.Metal.TRASH_ITEM.get());
        dropSelf(ITBlocks.Metal.VALVE_FLUID.get());
        dropSelf(ITBlocks.Metal.VALVE_LIMITER.get());
        dropSelf(ITBlocks.Metal.VALVE_LOAD.get());
        dropSelf(ITBlocks.Metal.TECHNOLOGY_ENGINEERING.get());
        dropSelf(ITBlocks.Stone.REINFORCED_COKE_BRICK.get());
        dropSelf(ITBlocks.Stone.SLAB_REINFORCED_COKE_BRICK.get());

        registerMultiblocksNoDrop();

    }

    private void registerEntity(Supplier<? extends Block> block) {
        LootPool.Builder pool = createPoolBuilder().add(ITBEDropLootEntry.builder());
        add(block.get(), LootTable.lootTable().withPool(pool));
    }

    private void registerMultiblocksNoDrop() {
        add(ITMultiblockProvider.ADVANCED_COKE_OVEN.block().get(), emptyLootTable());
        add(ITMultiblockProvider.ALTERNATOR.block().get(), emptyLootTable());
        add(ITMultiblockProvider.BOILER_LIQUID.block().get(), emptyLootTable());
        add(ITMultiblockProvider.BOILER_SOLID.block().get(), emptyLootTable());
        add(ITMultiblockProvider.BOILER_TANK.block().get(), emptyLootTable());
        add(ITMultiblockProvider.COOLING_TOWER.block().get(), emptyLootTable());
        add(ITMultiblockProvider.DISTILLER.block().get(), emptyLootTable());
        add(ITMultiblockProvider.GAS_TURBINE.block().get(), emptyLootTable());
        add(ITMultiblockProvider.HEAT_EXCHANGER.block().get(), emptyLootTable());
        add(ITMultiblockProvider.SOLAR_MELTER.block().get(), emptyLootTable());
        add(ITMultiblockProvider.SOLAR_REFLECTOR.block().get(), emptyLootTable());
        add(ITMultiblockProvider.SOLAR_TOWER.block().get(), emptyLootTable());
        add(ITMultiblockProvider.STEAM_TURBINE.block().get(), emptyLootTable());
        add(ITMultiblockProvider.STEEL_SHEETMETAL_TANK.block().get(), emptyLootTable());
    }

    private LootPool.Builder createPoolBuilder() { return LootPool.lootPool().when(ExplosionCondition.survivesExplosion()); }

    private LootTable.Builder emptyLootTable() {
        return LootTable.lootTable();
    }

    @Override
    @NotNull
    protected Set<Block> getKnownBlocks() {
        Stream<Block> registeredBlocks = ITBlocks.REGISTER.getEntries().stream().map(Supplier::get);
        Stream<Block> multiblockBlocks = Stream.of(
                ITMultiblockProvider.ADVANCED_COKE_OVEN.block().get(),
                ITMultiblockProvider.ALTERNATOR.block().get(),
                ITMultiblockProvider.BOILER_LIQUID.block().get(),
                ITMultiblockProvider.BOILER_SOLID.block().get(),
                ITMultiblockProvider.BOILER_TANK.block().get(),
                ITMultiblockProvider.COOLING_TOWER.block().get(),
                ITMultiblockProvider.DISTILLER.block().get(),
                ITMultiblockProvider.GAS_TURBINE.block().get(),
                ITMultiblockProvider.HEAT_EXCHANGER.block().get(),
                ITMultiblockProvider.SOLAR_MELTER.block().get(),
                ITMultiblockProvider.SOLAR_REFLECTOR.block().get(),
                ITMultiblockProvider.SOLAR_TOWER.block().get(),
                ITMultiblockProvider.STEAM_TURBINE.block().get(),
                ITMultiblockProvider.STEEL_SHEETMETAL_TANK.block().get()
        );
        return Stream.of(registeredBlocks, multiblockBlocks)
                .flatMap(stream -> stream)
                .collect(Collectors.toSet());
    }
}
