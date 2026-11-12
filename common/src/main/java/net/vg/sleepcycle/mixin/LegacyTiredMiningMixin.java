//? if <1.21 {
/*package net.vg.sleepcycle.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.vg.sleepcycle.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 1.20.1 has no BLOCK_BREAK_SPEED attribute. Apply the same 30% penalty.
@Mixin(Player.class)
public abstract class LegacyTiredMiningMixin {
    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void sleepcycle$tiredMining(BlockState state, CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        var tired = player.getEffect(ModEffects.TIRED.get());
        if (tired != null) cir.setReturnValue(cir.getReturnValueF() * (1.0F - 0.30F * (tired.getAmplifier() + 1)));
    }
}
*///? }
