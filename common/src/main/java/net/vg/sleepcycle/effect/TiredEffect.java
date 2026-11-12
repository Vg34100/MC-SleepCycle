package net.vg.sleepcycle.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.Identifier;
import net.vg.sleepcycle.Constants;

public class TiredEffect extends MobEffect {

    protected TiredEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);

        Identifier movementSpeedModifierId = net.vg.sleepcycle.util.ModIds.id("tired_movement_speed");
        Identifier attackSpeedModifierId = net.vg.sleepcycle.util.ModIds.id("tired_attack_speed");
        Identifier blockBreakSpeedModifierId = net.vg.sleepcycle.util.ModIds.id("tired_block_break_speed");


        //? if >=1.21 {
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, movementSpeedModifierId, -0.30, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, attackSpeedModifierId, -0.30, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, blockBreakSpeedModifierId, -0.30, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        //? } else {
        /*this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "45005e33-cf1e-3cf0-b1ad-432cfb6e037e", -0.30, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, "7229bdde-187b-39d1-833b-f947531da9ae", -0.30, AttributeModifier.Operation.MULTIPLY_TOTAL);
        *///? }
    }

    @Override
    //? if >=1.21 {
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    //? } else {
    /*public boolean isDurationEffectTick(int duration, int amplifier) {
    *///? }
        return true;
    }
}