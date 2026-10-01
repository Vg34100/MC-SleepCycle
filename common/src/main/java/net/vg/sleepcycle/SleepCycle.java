package net.vg.sleepcycle;

import net.vg.sleepcycle.advancement.ModCriteria;
import net.vg.sleepcycle.block.entity.ModBlockEntities;
import net.vg.sleepcycle.config.ModConfigs;
import net.vg.sleepcycle.effect.ModEffects;
import net.vg.sleepcycle.item.ModItems;
import net.vg.sleepcycle.sounds.ModSounds;
import net.vg.sleepcycle.stats.ModStats;
import net.vg.sleepcycle.network.ModNetwork;
import net.vg.sleepcycle.util.TimeProgressionHandler;

public final class SleepCycle {
    public static void init() {
        // Write common init code here.
        ModConfigs.registerConfigs();

        // stats
        ModStats.register();
        // effects
        ModEffects.register();
        // sounds
        ModSounds.register();
        // criteria
        ModCriteria.register();

        ModItems.register();
        ModBlockEntities.register();





        // handler
        TimeProgressionHandler.register();

        // network
        ModNetwork.registerCommon();
    }
}
