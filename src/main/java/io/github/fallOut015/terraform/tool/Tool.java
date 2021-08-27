package io.github.fallOut015.terraform.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.Map;

public abstract class Tool {
    final int u, v;
    final TranslatableComponent translated;
    final ToolSettings settings;

    public Tool(final int u, final int v, String name, Map<?, ?> settings) {
        this.u = u;
        this.v = v;
        this.translated = new TranslatableComponent("gui." + name);
        this.settings = new ToolSettings(settings);
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
        if(this.rendersOutline()) {
            this.renderOutline(event);
        }
        this.onUpdate();
    }
    public <T> T get(String key) {
        return this.settings.get(key);
    }
    public void set(String key, Object value) {
        this.settings.set(key, value);
    }

    protected abstract void onUpdate();
    protected void renderOutline(final RenderWorldLastEvent event) {
    }
    protected boolean rendersOutline() {
        return true;
    }
}