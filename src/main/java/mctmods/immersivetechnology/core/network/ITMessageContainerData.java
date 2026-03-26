package mctmods.immersivetechnology.core.network;

import com.mojang.datafixers.util.Pair;
import mctmods.immersivetechnology.common.gui.helper.ITGenericDataSerializers;
import mctmods.immersivetechnology.common.gui.helper.ITGenericDataSerializers.DataPair;
import mctmods.immersivetechnology.common.gui.helper.ITContainerMenu;
import mctmods.immersivetechnology.core.lib.ITLib;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.world.inventory.AbstractContainerMenu;
import java.util.ArrayList;
import java.util.List;

public record ITMessageContainerData(List<Pair<Integer, DataPair<?>>> synced) implements CustomPacketPayload {
    public static final Type<ITMessageContainerData> TYPE = new Type<>(ITLib.rl("container_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ITMessageContainerData> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buf, ITMessageContainerData message) -> message.write(buf),
            ITMessageContainerData::new
    );

    public ITMessageContainerData(RegistryFriendlyByteBuf buf) { this(readSynced(buf)); }

    private static List<Pair<Integer, DataPair<?>>> readSynced(RegistryFriendlyByteBuf buf) {
        int size = buf.readInt();
        List<Pair<Integer, DataPair<?>>> synced = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int index = buf.readVarInt();
            DataPair<?> dataPair = ITGenericDataSerializers.read(buf);
            synced.add(Pair.of(index, dataPair));
        }
        return synced;
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(synced.size());
        for (Pair<Integer, DataPair<?>> pair : synced) {
            buf.writeVarInt(pair.getFirst());
            pair.getSecond().write(buf);
        }
    }

    public static void handle(ITMessageContainerData message, IPayloadContext context) {
        AbstractContainerMenu currentContainer = context.player().containerMenu;
        if (currentContainer instanceof ITContainerMenu itContainer) itContainer.receiveSync(message.synced);
    }

    @Override
    public Type<ITMessageContainerData> type() {
        return TYPE;
    }
}
