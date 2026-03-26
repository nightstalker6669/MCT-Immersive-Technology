package mctmods.immersivetechnology.core.network;

import mctmods.immersivetechnology.common.blocks.metal.logic.OSDCommonBlockEntity;
import mctmods.immersivetechnology.common.blocks.metal.logic.ValveCommonBlockEntity;
import mctmods.immersivetechnology.core.lib.ITLib;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ITOSDSyncMessage(BlockPos pos, long lastAccepted, long average, int packetAverage) implements CustomPacketPayload {
    public static final Type<ITOSDSyncMessage> TYPE = new Type<>(ITLib.rl("osd_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ITOSDSyncMessage> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buf, ITOSDSyncMessage message) -> message.write(buf),
            ITOSDSyncMessage::new
    );

    public ITOSDSyncMessage(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readLong(), buf.readLong(), buf.readInt());
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeLong(lastAccepted);
        buf.writeLong(average);
        buf.writeInt(packetAverage);
    }

    public static void handle(ITOSDSyncMessage message, IPayloadContext context) {
        BlockEntity te = context.player().level().getBlockEntity(message.pos);
        if (te instanceof OSDCommonBlockEntity osd) {
            osd.lastAcceptedAmount = message.lastAccepted;
        }
        if (te instanceof ValveCommonBlockEntity valve) {
            valve.lastAcceptedAmount = message.lastAccepted;
            valve.average = message.average;
            valve.packetAverage = message.packetAverage;
        }
    }

    @Override
    public Type<ITOSDSyncMessage> type() {
        return TYPE;
    }
}
