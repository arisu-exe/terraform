package io.github.fallOut015.terraform.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

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
}