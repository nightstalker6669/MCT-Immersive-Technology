package mctmods.immersivetechnology.common.data.generators;

import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEItems;
import mctmods.immersivetechnology.core.lib.ITLib;
import mctmods.immersivetechnology.core.registration.ITBlocks;
import mctmods.immersivetechnology.core.registration.ITItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ITRecipes extends RecipeProvider {
    private final HashMap<String, Integer> pathCount = new HashMap<>();

    public ITRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output, HolderLookup.@NotNull Provider provider) {
        itemRecipes(output);
    }

    private void itemRecipes(@Nonnull RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ITBlocks.Stone.REINFORCED_COKE_BRICK.get().asItem(), 1)
                .define('P', IETags.getTagsFor(EnumMetals.STEEL).plate)
                .define('C', IEBlocks.StoneDecoration.COKEBRICK.get())
                .pattern("P")
                .pattern("C")
                .unlockedBy("has_steel_plate", has(IETags.getTagsFor(EnumMetals.STEEL).plate))
                .save(output, toResourceLocation("reinforced_coke_brick"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ITBlocks.Stone.REINFORCED_COKE_BRICK.get().asItem(), 1)
                .define('S', ITBlocks.Stone.SLAB_REINFORCED_COKE_BRICK.get())
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_slab_reinforced_coke_brick", has(ITBlocks.Stone.SLAB_REINFORCED_COKE_BRICK.get()))
                .save(output, toResourceLocation("reinforced_coke_brick_slab_back"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ITBlocks.Stone.SLAB_REINFORCED_COKE_BRICK.get().asItem(), 6)
                .define('B', ITBlocks.Stone.REINFORCED_COKE_BRICK.get())
                .pattern("BBB")
                .unlockedBy("has_reinforced_coke_brick", has(ITBlocks.Stone.REINFORCED_COKE_BRICK.get()))
                .save(output, toResourceLocation("reinforced_coke_brick_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ITBlocks.Metal.TECHNOLOGY_ENGINEERING.get().asItem(), 4)
                .define('C', IETags.getItemTag(IETags.getTagsFor(EnumMetals.COPPER).sheetmetal))
                .define('S', Ingredient.of(IEItems.Ingredients.COMPONENT_STEEL.get()))
                .define('E', IETags.getTagsFor(EnumMetals.ELECTRUM).ingot)
                .pattern("CSC")
                .pattern("SES")
                .pattern("CSC")
                .unlockedBy("has_copper_sheetmetal", has(IETags.getItemTag(IETags.getTagsFor(EnumMetals.COPPER).sheetmetal)))
                .save(output, toResourceLocation("technology_engineering"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ITBlocks.Metal.BARREL_OPEN.get().asItem(), 1)
                .define('S', Ingredient.of(IEBlocks.TO_SLAB.get(IEBlocks.Metals.SHEETMETAL.get(EnumMetals.IRON).getId()).get()))
                .define('B', Ingredient.of(IEBlocks.Metals.SHEETMETAL.get(EnumMetals.IRON).get()))
                .pattern("S S")
                .pattern("B B")
                .pattern("BBB")
                .unlockedBy("has_slab_sheetmetal_iron", has(IEBlocks.TO_SLAB.get(IEBlocks.Metals.SHEETMETAL.get(EnumMetals.IRON).getId()).get()))
                .save(output, toResourceLocation("barrel_open"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ITBlocks.Metal.BARREL_STEEL.get().asItem(), 1)
                .define('S', Ingredient.of(IEBlocks.TO_SLAB.get(IEBlocks.Metals.SHEETMETAL.get(EnumMetals.STEEL).getId()).get()))
                .define('B', Ingredient.of(IEBlocks.Metals.SHEETMETAL.get(EnumMetals.STEEL).get()))
                .pattern("SSS")
                .pattern("B B")
                .pattern("BBB")
                .unlockedBy("has_slab_sheetmetal_steel", has(IEBlocks.TO_SLAB.get(IEBlocks.Metals.SHEETMETAL.get(EnumMetals.STEEL).getId()).get()))
                .save(output, toResourceLocation("barrel_steel"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ITBlocks.Metal.TRASH_ITEM.get().asItem(), 1)
                .define('P', IETags.getTagsFor(EnumMetals.IRON).plate)
                .define('C', net.neoforged.neoforge.common.Tags.Items.CHESTS_WOODEN)
                .define('S', IETags.getItemTag(IETags.getTagsFor(EnumMetals.IRON).sheetmetal))
                .pattern("PPP")
                .pattern("PCP")
                .pattern(" S ")
                .unlockedBy("has_iron_plate", has(IETags.getTagsFor(EnumMetals.IRON).plate))
                .save(output, toResourceLocation("trash_item"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ITBlocks.Metal.TRASH_FLUID.get().asItem(), 1)
                .define('P', IETags.getTagsFor(EnumMetals.IRON).plate)
                .define('B', IEBlocks.MetalDevices.FLUID_PUMP.get())
                .define('S', IETags.getItemTag(IETags.getTagsFor(EnumMetals.IRON).sheetmetal))
                .pattern("PPP")
                .pattern("PBP")
                .pattern(" S ")
                .unlockedBy("has_iron_plate", has(IETags.getTagsFor(EnumMetals.IRON).plate))
                .save(output, toResourceLocation("trash_fluid"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ITBlocks.Metal.TRASH_ENERGY.get().asItem(), 1)
                .define('P', IETags.getTagsFor(EnumMetals.IRON).plate)
                .define('C', IEBlocks.MetalDecoration.HV_COIL.get())
                .define('S', IETags.getItemTag(IETags.getTagsFor(EnumMetals.IRON).sheetmetal))
                .pattern("PPP")
                .pattern("PCP")
                .pattern(" S ")
                .unlockedBy("has_iron_plate", has(IETags.getTagsFor(EnumMetals.IRON).plate))
                .save(output, toResourceLocation("trash_energy"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ITBlocks.Metal.VALVE_FLUID.get().asItem(), 2)
                .define('L', IETags.getTagsFor(EnumMetals.IRON).plate)
                .define('R', IEBlocks.Connectors.CONNECTOR_REDSTONE.get())
                .define('P', IEBlocks.MetalDevices.FLUID_PIPE.get())
                .define('I', IEItems.Ingredients.COMPONENT_IRON.get())
                .define('C', IEItems.Ingredients.CIRCUIT_BOARD.get())
                .pattern("LRL")
                .pattern("PIP")
                .pattern("LCL")
                .unlockedBy("has_iron_plate", has(IETags.getTagsFor(EnumMetals.IRON).plate))
                .save(output, toResourceLocation("valve_fluid"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ITBlocks.Metal.VALVE_LIMITER.get().asItem(), 2)
                .define('L', IETags.getTagsFor(EnumMetals.IRON).plate)
                .define('R', IEBlocks.Connectors.CONNECTOR_REDSTONE.get())
                .define('P', Objects.requireNonNull(BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("immersiveengineering", "conveyor_basic"))))
                .define('I', IEItems.Ingredients.COMPONENT_IRON.get())
                .define('C', IEItems.Ingredients.CIRCUIT_BOARD.get())
                .pattern("LRL")
                .pattern("PIP")
                .pattern("LCL")
                .unlockedBy("has_iron_plate", has(IETags.getTagsFor(EnumMetals.IRON).plate))
                .save(output, toResourceLocation("valve_limiter"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ITBlocks.Metal.VALVE_LOAD.get().asItem(), 2)
                .define('L', IETags.getTagsFor(EnumMetals.IRON).plate)
                .define('R', IEBlocks.Connectors.CONNECTOR_REDSTONE.get())
                .define('P', IEBlocks.Connectors.getEnergyConnector("HV", false).get())
                .define('I', IEItems.Ingredients.COMPONENT_IRON.get())
                .define('C', IEItems.Ingredients.CIRCUIT_BOARD.get())
                .pattern("LRL")
                .pattern("PIP")
                .pattern("LCL")
                .unlockedBy("has_iron_plate", has(IETags.getTagsFor(EnumMetals.IRON).plate))
                .save(output, toResourceLocation("valve_load"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ITBlocks.Metal.ADVANCED_COKE_OVEN_BASEHEATER.get(), 1)
                .define('S', IETags.getItemTag(IETags.getTagsFor(EnumMetals.IRON).sheetmetal))
                .define('H', IEBlocks.MetalDevices.FURNACE_HEATER.get())
                .define('R', Items.REDSTONE)
                .pattern("SSS")
                .pattern("HRH")
                .pattern("SSS")
                .unlockedBy("has_furnace_heater", has(IEBlocks.MetalDevices.FURNACE_HEATER.get()))
                .save(output, toResourceLocation("advanced_coke_oven_baseheater"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ITItems.FORMATION_TOOL.get(), 1)
                .define('I', IETags.getTagsFor(EnumMetals.IRON).ingot)
                .define('E', IETags.getTagsFor(EnumMetals.ELECTRUM).ingot)
                .pattern(" I ")
                .pattern(" EI")
                .pattern("I  ")
                .unlockedBy("has_iron_ingot", has(IETags.getTagsFor(EnumMetals.IRON).ingot))
                .save(output, toResourceLocation("formation_tool"));
    }

    private ResourceLocation toResourceLocation(String resourceLocation) {
        if (!resourceLocation.contains("/")) {
            resourceLocation = "crafting/" + resourceLocation;
        }
        if (pathCount.containsKey(resourceLocation)) {
            int count = pathCount.get(resourceLocation) + 1;
            pathCount.put(resourceLocation, count);
            return ITLib.rl(resourceLocation + count);
        }
        pathCount.put(resourceLocation, 1);
        return ITLib.rl(resourceLocation);
    }
}
