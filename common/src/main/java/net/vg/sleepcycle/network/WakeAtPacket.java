package net.vg.sleepcycle.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.vg.sleepcycle.Constants;

public record WakeAtPacket(long targetDayTime) implements CustomPacketPayload {
    public static final Type<WakeAtPacket> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "wake_at")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, WakeAtPacket> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, WakeAtPacket::targetDayTime,
            WakeAtPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
