package net.vg.sleepcycle.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTriggerInstance;
//? if >=26.2 {
/*import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
*///? } elif >=26.1 {
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
//? } else {
/*import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
*///? }
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.vg.sleepcycle.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SleepCriterion extends SimpleCriterionTrigger<SleepCriterion.Conditions> {
    public static final Identifier ID = Identifier.parse("tutorialmod.sleep");
    public SleepCriterion() {
    }

    @Override
    public @NotNull Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }

    public static class Conditions implements SimpleCriterionTrigger.SimpleInstance, CriterionTriggerInstance {

        public static final Conditions INSTANCE = new Conditions();
        public static final Codec<Conditions> CODEC = MapCodec.unit(INSTANCE).codec();
        public Conditions() {
        }

        public Conditions(Conditions conditions) {

        }

        @Override
        public @NotNull Optional<ContextAwarePredicate> player() {
            return Optional.empty();
        }
    }
}
