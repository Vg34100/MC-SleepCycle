package net.vg.sleepcycle.neoforge;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.SleepCycle;
import net.vg.sleepcycle.client.gui.screen.option.MainOptionScreen;
import net.vg.sleepcycle.neoforge.util.DaySleep;

@Mod(Constants.MOD_ID)
public final class SleepCycleNeoForge {
    public SleepCycleNeoForge() {
        // Run our common setup.
        SleepCycle.init();
        NeoForge.EVENT_BUS.register(new DaySleep());

        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> new IConfigScreenFactory() {
            @Override
            public Screen createScreen(ModContainer arg, Screen arg2) {
                return new MainOptionScreen(arg2);
            }

        });
    }

    private void onSleepingTimeCheck(CanContinueSleepingEvent event) {
        // Allow sleeping at any time
        event.setContinueSleeping(true);
    }
}
