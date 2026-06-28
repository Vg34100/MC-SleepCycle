package net.vg.sleepcycle.stats;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.vg.sleepcycle.Constants;

public class ModStats {
    private static final DeferredRegister<Identifier> CUSTOM_STATS =
            DeferredRegister.create(Constants.MOD_ID, Registries.CUSTOM_STAT);

    public static final RegistrySupplier<Identifier> TIME_SLEPT =
            CUSTOM_STATS.register("time_slept",
                    () -> Identifier.fromNamespaceAndPath(Constants.MOD_ID, "time_slept"));

    public static final RegistrySupplier<Identifier> WELL_RESTED_SLEEPS =
            CUSTOM_STATS.register("well_rested_sleeps",
                    () -> Identifier.fromNamespaceAndPath(Constants.MOD_ID, "well_rested_sleeps"));

    public static final RegistrySupplier<Identifier> TIRED_SLEEPS =
            CUSTOM_STATS.register("tired_sleeps",
                    () -> Identifier.fromNamespaceAndPath(Constants.MOD_ID, "tired_sleeps"));

    public static final RegistrySupplier<Identifier> HEALTH_REGAINED =
            CUSTOM_STATS.register("health_regained",
                    () -> Identifier.fromNamespaceAndPath(Constants.MOD_ID, "health_regained"));

    public static void register() {
        CUSTOM_STATS.register();

        Stats.CUSTOM.get(TIME_SLEPT.get(), StatFormatter.TIME);
        Stats.CUSTOM.get(WELL_RESTED_SLEEPS.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(TIRED_SLEEPS.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(HEALTH_REGAINED.get(), StatFormatter.DEFAULT);
    }
}