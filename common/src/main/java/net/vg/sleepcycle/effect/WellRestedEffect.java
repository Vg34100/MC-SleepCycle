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


        Identifier absorptionModifierId = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "well_rested_absorption");

        this.addAttributeModifier(Attributes.MAX_ABSORPTION, absorptionModifierId, 4.0, AttributeModifier.Operation.ADD_VALUE);

    }


    @Override
    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        int k = 50 >> j;
        if (k > 0) {
            return i % k == 0;
        } else {
            return true;
        }
    }

    @Override
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            livingEntity.heal(1.0F);
        }

        return true;
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int i) {
        super.onEffectStarted(livingEntity, i);
        livingEntity.setAbsorptionAmount(Math.max(livingEntity.getAbsorptionAmount(), (float)(4 * (1 + i))));
    }
}