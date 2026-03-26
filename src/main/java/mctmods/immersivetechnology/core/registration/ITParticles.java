package mctmods.immersivetechnology.core.registration;

import com.mojang.serialization.MapCodec;
import mctmods.immersivetechnology.client.particles.ColoredSmoke;
import mctmods.immersivetechnology.core.lib.ITLib;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ITParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ITLib.MODID);

    public static final RegistryObject<ParticleType<ColoredSmoke>> COLORED_SMOKE = REGISTER.register("colored_smoke", () -> new ParticleType<>(false) {
        @Override @NotNull public MapCodec<ColoredSmoke> codec() {return ColoredSmoke.CODEC;}

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, ColoredSmoke> streamCodec() {
            return ColoredSmoke.STREAM_CODEC;
        }
    });

    public static final RegistryObject<SimpleParticleType> SMOKE_CUSTOM = REGISTER.register("smoke_custom", () -> new SimpleParticleType(true));
}
