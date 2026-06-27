package net.vg.sleepcycle.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.vg.sleepcycle.effect.ModEffects;
import net.vg.sleepcycle.util.TimeProgressionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {
    @Inject(method = "useWithoutItem", at = @At("RETURN"))
    private void onUse(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        // Check if the original method returned SUCCESS
        if (cir.getReturnValue() == InteractionResult.SUCCESS && !level.isClientSide()) {
            // Ensure the player is sleeping and add custom logic
            if (player instanceof ServerPlayer) {

                // Register the world for time progression
                TimeProgressionHandler.addWorld((ServerLevel) level);
            }
        }
    }
}
