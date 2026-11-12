//? if >=1.21 {
package net.vg.sleepcycle.advancement;


import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
//? if >=26.2 {
/*import net.minecraft.advancements.triggers.CriterionTrigger;
*///? } else {
import net.minecraft.advancements.CriterionTrigger;
//? }
import net.minecraft.core.registries.Registries;
import net.vg.sleepcycle.Constants;

public class ModCriteria {
    private static final DeferredRegister<CriterionTrigger<?>> CRITERIA = DeferredRegister.create(Constants.MOD_ID, Registries.TRIGGER_TYPE);

    public static final RegistrySupplier<SleepCriterion> SLEEP = CRITERIA.register("tutorialmod.sleep", SleepCriterion::new);

    public static void register() {
        CRITERIA.register();
    }

    // This method can be used to get the registered criterion if needed
    public static SleepCriterion getSleepCriterion() {
        return SLEEP.get();
    }
}
//? } else {
/*package net.vg.sleepcycle.advancement;

import net.vg.sleepcycle.mixin.LegacyCriteriaInvoker;

public final class ModCriteria {
    private static final SleepCriterion SLEEP = new SleepCriterion();
    public static void register() { LegacyCriteriaInvoker.sleepcycle$register(SLEEP); }
    public static SleepCriterion getSleepCriterion() { return SLEEP; }
}
*///? }
