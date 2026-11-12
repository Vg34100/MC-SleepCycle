//? if >=1.21 {
package net.vg.sleepcycle.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTriggerInstance;
//? if >=26.2 {
/*import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
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
//? if >=26.3 {
/*import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
*///? } elif >=26.2 {
/*import net.minecraft.advancements.predicates.ContextAwarePredicate;
*///? }

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
        //? if >=26.3 {
        /*public @NotNull Optional<Holder<LootItemCondition>> player() {
        *///? } else {
        public @NotNull Optional<ContextAwarePredicate> player() {
        //? }
            return Optional.empty();
        }
    }
}

//? } else {
/*package net.vg.sleepcycle.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.vg.sleepcycle.util.ModIds;

public final class SleepCriterion extends SimpleCriterionTrigger<SleepCriterion.Conditions> {
    public static final Identifier ID = ModIds.id("tutorialmod.sleep");
    @Override public Identifier getId() { return ID; }
    @Override protected Conditions createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext context) {
        return new Conditions(player);
    }
    public void trigger(ServerPlayer player) { trigger(player, instance -> true); }
    public static final class Conditions extends AbstractCriterionTriggerInstance {
        public Conditions(ContextAwarePredicate player) { super(ID, player); }
    }
}
*///? }
