package mctmods.immersivetechnology.core.network;

import mctmods.immersivetechnology.common.blocks.metal.logic.OSDCommonBlockEntity;
import mctmods.immersivetechnology.common.blocks.metal.logic.ValveCommonBlockEntity;
import mctmods.immersivetechnology.core.lib.ITLib;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ITOSDRequestMessage(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ITOSDRequestMessage> TYPE = new Type<>(ITLib.rl("osd_request"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ITOSDRequestMessage> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buf, ITOSDRequestMessage message) -> message.write(buf),
            ITOSDRequestMessage::new
    );

    public ITOSDRequestMessage(RegistryFriendlyByteBuf buf) { this(buf.readBlockPos()); }

    private void write(RegistryFriendlyByteBuf buf) { buf.writeBlockPos(pos); }

    public static void handle(ITOSDRequestMessage message, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            Level level = player.level();
            BlockEntity te = level.getBlockEntity(message.pos);
            if (te instanceof OSDCommonBlockEntity trash) {
                context.reply(new ITOSDSyncMessage(message.pos, trash.lastAcceptedAmount, 0, 0));
            }
            if (te instanceof ValveCommonBlockEntity valve) {
                context.reply(new ITOSDSyncMessage(message.pos, valve.lastAcceptedAmount, valve.average, valve.packetAverage));
            }
        }
    }

    @Override
    public Type<ITOSDRequestMessage> type() {
        return TYPE;
    }
}
