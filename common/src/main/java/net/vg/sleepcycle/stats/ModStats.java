package net.vg.sleepcycle.stats;

import com.mojang.datafixers.kinds.Const;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.SleepCycle;


public class ModStats {



//    public static final Identifier TIME_SLEPT = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "time_slept"); // Counts the amount of time the user sleeps
//    public static final Identifier WELL_RESTED_SLEEPS = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "well_rested_sleeps"); // Counts the amount of times the user has gotten a well rested sleep
//    public static final Identifier TIRED_SLEEPS = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "tired_sleeps"); // Counts the amount of times the user has gotten a tired sleep
//    public static final Identifier HEALTH_REGAINED = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "health_regained"); // Counts the amount of health the user has regained while sleeping

    private static final DeferredRegister<Identifier> CUSTOM_STATS = DeferredRegister.create(Constants.MOD_ID, Registries.CUSTOM_STAT);
    public static final RegistrySupplier<Identifier> EXAMPLE_ITEM = CUSTOM_STATS.register("well_rested_sleeps", () -> Identifier.fromNamespaceAndPath(Constants.MOD_ID, "well_rested_sleeps"));


    public static final RegistrySupplier<Identifier> TIME_SLEPT = CUSTOM_STATS.register("time_slept",
            () -> makeCustomStat("time_slept", StatFormatter.TIME));
    public static final RegistrySupplier<Identifier> WELL_RESTED_SLEEPS = CUSTOM_STATS.register("well_rested_sleeps",
            () -> makeCustomStat("well_rested_sleeps", StatFormatter.DEFAULT));
    public static final RegistrySupplier<Identifier> TIRED_SLEEPS = CUSTOM_STATS.register("tired_sleeps",
            () -> makeCustomStat("tired_sleeps", StatFormatter.DEFAULT));
    public static final RegistrySupplier<Identifier> HEALTH_REGAINED = CUSTOM_STATS.register("health_regained",
            () -> makeCustomStat("health_regained", StatFormatter.DEFAULT));

    private static Identifier makeCustomStat(String name, StatFormatter formatter) {
        Identifier id = Identifier.fromNamespaceAndPath(Constants.MOD_ID, name);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
        Stats.CUSTOM.get(id, formatter);
        return id;
    }

    private static void registerCustomStat(Identifier id, StatFormatter statFormatter) {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
        Stats.CUSTOM.get(id, statFormatter);
    }

    public static void register() {
        CUSTOM_STATS.register();
    }
}
