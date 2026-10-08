package net.vg.sleepcycle.neoforge.client;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.vg.sleepcycle.client.gui.screen.option.MainOptionScreen;

public final class SleepCycleNeoForgeClient {
    private SleepCycleNeoForgeClient() {}

    public static void registerConfigScreen(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (mod, parent) -> new MainOptionScreen(parent));
    }
}
