package net.vg.sleepcycle.client.gui.screen.option;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.vg.sleepcycle.config.ModConfigs;

public class ClientOptionScreen extends OptionsSubScreen {

    public ClientOptionScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("config.client.title"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void addOptions() {
        // Define client-specific options here
        OptionInstance<Boolean> changeCameraPos = OptionInstance.createBoolean(
                "sleep.change.camera.pos",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.change.camera.pos")),
                ModConfigs.CHANGE_CAMERA_POS,
                value -> ModConfigs.CHANGE_CAMERA_POS = value
        );

        OptionInstance<Integer> sleepButtonHeight = new OptionInstance<>(
                "sleep.sleep.button.height",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.sleep.button.height")),
                (optionText, value) -> getGenericValueText(optionText, Component.literal(String.valueOf(value))),
                new OptionInstance.IntRange(0, 50),
                (int) ModConfigs.SLEEP_BUTTON_HEIGHT,
                value -> ModConfigs.SLEEP_BUTTON_HEIGHT = value
        );

        OptionInstance<?>[] options = new OptionInstance[]{
                changeCameraPos,
                sleepButtonHeight
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
