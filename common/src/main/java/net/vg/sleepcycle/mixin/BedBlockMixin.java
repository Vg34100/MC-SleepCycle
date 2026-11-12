package net.vg.sleepcycle.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.vg.sleepcycle.util.TimeProgressionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >=26.3 {
/*@Mixin(net.minecraft.world.level.block.AbstractBedBlock.class)
*///? } else {
@Mixin(BedBlock.class)
//? }
public class BedBlockMixin {
    //? if >=1.21 {
    @Inject(method = "useWithoutItem", at = @At("RETURN"))
    private void onUse(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
    //? } else {
    /*@Inject(method = "use", at = @At("RETURN"))
    private void onUse(BlockState blockState, Level level, BlockPos blockPos, Player player, net.minecraft.world.InteractionHand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
    *///? }
        //? if >=1.21 {
        if (!level.isClientSide() && player instanceof ServerPlayer && player.isSleeping()) {
        //? } else {
        /*if (!level.isClientSide && player instanceof ServerPlayer && player.isSleeping()) {
        *///? }
            TimeProgressionHandler.addWorld((ServerLevel) level);
        }
    }
}
