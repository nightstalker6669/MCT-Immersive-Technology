package mctmods.immersivetechnology.core.network;

import mctmods.immersivetechnology.core.lib.ITLib;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = ITLib.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ITPacketHandler {
    public static final String NET_VERSION = "1";

    private ITPacketHandler() {}

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(NET_VERSION);
        registrar.playToServer(ITMessageContainerUpdate.TYPE, ITMessageContainerUpdate.STREAM_CODEC, ITMessageContainerUpdate::handle);
        registrar.playToClient(ITMessageContainerData.TYPE, ITMessageContainerData.STREAM_CODEC, ITMessageContainerData::handle);
        registrar.playToServer(ITOSDRequestMessage.TYPE, ITOSDRequestMessage.STREAM_CODEC, ITOSDRequestMessage::handle);
        registrar.playToClient(ITOSDSyncMessage.TYPE, ITOSDSyncMessage.STREAM_CODEC, ITOSDSyncMessage::handle);
        registrar.playToClient(ITOSDSyncBlock.TYPE, ITOSDSyncBlock.STREAM_CODEC, ITOSDSyncBlock::handle);
        registrar.playToServer(ITMessageTileSync.TYPE, ITMessageTileSync.STREAM_CODEC, ITMessageTileSync::handle);
    }

    public static <T extends CustomPacketPayload> void sendToPlayer(Player player, T message) {
        if (message == null) return;
        if (player instanceof ServerPlayer serverPlayer) PacketDistributor.sendToPlayer(serverPlayer, message);
    }

    public static <T extends CustomPacketPayload> void sendToServer(T message) {
        if (message == null) return;
        PacketDistributor.sendToServer(message);
    }

    public static <T extends CustomPacketPayload> void sendToDimension(ResourceKey<Level> dim, T message) {
        if (message == null) return;
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().getLevel(dim);
        if (level != null) PacketDistributor.sendToPlayersInDimension(level, message);
    }

    public static <T extends CustomPacketPayload> void sendAll(T message) {
        if (message == null) return;
        PacketDistributor.sendToAllPlayers(message);
    }
}
