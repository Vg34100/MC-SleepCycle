//? if <1.21 {
/*package net.vg.sleepcycle.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CriteriaTriggers.class)
public interface LegacyCriteriaInvoker {
    @Invoker("register")
    static <T extends CriterionTrigger<?>> T sleepcycle$register(T trigger) { throw new AssertionError(); }
}
*///? }
