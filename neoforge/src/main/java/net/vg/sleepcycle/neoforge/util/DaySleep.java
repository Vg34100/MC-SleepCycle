package net.vg.sleepcycle.neoforge.util;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.vg.sleepcycle.config.ModConfigs;

public class DaySleep {
    @SubscribeEvent
    public void onCanSleep(CanPlayerSleepEvent event) {
        if (ModConfigs.ALLOW_DAY_SLEEPING) {
            event.setProblem(null);
        }
    }

    @SubscribeEvent
    public void onCanContinueSleeping(CanContinueSleepingEvent event) {
        if (ModConfigs.ALLOW_DAY_SLEEPING) {
            event.setContinueSleeping(true);
        }
    }
}
