package net.vg.sleepcycle.client.gui.screen.option;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.vg.sleepcycle.config.ModConfigs;

public class ExperimentalOptionScreen extends OptionsSubScreen {

    public ExperimentalOptionScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("config.experimental.title"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void addOptions() {
        // Define client-specific options here
        OptionInstance<Integer> ticksPerTick = new OptionInstance<>(
                "sleep.tick.per.tick",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.tick.per.tick")),
                (optionText, value) -> getGenericValueText(optionText, Component.literal(String.valueOf(value))),
                new OptionInstance.IntRange(1, 10),
                (int) ModConfigs.TICKS_PER_TICK,
                value -> ModConfigs.TICKS_PER_TICK = value
        );

        OptionInstance<?>[] options = new OptionInstance[]{
                ticksPerTick
        };

        this.list.addSmall(options);
    }

    @Override
    public void onClose() {
        ModConfigs.saveConfigs();
        //? if >=26.2 {
        /*this.minecraft.gui.setScreen(this.lastScreen);
        *///? } else {
        this.minecraft.setScreen(this.lastScreen);
        //? }
    }

    private static Component getGenericValueText(Component prefix, Component value) {
        return Component.translatable("options.generic_value", prefix, value);
    }
}
