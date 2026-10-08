package net.vg.sleepcycle.client.gui.screen.option;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.vg.sleepcycle.config.ModConfigs;

public class ServerOptionScreen extends OptionsSubScreen {

    public ServerOptionScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("config.server.title"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void addOptions() {
        // Boolean Options
        OptionInstance<Boolean> allowDaySleeping = OptionInstance.createBoolean(
                "sleep.allow.day.sleep",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.allow.day.sleep")),
                ModConfigs.ALLOW_DAY_SLEEPING,
                value -> ModConfigs.ALLOW_DAY_SLEEPING = value
        );

        OptionInstance<Boolean> grantBuffs = OptionInstance.createBoolean(
                "sleep.grant.buffs",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.grant.buffs")),
                ModConfigs.GRANT_BUFFS,
                value -> ModConfigs.GRANT_BUFFS = value
        );

        OptionInstance<Boolean> doRegen = OptionInstance.createBoolean(
                "sleep.do.regeneration",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.do.regeneration")),
                ModConfigs.DO_REGEN,
                value -> ModConfigs.DO_REGEN = value
        );

        OptionInstance<Boolean> changeTickSpeed = OptionInstance.createBoolean(
                "sleep.change.tick.speed",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.change.tick.speed")),
                ModConfigs.CHANGE_TICK_SPEED,
                value -> ModConfigs.CHANGE_TICK_SPEED = value
        );

        // Integer Options
        OptionInstance<Integer> wellRestedWait = new OptionInstance<>(
                "sleep.well.rested.wait",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.well.rested.wait")),
                (optionText, value) -> getGenericValueText(optionText, Component.literal(String.valueOf(value))),
                new OptionInstance.IntRange(0, 1000),
                ModConfigs.WELL_RESTED_WAIT,
                value -> ModConfigs.WELL_RESTED_WAIT = value
        );

        OptionInstance<Integer> tiredWait = new OptionInstance<>(
                "sleep.tired.wait",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.tired.wait")),
                (optionText, value) -> getGenericValueText(optionText, Component.literal(String.valueOf(value))),
                new OptionInstance.IntRange(0, 1000),
                ModConfigs.TIRED_WAIT,
                value -> ModConfigs.TIRED_WAIT = value
        );

        OptionInstance<Integer> wellRestedLength = new OptionInstance<>(
                "sleep.well.rested.length",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.well.rested.length")),
                (optionText, value) -> getGenericValueText(optionText, Component.literal(String.valueOf(value))),
                new OptionInstance.IntRange(0, 3000),
                ModConfigs.WELL_RESTED_LENGTH,
                value -> ModConfigs.WELL_RESTED_LENGTH = value
        );

        OptionInstance<Integer> tiredLength = new OptionInstance<>(
                "sleep.tired.length",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.tired.length")),
                (optionText, value) -> getGenericValueText(optionText, Component.literal(String.valueOf(value))),
                new OptionInstance.IntRange(0, 3000),
                ModConfigs.TIRED_LENGTH,
                value -> ModConfigs.TIRED_LENGTH = value
        );

        // Double Options
        OptionInstance<Integer> daySkipSpeed = new OptionInstance<>(
                "sleep.day.skip.speed",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.day.skip.speed")),
                (optionText, value) -> getGenericValueText(optionText, Component.literal(String.valueOf(value))),
                new OptionInstance.IntRange(0, 200),
                (int) ModConfigs.DAY_SKIP_SPEED,
                value -> ModConfigs.DAY_SKIP_SPEED = (double) value
        );

        OptionInstance<Double> sleepTickSpeed = new OptionInstance<>(
                "sleep.sleep.tick.multiplier",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.sleep.sleep.tick.multiplier")),
                (optionText, value) -> Component.translatable("options.generic_value",
                        optionText,
                        Component.translatable("options.multiplier", String.format("%.1f", value))
                ),
                (new OptionInstance.IntRange(0, 300)).xmap(
                        (sliderprogressvalue) ->(double)sliderprogressvalue / 100.0,
                        (value) -> (int)(value * 100)
                        //? if >=26.1 {
                        , true
                        //? }
                ),
                Codec.doubleRange(0.0, 3.0),
                ModConfigs.SLEEP_TICK_MULTIPLIER,
                value -> ModConfigs.SLEEP_TICK_MULTIPLIER = value
        );

        OptionInstance<?>[] options = new OptionInstance[]{
                allowDaySleeping,
                grantBuffs,
                doRegen,
                changeTickSpeed,
                wellRestedWait,
                tiredWait,
                wellRestedLength,
                tiredLength,
                daySkipSpeed,
                sleepTickSpeed
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
