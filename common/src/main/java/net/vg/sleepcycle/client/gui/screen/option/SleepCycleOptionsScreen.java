package net.vg.sleepcycle.client.gui.screen.option;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
//? if >=1.21 {
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
//? } else {
/*import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
*///? }
import net.minecraft.network.chat.Component;

public abstract class SleepCycleOptionsScreen extends OptionsSubScreen {
    protected SleepCycleOptionsScreen(Screen parent, Component title) {
        super(parent, Minecraft.getInstance().options, title);
    }
    //? if <1.21 {
    /*protected OptionsList list;
    protected abstract void addOptions();
    @Override protected void init() {
        list = new OptionsList(minecraft, width, height, 32, height - 32, 25);
        addOptions();
        addWidget(list);
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose())
                .bounds(width / 2 - 100, height - 27, 200, 20).build());
    }
    @Override public void render(GuiGraphics graphics, int x, int y, float delta) {
        basicListRender(graphics, list, x, y, delta);
    }
    *///? }
}
