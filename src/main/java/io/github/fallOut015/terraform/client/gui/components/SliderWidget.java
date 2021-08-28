package io.github.fallOut015.terraform.client.gui.components;

import io.github.fallOut015.terraform.tool.Tool;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fmlclient.gui.widget.Slider;

public class SliderWidget extends Slider implements IToolSetting<Double> {
    final Tool tool;
    final String key;
    final Component translated;

    public SliderWidget(final Tool tool, final String key, int xPos, int yPos, int width, int height, double minVal, double maxVal, double currentVal) {
        super(xPos, yPos, width, height, Component.nullToEmpty(""), Component.nullToEmpty(""), minVal, maxVal, currentVal, false, false, button -> {});

        this.tool = tool;
        this.key = key;
        this.translated = new TranslatableComponent("gui." + key);
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public void setSettingValue(Object value) {
        this.sliderValue = (Double) value;
    }
    @Override
    public Double getSettingValue() {
        return this.sliderValue;
    }

    @Override
    public Tool getTool() {
        return this.tool;
    }

    @Override
    public Component getTranslated() {
        return this.translated;
    }
}