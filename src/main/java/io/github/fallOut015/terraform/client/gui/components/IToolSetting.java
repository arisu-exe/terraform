package io.github.fallOut015.terraform.client.gui.components;

import io.github.fallOut015.terraform.tool.Tool;
import net.minecraft.network.chat.Component;

public interface IToolSetting<T> {
    String getKey();

    void setSettingValue(Object value);
    T getSettingValue();

    Tool getTool();

    Component getTranslated();
}