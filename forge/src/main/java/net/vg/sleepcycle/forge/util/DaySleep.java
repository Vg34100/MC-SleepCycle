package net.vg.sleepcycle.forge.util;

import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.vg.sleepcycle.config.ModConfigs;

public final class DaySleep {
    @SubscribeEvent
    public void onSleepTime(SleepingTimeCheckEvent event) {
        if (ModConfigs.ALLOW_DAY_SLEEPING) event.setResult(Event.Result.ALLOW);
    }
}
