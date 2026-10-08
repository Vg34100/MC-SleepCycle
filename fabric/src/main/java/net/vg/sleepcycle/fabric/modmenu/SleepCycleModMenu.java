package net.vg.sleepcycle.fabric.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.client.gui.screen.option.MainOptionScreen;

public class SleepCycleModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        Constants.LOGGER.debug("Providing main configuration screen factory");
        return MainOptionScreen::new;
    }


}