package net.vg.sleepcycle.mixin.client;

import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import net.vg.sleepcycle.config.ModConfigs;
import net.vg.sleepcycle.network.WakeAtPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InBedChatScreen.class)
public abstract class InBedChatScreenMixin extends Screen {

    protected InBedChatScreenMixin(Component title) {
        super(title);
    }

    @Shadow private Button leaveBedButton;

    @Unique private static final long[] WAKE_TIMES = {0L, 6000L, 12000L, 13000L};
    @Unique private static final String[] WAKE_KEYS = {
        "sleepcycle.sleep_button.dawn",
        "sleepcycle.sleep_button.noon",
        "sleepcycle.sleep_button.dusk",
        "sleepcycle.sleep_button.night"
    };

    @Inject(method = "init", at = @At("TAIL"))
    private void sleepcycle$onInit(CallbackInfo info) {
        Minecraft mc = Minecraft.getInstance();
        int currentZone = mc.level != null ? sleepcycle$getZone(sleepcycle$getDayTime(mc)) : -1;

        // Move leave bed button up to make room for time buttons
        int defaultY = this.leaveBedButton.getY();
        this.leaveBedButton.setY(defaultY - ModConfigs.SLEEP_BUTTON_HEIGHT - 5);

        // 4 time buttons in a row where leave bed originally was
        int btnW = 60, btnH = 20, pad = 5;
        int totalW = 4 * btnW + 3 * pad;
        int startX = (this.width - totalW) / 2;

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            Component label = Component.translatable(WAKE_KEYS[i])
                .withStyle(i == currentZone ? ChatFormatting.YELLOW : ChatFormatting.WHITE);
            Button btn = Button.builder(label,
                b -> NetworkManager.sendToServer(new WakeAtPacket(WAKE_TIMES[idx]))
            ).bounds(startX + i * (btnW + pad), defaultY, btnW, btnH).build();
            this.addRenderableWidget(btn);
        }
    }

    @Unique
    private long sleepcycle$getDayTime(Minecraft mc) {
        try {
            Holder<WorldClock> clock = mc.level.registryAccess()
                .lookupOrThrow(Registries.WORLD_CLOCK).getOrThrow(WorldClocks.OVERWORLD);
            return mc.level.clockManager().getTotalTicks(clock) % 24000;
        } catch (Exception e) {
            return 0;
        }
    }

    @Unique
    private int sleepcycle$getZone(long dayTime) {
        if (dayTime < 6000) return 0;   // dawn
        if (dayTime < 12000) return 1;  // noon
        if (dayTime < 13000) return 2;  // dusk
        return 3;                        // night
    }
}
