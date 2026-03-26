package mctmods.immersivetechnology.client.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mctmods.immersivetechnology.core.registration.ITParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class ColoredSmoke implements ParticleOptions {
    public static final MapCodec<ColoredSmoke> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.x()),
            Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.y()),
            Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.z()),
            Codec.BOOL.optionalFieldOf("collide_horizontal", false).forGetter(d -> d.collideHorizontal),
            Codec.BOOL.optionalFieldOf("collide_vertical", false).forGetter(d -> d.collideVertical)
    ).apply(inst, ColoredSmoke::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ColoredSmoke> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ColoredSmoke decode(RegistryFriendlyByteBuf buf) {
            return new ColoredSmoke(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ColoredSmoke value) {
            buf.writeFloat(value.color.x());
            buf.writeFloat(value.color.y());
            buf.writeFloat(value.color.z());
            buf.writeBoolean(value.collideHorizontal);
            buf.writeBoolean(value.collideVertical);
        }
    };

    public final Vector3f color;
    public final boolean collideHorizontal;
    public final boolean collideVertical;

    public ColoredSmoke(float r, float g, float b) { this(r, g, b, false, false); }

    public ColoredSmoke(float r, float g, float b, boolean collideHorizontal, boolean collideVertical) {
        this.color = new Vector3f(r, g, b);
        this.collideHorizontal = collideHorizontal;
        this.collideVertical = collideVertical;
    }

    @Override @NotNull public ParticleType<?> getType() { return ITParticles.COLORED_SMOKE.get(); }
}
