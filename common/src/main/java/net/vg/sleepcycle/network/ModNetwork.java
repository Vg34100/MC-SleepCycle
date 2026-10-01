package net.vg.sleepcycle.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.vg.sleepcycle.util.TimeProgressionHandler;

public class ModNetwork {
    public static void registerCommon() {
        NetworkManager.registerReceiver(
            NetworkManager.c2s(),
            WakeAtPacket.TYPE,
            WakeAtPacket.CODEC,
            (packet, context) -> context.queue(() -> {
                if (context.getPlayer() instanceof ServerPlayer sp) {
                    TimeProgressionHandler.setWakeTarget(sp, packet.targetDayTime());
                }
            })
        );
    }
}
