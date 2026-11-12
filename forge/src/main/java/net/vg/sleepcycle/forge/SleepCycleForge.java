package net.vg.sleepcycle.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.SleepCycle;
import net.vg.sleepcycle.forge.util.DaySleep;

@Mod(Constants.MOD_ID)
public final class SleepCycleForge {
    public SleepCycleForge() {
        EventBuses.registerModEventBus(Constants.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SleepCycle.init();
        MinecraftForge.EVENT_BUS.register(new DaySleep());
    }
}
