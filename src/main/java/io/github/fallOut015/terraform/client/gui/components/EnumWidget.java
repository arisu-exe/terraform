package io.github.fallOut015.terraform.client.gui.components;

import io.github.fallOut015.terraform.tool.Tool;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class EnumWidget<T extends IEnumSetting> extends AbstractButton implements IToolSetting<T> {
    final Tool tool;
    final String key;
    final Component translated;

    T enumValue;

    public EnumWidget(final Tool tool, final String key, int x, int y, int w, int h, T enumValue) {
        super(x, y, w, h, Component.nullToEmpty(""));

        this.tool = tool;
        this.key = key;
        this.translated = new TranslatableComponent("gui." + key);

        this.enumValue = enumValue;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setSettingValue(Object value) {
        this.enumValue = (T) value;
    }
    @Override
    public T getSettingValue() {
        return this.enumValue;
    }

    @Override
    public Tool getTool() {
        return null;
    }

    @Override
    public Component getTranslated() {
        return this.translated;
    }

    @Override
    public void onPress() {

    }
    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {

    }
}