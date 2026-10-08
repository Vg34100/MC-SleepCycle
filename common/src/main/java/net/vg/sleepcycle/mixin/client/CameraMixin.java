package net.vg.sleepcycle.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.vg.sleepcycle.config.ModConfigs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Shadow
    protected abstract void move(float forwards, float up, float right);

    @Shadow
    private Entity entity;

    @Shadow
    protected abstract float getMaxZoom(float cameraDist);

    //? if >=26.1 {
    @Inject(method = "alignWithEntity", at = @At("TAIL"))
    private void sleepcycle$alignWithEntity(float partialTicks, CallbackInfo ci) {
    //? } else {
    /*@Inject(method = "setup", at = @At("TAIL"))
    private void sleepcycle$setup(BlockGetter level, Entity entity, boolean detached, boolean mirror, float partialTicks, CallbackInfo ci) {
    *///? }
        if (!ModConfigs.CHANGE_CAMERA_POS) {
            return;
        }

        if (this.entity instanceof LivingEntity livingEntity && livingEntity.isSleeping()) {
            Direction direction = livingEntity.getBedOrientation();

            this.setRotation(direction != null ? direction.toYRot() - 180.0F : 0.0F, 45.0F);

            /*
             * Vanilla 26.1.2 already moves the first-person sleeping camera by:
             * move(0.0F, 0.3F, 0.0F)
             *
             * This extra movement keeps your original intended raised sleeping camera angle.
             */


            this.move(-getMaxZoom(4.0f), 0.3F, 0.0F);
        }
    }
}