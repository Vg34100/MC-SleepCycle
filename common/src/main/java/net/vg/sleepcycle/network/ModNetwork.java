package net.vg.sleepcycle.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.vg.sleepcycle.util.TimeProgressionHandler;

public class ModNetwork {
    private static boolean registered;

    public static void registerCommon() {
        if (registered) return;
        registered = true;
        //? if >=1.21 {
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
        //? } else {
        /*NetworkManager.registerReceiver(NetworkManager.c2s(), net.vg.sleepcycle.util.ModIds.id("wake_at"),
                (buffer, context) -> {
                    long target = buffer.readVarLong();
                    context.queue(() -> {
                        if (context.getPlayer() instanceof ServerPlayer player) {
                            TimeProgressionHandler.setWakeTarget(player, target);
                        }
                    });
                });
        *///? }

    }
    // Called only by the physical-client sleep screen.
    public static void sendWakeTarget(long target) {
        //? if >=1.21 {
        NetworkManager.sendToServer(new WakeAtPacket(target));
        //? } else {
        /*var buffer = new net.minecraft.network.FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        try {
            buffer.writeVarLong(target);
            NetworkManager.sendToServer(net.vg.sleepcycle.util.ModIds.id("wake_at"), buffer);
        } finally {
            buffer.release();
        }
        *///? }
    }
}
