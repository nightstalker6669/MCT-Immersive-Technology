package mctmods.immersivetechnology.core.network;

import mctmods.immersivetechnology.common.gui.helper.ITContainerMenu;
import mctmods.immersivetechnology.core.lib.ITLib;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ITMessageContainerUpdate(int windowId, CompoundTag nbt) implements CustomPacketPayload {
    public static final Type<ITMessageContainerUpdate> TYPE = new Type<>(ITLib.rl("container_update"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ITMessageContainerUpdate> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buf, ITMessageContainerUpdate message) -> message.write(buf),
            ITMessageContainerUpdate::new
    );

    public ITMessageContainerUpdate(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readNbt());
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(this.windowId);
        buf.writeNbt(this.nbt);
    }

    public static void handle(ITMessageContainerUpdate message, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            player.resetLastActionTime();
            if (player.containerMenu.containerId == message.windowId) {
                AbstractContainerMenu menu = player.containerMenu;
                if (menu instanceof ITContainerMenu itMenu) itMenu.receiveMessageFromScreen(message.nbt);
            }
        }
    }

    @Override
    public Type<ITMessageContainerUpdate> type() {
        return TYPE;
    }
}
