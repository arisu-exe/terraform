package io.github.fallOut015.terraform.client.gui.components;

import io.github.fallOut015.terraform.tool.Tool;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fmlclient.gui.widget.Slider;

public class SliderWidget extends Slider implements IToolSetting<Double> {
    final Tool tool;
    final String key;

    public SliderWidget(final Tool tool, final String key, int xPos, int yPos, int width, int height, double minVal, double maxVal, double currentVal) {
        super(xPos, yPos, width, height, Component.nullToEmpty(""), Component.nullToEmpty(""), minVal, maxVal, currentVal, false, false, handler);
        this.tool = tool;
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public void setSettingValue(Double value) {

    }
    @Override
    public Double getSettingValue() {

    }

    @Override
    public Tool getTool() {
        return this.tool;
    }
}