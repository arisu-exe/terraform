package io.github.fallOut015.terraform.client.gui.components;

import io.github.fallOut015.terraform.tool.Tool;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

public class CheckboxWidget extends Checkbox implements IToolSetting<Boolean> {
    final Tool tool;
    final String key;

    public CheckboxWidget(final Tool tool, final String key, int x, int y, int width, int height, boolean currentValue) {
        super(x, y, width, height, Component.nullToEmpty(""), currentValue, false);
        this.tool = tool;
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public void setSettingValue(Boolean value) {
        if(value != this.getSettingValue()) {
            this.onPress();
        }
    }
    @Override
    public Boolean getSettingValue() {
        return this.selected();
    }

    @Override
    public Tool getTool() {
        return this.tool;
    }
}