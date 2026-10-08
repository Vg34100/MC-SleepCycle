package net.vg.sleepcycle.client.gui.screen.option;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.vg.sleepcycle.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainOptionScreen extends OptionsSubScreen{
    private static final Component TITLE_TEXT = Component.translatable("config.general.title");
    private static final Component SERVER_SETTINGS_TEXT = Component.translatable("config.server.title");
    private static final Component CLIENT_SETTINGS_TEXT = Component.translatable("config.client.title");
    private static final Component EXPERIMENTAL_SETTINGS_TEXT = Component.translatable("config.experimental.title");

    public MainOptionScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, TITLE_TEXT);
        Constants.LOGGER.debug("Initializing MainConfigScreen with title: {}", TITLE_TEXT.getString());
    }

    @Override
    protected void addOptions() {
        Constants.LOGGER.debug("Adding options to MainConfigScreen");

        List<AbstractWidget> clickableWidgets = new ArrayList<>();

        Button serverSettingsButton = Button.builder(SERVER_SETTINGS_TEXT, button -> {
            Constants.LOGGER.debug("Server settings button clicked");
            //? if >=26.2 {
            /*this.minecraft.gui.setScreen(new ServerOptionScreen(this));
            *///? } else {
            this.minecraft.setScreen(new ServerOptionScreen(this));
            //? }
        }).width(150).build();
        clickableWidgets.add(serverSettingsButton);

        Button clientSettingsButton = Button.builder(CLIENT_SETTINGS_TEXT, button -> {
            Constants.LOGGER.debug("Client settings button clicked");
            //? if >=26.2 {
            /*this.minecraft.gui.setScreen(new ClientOptionScreen(this));
            *///? } else {
            this.minecraft.setScreen(new ClientOptionScreen(this));
            //? }
        }).width(150).build();
        clickableWidgets.add(clientSettingsButton);

        Button experimentalSettingsButton = Button.builder(EXPERIMENTAL_SETTINGS_TEXT, button -> {
            Constants.LOGGER.debug("Experimental settings button clicked");
            //? if >=26.2 {
            /*this.minecraft.gui.setScreen(new ExperimentalOptionScreen(this));
            *///? } else {
            this.minecraft.setScreen(new ExperimentalOptionScreen(this));
            //? }
        }).width(150).build();
        clickableWidgets.add(experimentalSettingsButton);

        assert this.list != null;
        this.list.addSmall(clickableWidgets);
        Constants.LOGGER.debug("Added {} clickable widgets", clickableWidgets.size());
    }

    private static Component getGenericValueText(Component prefix, Component value) {
        return Component.translatable("options.generic_value", prefix, value);
    }

}
