package mctmods.immersivetechnology.core.network;

import mctmods.immersivetechnology.common.blocks.helper.ITBaseBlockEntity;
import mctmods.immersivetechnology.core.lib.ITLib;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ITMessageTileSync(BlockPos pos, CompoundTag nbt) implements CustomPacketPayload {
    public static final Type<ITMessageTileSync> TYPE = new Type<>(ITLib.rl("tile_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ITMessageTileSync> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buf, ITMessageTileSync message) -> message.write(buf),
            ITMessageTileSync::new
    );

    public ITMessageTileSync(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readNbt());
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeNbt(this.nbt);
    }

    public static void handle(ITMessageTileSync message, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            Level level = player.level();
            BlockEntity tile = level.getBlockEntity(message.pos);
            if (tile instanceof ITBaseBlockEntity itbe) itbe.receiveMessageFromClient(message.nbt);
        }
    }

    @Override
    public Type<ITMessageTileSync> type() {
        return TYPE;
    }
}
