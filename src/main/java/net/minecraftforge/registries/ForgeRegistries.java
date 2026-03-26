package net.minecraftforge.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ForgeRegistries {
    public static final Registry<Block> BLOCKS = BuiltInRegistries.BLOCK;
    public static final Registry<Item> ITEMS = BuiltInRegistries.ITEM;
    public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = BuiltInRegistries.BLOCK_ENTITY_TYPE;
    public static final Registry<Fluid> FLUIDS = BuiltInRegistries.FLUID;
    public static final Registry<MenuType<?>> MENU_TYPES = BuiltInRegistries.MENU;
    public static final Registry<ParticleType<?>> PARTICLE_TYPES = BuiltInRegistries.PARTICLE_TYPE;
    public static final Registry<RecipeSerializer<?>> RECIPE_SERIALIZERS = BuiltInRegistries.RECIPE_SERIALIZER;

    private ForgeRegistries() {}

    public static final class Keys {
        public static final ResourceKey<Registry<FluidType>> FLUID_TYPES = NeoForgeRegistries.Keys.FLUID_TYPES;

        private Keys() {}
    }
}
