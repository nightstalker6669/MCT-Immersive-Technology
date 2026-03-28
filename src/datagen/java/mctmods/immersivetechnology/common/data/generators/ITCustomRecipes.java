package mctmods.immersivetechnology.common.data.generators;

import blusunrize.immersiveengineering.api.IETags;
import mctmods.immersivetechnology.compat.ie.crafting.FluidTagInput;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import mctmods.immersivetechnology.compat.ie.crafting.builders.IEFinishedRecipe;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.DistillerRecipe;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.GasTurbineRecipe;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.builder.BoilerLiquidRecipeBuilder;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.builder.BoilerSolidRecipeBuilder;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.builder.BoilerTankRecipeBuilder;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.builder.GasTurbineRecipeBuilder;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.builder.HeatExchangerRecipeBuilder;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.builder.SolarMelterRecipeBuilder;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.builder.SolarTowerRecipeBuilder;
import mctmods.immersivetechnology.common.multiblocks.metal.recipe.builder.SteamTurbineRecipeBuilder;
import mctmods.immersivetechnology.common.multiblocks.stone.recipe.builder.CoolingTowerRecipeBuilder;
import mctmods.immersivetechnology.core.lib.ITLib;
import mctmods.immersivetechnology.core.registration.ITFluids;
import mctmods.immersivetechnology.core.registration.ITItems;
import mctmods.immersivetechnology.core.registration.ITTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ITCustomRecipes implements DataProvider {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private final PackOutput.PathProvider recipePathProvider;
    private final HashMap<String, Integer> pathCount = new HashMap<>();

    public ITCustomRecipes(PackOutput output) {
        this.recipePathProvider = output.createRegistryElementsPathProvider(net.minecraft.core.registries.Registries.RECIPE);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Map<ResourceLocation, JsonObject> recipes = new HashMap<>();
        Consumer<IEFinishedRecipe.GeneratedRecipe> consumer = generated -> {
            JsonObject previous = recipes.put(generated.id(), generated.json());
            if (previous != null) {
                throw new IllegalStateException("Duplicate recipe " + generated.id());
            }
        };

        recipesBoilerTank(consumer);
        recipesBoilerLiquid(consumer);
        recipesBoilerSolid(consumer);
        recipesCoolingTower(consumer);
        recipesDistiller(consumer);
        recipesHeatExchanger(consumer);
        recipesMixer(consumer);
        recipesTurbine(consumer);
        recipesSolarMelter(consumer);
        recipesSolarTower(consumer);

        CompletableFuture<?>[] futures = recipes.entrySet().stream()
                .map(entry -> saveRecipe(output, entry.getKey(), entry.getValue()))
                .toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futures);
    }

    private CompletableFuture<?> saveRecipe(CachedOutput output, ResourceLocation id, JsonObject json) {
        return CompletableFuture.runAsync(() -> {
            try {
                String jsonText = GSON.toJson(json);
                byte[] bytes = jsonText.getBytes(StandardCharsets.UTF_8);
                output.writeIfNeeded(recipePathProvider.json(id), bytes, Hashing.sha256().hashBytes(bytes));
            } catch (IOException e) {
                throw new RuntimeException("Failed to save recipe " + id, e);
            }
        });
    }

    private void recipesBoilerTank(@NotNull Consumer<IEFinishedRecipe.GeneratedRecipe> out) {
        BoilerTankRecipeBuilder.builder(FluidTags.WATER, 250).addOutput(ITFluids.STEAM.getStill(), 450).setTime(10).setRequiredHeat(100.0).build(out, toResourceLocation("boiler_tank/water"));
        BoilerTankRecipeBuilder.builder(ITTags.fluidDistilledWater, 250).addOutput(ITFluids.STEAM.getStill(), 500).setTime(10).setRequiredHeat(100.0).build(out, toResourceLocation("boiler_tank/distilled_water"));
    }

    private void recipesBoilerLiquid(@NotNull Consumer<IEFinishedRecipe.GeneratedRecipe> out) {
        BoilerLiquidRecipeBuilder.builder().addInput(IETags.fluidBiodiesel, 10).setTime(10).setHeatPerTick(0.1).setTargetHeat(100.0).build(out, toResourceLocation("boiler_liquid/biodiesel"));
        BoilerLiquidRecipeBuilder.builder().addInput(FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", "gasoline")), 50).setTime(10).setHeatPerTick(0.1).setTargetHeat(100.0).build(out, toResourceLocation("boiler_liquid/gasoline"));
        BoilerLiquidRecipeBuilder.builder().addInput(FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", "diesel")), 7).setTime(10).setHeatPerTick(0.1).setTargetHeat(100.0).build(out, toResourceLocation("boiler_liquid/diesel"));
        BoilerLiquidRecipeBuilder.builder().addInput(FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", "kerosene")), 9).setTime(10).setHeatPerTick(0.1).setTargetHeat(100.0).build(out, toResourceLocation("boiler_liquid/kerosene"));
    }

    private void recipesBoilerSolid(@NotNull Consumer<IEFinishedRecipe.GeneratedRecipe> out) {
        BoilerSolidRecipeBuilder.builder().addInput(ItemTags.COALS, 1).setHeatPerTick(0.1).setTargetHeat(100.0).build(out, toResourceLocation("boiler_solid/coal"));
        BoilerSolidRecipeBuilder.builder().addInput(IETags.coalCoke, 1).setHeatPerTick(0.1).setTargetHeat(100.0).build(out, toResourceLocation("boiler_solid/coal_coke"));
    }

    private void recipesCoolingTower(@NotNull Consumer<IEFinishedRecipe.GeneratedRecipe> out) {
        CoolingTowerRecipeBuilder.builder().addInput(FluidTags.WATER, 1000).addInput(ITTags.fluidExhaustSteam, 900).addOutput(Fluids.WATER, 500).addOutput(Fluids.WATER, 500).addOutput(Fluids.WATER, 500).setTime(3).build(out, toResourceLocation("cooling_tower/water"));
    }

    private void recipesDistiller(@NotNull Consumer<IEFinishedRecipe.GeneratedRecipe> out) {
        ItemStack salt = new ItemStack(ITItems.SALT.get(), 1);
        out.accept(buildJsonRecipe(toResourceLocation("distiller/water"), DistillerRecipe.SERIALIZER.get(), json -> {
            json.add("input", new FluidTagInput(FluidTags.WATER, 1000).serialize());
            json.add("result", fluidStackJson(new FluidStack(ITFluids.DISTILLED_WATER.getStill(), 500)));
            json.addProperty("time", 20);
            json.addProperty("energy", 10000);

            JsonObject itemStackJson = new JsonObject();
            itemStackJson.addProperty("item", BuiltInRegistries.ITEM.getKey(salt.getItem()).toString());
            if (salt.getCount() > 1) {
                itemStackJson.addProperty("count", salt.getCount());
            }

            JsonObject itemOutputJson = new JsonObject();
            itemOutputJson.add("item", itemStackJson);
            itemOutputJson.addProperty("chance", 0.5f);
            json.add("item_output", itemOutputJson);
        }));
    }

    private void recipesHeatExchanger(@NotNull Consumer<IEFinishedRecipe.GeneratedRecipe> out) {
        HeatExchangerRecipeBuilder.builder(new FluidTagInput(FluidTags.WATER, 250), new FluidTagInput(ITTags.fluidFlueGas, 1000), new FluidStack(ITFluids.STEAM.getStill(), 450), null, 640, 10).build(out, toResourceLocation("heat_exchanger/water_fluegas"));
        HeatExchangerRecipeBuilder.builder(new FluidTagInput(ITTags.fluidDistilledWater, 250), new FluidTagInput(ITTags.fluidFlueGas, 1000), new FluidStack(ITFluids.STEAM.getStill(), 500), null, 640, 10).build(out, toResourceLocation("heat_exchanger/distwater_fluegas"));
        HeatExchangerRecipeBuilder.builder(new FluidTagInput(FluidTags.WATER, 250), new FluidTagInput(ITTags.fluidMoltenSalt, 80), new FluidStack(ITFluids.STEAM.getStill(), 450), new FluidStack(ITFluids.HEATED_SALT.getStill(), 80), 640, 10).build(out, toResourceLocation("heat_exchanger/water_moltensalt"));
        HeatExchangerRecipeBuilder.builder(new FluidTagInput(ITTags.fluidDistilledWater, 250), new FluidTagInput(ITTags.fluidMoltenSalt, 80), new FluidStack(ITFluids.STEAM.getStill(), 500), new FluidStack(ITFluids.HEATED_SALT.getStill(), 80), 640, 10).build(out, toResourceLocation("heat_exchanger/distwater_moltensalt"));
    }

    private void recipesMixer(@NotNull Consumer<IEFinishedRecipe.GeneratedRecipe> out) {
        out.accept(mixerRecipe(
                toResourceLocation("mixer/salt_slurry"),
                new FluidTagInput(FluidTags.WATER, 1000),
                new IngredientWithSize(ITTags.saltForge, 4),
                new FluidStack(ITFluids.SALT_SLURRY.getStill(), 1000),
                3200
        ));
        out.accept(mixerRecipe(
                toResourceLocation("mixer/gravel_slurry"),
                new FluidTagInput(FluidTags.WATER, 1000),
                new IngredientWithSize(net.minecraft.world.item.crafting.Ingredient.of(Blocks.GRAVEL), 4),
                new FluidStack(ITFluids.GRAVEL_SLURRY.getStill(), 1000),
                3200
        ));
    }

    private void recipesTurbine(@NotNull Consumer<IEFinishedRecipe.GeneratedRecipe> out) {
        SteamTurbineRecipeBuilder.builder().addInput(ITTags.fluidSteam, 100).addOutput(ITFluids.EXHAUST_STEAM.getStill(), 100).setTime(1).build(out, toResourceLocation("steam_turbine/steam"));
        SteamTurbineRecipeBuilder.builder().addInput(ITTags.fluidSteamForge, 100).addOutput(ITFluids.EXHAUST_STEAM.getStill(), 100).setTime(1).build(out, toResourceLocation("steam_turbine/steam_forge"));
        GasTurbineRecipeBuilder.builder().addInput(IETags.fluidBiodiesel, 160).addOutput(ITFluids.FLUE_GAS.getStill(), 1000).setTime(10).build(out, toResourceLocation("gas_turbine/biodiesel"));
        out.accept(withConditions(
                buildJsonRecipe(toResourceLocation("gas_turbine/gasoline"), GasTurbineRecipe.SERIALIZER.get(), json -> {
                    json.add("input", new FluidTagInput(FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", "gasoline")), 800).serialize());
                    json.add("output", fluidStackJson(new FluidStack(ITFluids.FLUE_GAS.getStill(), 1000)));
                    json.addProperty("time", 10);
                }),
                modLoaded("immersivepetroleum")
        ));
        out.accept(withConditions(
                buildJsonRecipe(toResourceLocation("gas_turbine/diesel"), GasTurbineRecipe.SERIALIZER.get(), json -> {
                    json.add("input", new FluidTagInput(FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", "diesel")), 114).serialize());
                    json.add("output", fluidStackJson(new FluidStack(ITFluids.FLUE_GAS.getStill(), 1000)));
                    json.addProperty("time", 10);
                }),
                modLoaded("immersivepetroleum")
        ));
        out.accept(withConditions(
                buildJsonRecipe(toResourceLocation("gas_turbine/kerosene"), GasTurbineRecipe.SERIALIZER.get(), json -> {
                    json.add("input", new FluidTagInput(FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", "kerosene")), 150).serialize());
                    json.add("output", fluidStackJson(new FluidStack(ITFluids.FLUE_GAS.getStill(), 1000)));
                    json.addProperty("time", 10);
                }),
                modLoaded("immersivepetroleum")
        ));
    }

    private void recipesSolarMelter(@NotNull Consumer<IEFinishedRecipe.GeneratedRecipe> out) {
        SolarMelterRecipeBuilder.builder().addInput(ITTags.fluidHeatedSaltSlurry, 1000).addOutput(ITFluids.MOLTEN_SALT.getStill(), 500).setTime(20).setRequiredTemp(1000.0).build(out, toResourceLocation("solar_melter/heated_salt"));
        SolarMelterRecipeBuilder.builder().addInput(ITTags.fluidHeatedGravelSlurry, 1000).addOutput(Fluids.LAVA, 500).setTime(20).setRequiredTemp(1000.0).build(out, toResourceLocation("solar_melter/heated_gravel_slurry"));
    }

    private void recipesSolarTower(@NotNull Consumer<IEFinishedRecipe.GeneratedRecipe> out) {
        SolarTowerRecipeBuilder.builder().addInput(FluidTags.WATER, 250).addOutput(ITFluids.STEAM.getStill(), 450).setTime(10).setRequiredTemp(100.0).build(out, toResourceLocation("solar_tower/water"));
        SolarTowerRecipeBuilder.builder().addInput(ITTags.fluidDistilledWater, 250).addOutput(ITFluids.STEAM.getStill(), 500).setTime(10).setRequiredTemp(100.0).build(out, toResourceLocation("solar_tower/distilled_water"));
        SolarTowerRecipeBuilder.builder().addInput(ITTags.fluidSaltSlurry, 1000).addOutput(ITFluids.HEATED_SALT.getStill(), 500).setTime(10).setRequiredTemp(400.0).build(out, toResourceLocation("solar_tower/salt_slurry"));
        SolarTowerRecipeBuilder.builder().addInput(ITTags.fluidGravelSlurry, 1000).addOutput(ITFluids.HEATED_GRAVEL.getStill(), 500).setTime(10).setRequiredTemp(400.0).build(out, toResourceLocation("solar_tower/gravel_slurry"));
    }

    private IEFinishedRecipe.GeneratedRecipe mixerRecipe(ResourceLocation id, FluidTagInput fluidInput, IngredientWithSize itemInput, FluidStack result, int energy) {
        return buildJsonRecipe(id, blusunrize.immersiveengineering.api.crafting.MixerRecipe.SERIALIZER.get(), json -> {
            json.add("fluid", fluidInput.serialize());
            JsonArray inputs = new JsonArray();
            inputs.add(itemInput.serialize());
            json.add("inputs", inputs);
            json.add("result", fluidStackJson(result));
            json.addProperty("energy", energy);
        });
    }

    private IEFinishedRecipe.GeneratedRecipe buildJsonRecipe(ResourceLocation id, net.minecraft.world.item.crafting.RecipeSerializer<?> serializer, Consumer<JsonObject> writer) {
        JsonObject json = new JsonObject();
        json.addProperty("type", BuiltInRegistries.RECIPE_SERIALIZER.getKey(serializer).toString());
        json.addProperty("id", id.toString());
        writer.accept(json);
        return new IEFinishedRecipe.GeneratedRecipe(id, json);
    }

    private IEFinishedRecipe.GeneratedRecipe withConditions(IEFinishedRecipe.GeneratedRecipe recipe, ICondition... conditions) {
        JsonObject json = recipe.json().deepCopy();
        JsonArray conditionArray = new JsonArray();
        for (ICondition ignored : conditions) {
            if (ignored instanceof ModLoadedCondition modLoadedCondition) {
                JsonObject conditionJson = new JsonObject();
                conditionJson.addProperty("type", "neoforge:mod_loaded");
                conditionJson.addProperty("modid", modLoadedCondition.modid());
                conditionArray.add(conditionJson);
            }
        }
        if (!conditionArray.isEmpty()) {
            json.add("neoforge:conditions", conditionArray);
        }
        return new IEFinishedRecipe.GeneratedRecipe(recipe.id(), json);
    }

    private JsonObject fluidStackJson(FluidStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("id", BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString());
        json.addProperty("amount", stack.getAmount());
        return json;
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

    private static ICondition modLoaded(String modId) {
        return new ModLoadedCondition(modId);
    }

    @Override
    public @NotNull String getName() {
        return "Immersive Technology Custom Recipes";
    }
}
