package net.vg.sleepcycle.fabric.util;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.vg.sleepcycle.config.ModConfigs;

public class DaySleep {
    public static void init() {
        EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
            if (ModConfigs.ALLOW_DAY_SLEEPING) {
                return null;
            }
            return null;
        });
    }
}
