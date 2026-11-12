package net.vg.sleepcycle.effect;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.vg.sleepcycle.Constants;
import org.jetbrains.annotations.NotNull;

public class WellRestedEffect extends MobEffect {

    protected WellRestedEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);


        Identifier absorptionModifierId = net.vg.sleepcycle.util.ModIds.id("well_rested_absorption");

        //? if >=1.21 {
        this.addAttributeModifier(Attributes.MAX_ABSORPTION, absorptionModifierId, 4.0, AttributeModifier.Operation.ADD_VALUE);
        //? }

    }


    @Override
    //? if >=1.21 {
    public boolean shouldApplyEffectTickThisTick(int i, int j) {
    //? } else {
    /*public boolean isDurationEffectTick(int i, int j) {
    *///? }
        int k = 50 >> j;
        if (k > 0) {
            return i % k == 0;
        } else {
            return true;
        }
    }

    @Override
    //? if >=26.1 {
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity livingEntity, int amplifier) {
    //? } elif >=1.21 {
    /*public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
    *///? } else {
    /*public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
    *///? }
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            livingEntity.heal(1.0F);
        }

        //? if >=1.21 {
        return true;
        //? }
    }

    //? if >=1.21 {
    @Override
    public void onEffectStarted(LivingEntity livingEntity, int i) {
        super.onEffectStarted(livingEntity, i);
        livingEntity.setAbsorptionAmount(Math.max(livingEntity.getAbsorptionAmount(), (float)(4 * (1 + i))));
    }
    //? } else {
    /*@Override
    public void addAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap attributes, int amplifier) {
        super.addAttributeModifiers(entity, attributes, amplifier);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), 4.0F * (1 + amplifier)));
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        entity.setAbsorptionAmount(Math.max(0.0F, entity.getAbsorptionAmount() - 4.0F * (1 + amplifier)));
    }
    *///? }
}