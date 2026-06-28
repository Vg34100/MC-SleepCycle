package net.vg.sleepcycle.mixin;

import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vg.sleepcycle.config.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerTickMixin {
    // Player.tick() wakes sleeping players when BedRule.canSleep returns false (daytime).
    // When day sleeping is enabled, always return true so the player stays asleep.
    @Redirect(
        method = "tick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/BedRule;canSleep(Lnet/minecraft/world/level/Level;)Z"),
        require = 0
    )
    private boolean sleepcycle$allowDaySleeping(BedRule bedRule, Level level) {
        if (ModConfigs.ALLOW_DAY_SLEEPING) {
            return true;
        }
        return bedRule.canSleep(level);
    }
}
