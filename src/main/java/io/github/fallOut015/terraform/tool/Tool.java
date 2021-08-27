package io.github.fallOut015.terraform.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public abstract class Tool {
    final int u, v;
    final TranslatableComponent translated;
    // Settings

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
        if(this.rendersOutline()) {
            this.renderOutline(event);
        }
        this.onUpdate();
    }

    protected abstract void onUpdate();
    protected void renderOutline(final RenderWorldLastEvent event) {
    }
    protected boolean rendersOutline() {
        return true;
    }
}