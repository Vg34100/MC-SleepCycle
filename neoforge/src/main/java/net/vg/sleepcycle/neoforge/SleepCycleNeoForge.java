package net.vg.sleepcycle.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.SleepCycle;
import net.vg.sleepcycle.neoforge.client.SleepCycleNeoForgeClient;
import net.vg.sleepcycle.neoforge.util.DaySleep;

@Mod(Constants.MOD_ID)
public final class SleepCycleNeoForge {
    public SleepCycleNeoForge(ModContainer container, Dist dist) {
        SleepCycle.init();
        NeoForge.EVENT_BUS.register(new DaySleep());
        if (dist == Dist.CLIENT) {
            SleepCycleNeoForgeClient.registerConfigScreen(container);
        }
    }
}
