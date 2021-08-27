package io.github.fallOut015.terraform.tool;

import net.minecraft.client.Minecraft;

import java.util.Map;

public class RotatorTool extends Tool {
    boolean active;
    double oldx, oldy;

    public RotatorTool() {
        super(120, 0, "rotator", Map.of());
    }

    @Override
    public void onPress() {
        this.active = true;
        this.oldx = Minecraft.getInstance().mouseHandler.xpos();
        this.oldy = Minecraft.getInstance().mouseHandler.ypos();
    }
    @Override
    protected void onUpdate() {
        if(this.active) {
            double x = Minecraft.getInstance().mouseHandler.xpos();
            double y = Minecraft.getInstance().mouseHandler.ypos();

            Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().turn(x - this.oldx, y - this.oldy);

            this.oldx = x;
            this.oldy = y;
        }
    }
    @Override
    public void onRelease() {
        this.active = false;
    }

    @Override
    protected boolean rendersOutline() {
        return false;
    }
}