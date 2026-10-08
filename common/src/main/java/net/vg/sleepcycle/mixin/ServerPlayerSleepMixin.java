//? if >=26.1 {
package net.vg.sleepcycle.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.level.Level;
import net.vg.sleepcycle.config.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerSleepMixin {
    // startSleepInBed checks canSleep to block sleeping during the day.
    // When day sleeping is enabled, override to always return true.
    @Redirect(
        method = "startSleepInBed",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/BedRule;canSleep(Lnet/minecraft/world/level/Level;)Z"),
        require = 0
    )
    private boolean sleepcycle$allowDaySleepStart(BedRule bedRule, Level level) {
        if (ModConfigs.ALLOW_DAY_SLEEPING) {
            return true;
        }
        return bedRule.canSleep(level);
    }
}
//? }
