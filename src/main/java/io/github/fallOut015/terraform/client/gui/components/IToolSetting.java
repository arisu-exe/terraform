package io.github.fallOut015.terraform.client.gui.components;

import io.github.fallOut015.terraform.tool.Tool;

public interface IToolSetting<T> {
    String getKey();

    void setSettingValue(T value);
    T getSettingValue();

    Tool getTool();
}