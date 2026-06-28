package net.vg.sleepcycle.fabric.util;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.vg.sleepcycle.config.ModConfigs;

public class DaySleep {
    public static void init() {
        // For vanilla beds on Fabric: allow sleep to start during the day when configured.
        // The PlayerTickMixin handles the ongoing day/night check that would otherwise wake
        // the player each tick. This event covers the initial startSleepInBed call.
        EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
            if (ModConfigs.ALLOW_DAY_SLEEPING) {
                return null; // null = allow sleeping
            }
            return null; // null = no opinion, let vanilla decide
        });
    }
}
