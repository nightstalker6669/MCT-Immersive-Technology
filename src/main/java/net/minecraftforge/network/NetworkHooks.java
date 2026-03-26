package net.minecraftforge.network;

import java.util.function.Consumer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public final class NetworkHooks {
    private NetworkHooks() {}

    public static void openScreen(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    public static void openScreen(ServerPlayer player, MenuProvider provider, Consumer<RegistryFriendlyByteBuf> extraDataWriter) {
        player.openMenu(provider, extraDataWriter);
    }
}
