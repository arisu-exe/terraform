package io.github.fallOut015.terraform.tool;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import io.github.fallOut015.terraform.client.gui.components.IEnumSetting;
import io.github.fallOut015.terraform.client.gui.components.IToolSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.List;
import java.util.Map;

public abstract class Tool {
    final int u, v;
    final TranslatableComponent translated;
    List<IToolSetting<?>> settings;

    public Tool(final int u, final int v, String name) {
        this.u = u;
        this.v = v;
        this.translated = new TranslatableComponent("gui." + name);
    }

    public abstract void onPress();
    public abstract void onRelease();

    public final int getU() {
        return this.u;
    }
    public final int getV() {
        return this.v;
    }
    public final Component getTranslated() {
        return this.translated;
    }
    public boolean isToolForOutlining() {
        return true;
    }
    public void onRenderUpdate(final RenderWorldLastEvent event) {
        this.calculatePointer();
        if(this.rendersOutline()) {
            this.renderOutline(event);
        }
        this.postUpdate();
    }
    public Object get(String key) {
        for(IToolSetting<?> widget : this.settings) {
            if(widget.getKey().equals(key)) {
                return widget.getSettingValue();
            }
        }
        return null;
    }
    public void set(String key, Object value) {
        for(IToolSetting<?> widget : this.settings) {
            if(widget.getKey().equals(key)) {
                widget.setSettingValue(value);
            }
        }
    }
    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }
    public void renderSettings(PoseStack poseStack, int mx, int my, float partialTicks) {
        for(IToolSetting<?> widget : this.settings) {
            if(widget instanceof AbstractWidget abstractWidget) {
                Gui.drawString(poseStack, Minecraft.getInstance().font, widget.getTranslated(), abstractWidget.x, abstractWidget.y, 0xFFFFFF);

                abstractWidget.render(poseStack, mx, my, partialTicks);
            }
        }
    }
    public void addSetting(IToolSetting<?> e) {
        this.settings.add(e);
    }

    protected abstract void calculatePointer();
    protected abstract void postUpdate();
    protected void renderOutline(final RenderWorldLastEvent event) {
    }
    protected boolean rendersOutline() {
        return true;
    }
}