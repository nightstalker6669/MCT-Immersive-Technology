package mctmods.immersivetechnology.core.network;

import mctmods.immersivetechnology.core.util.TranslationKey;
import net.minecraft.client.Minecraft;
import mctmods.immersivetechnology.core.lib.ITLib;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ITOSDSyncBlock(String key, int distance) implements CustomPacketPayload {
    public static final Type<ITOSDSyncBlock> TYPE = new Type<>(ITLib.rl("osd_sync_block"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ITOSDSyncBlock> STREAM_CODEC = StreamCodec.of(ITOSDSyncBlock::write, ITOSDSyncBlock::new);

    public ITOSDSyncBlock(RegistryFriendlyByteBuf buf) { this(buf.readUtf(), buf.readInt()); }

    private void write(RegistryFriendlyByteBuf buf) { buf.writeUtf(key); buf.writeInt(distance); }

    public static void handle(ITOSDSyncBlock message, IPayloadContext context) {
        TranslationKey transKey = TranslationKey.valueOf(message.key);
        String actualKey = transKey.getLocation();
        Component msg = message.distance >= 0 ? Component.translatable(actualKey, message.distance) : Component.translatable(actualKey);
        Minecraft.getInstance().gui.getChat().addMessage(msg);
    }

    @Override
    public Type<ITOSDSyncBlock> type() {
        return TYPE;
    }
}
