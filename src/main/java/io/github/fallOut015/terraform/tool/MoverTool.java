package io.github.fallOut015.terraform.tool;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class MoverTool extends Tool {
    boolean active;
    double oldx, oldy;

    public MoverTool() {
        super(100, 0, "mover", Map.of());
    }

    @Override
    public void onPress() {
        this.active = true;
        this.oldx = Minecraft.getInstance().mouseHandler.xpos();
        this.oldy = Minecraft.getInstance().mouseHandler.ypos();
    }
    @Override
    protected void calculatePointer() {
        if(this.active) {
            double x = Minecraft.getInstance().mouseHandler.xpos();
            double y = Minecraft.getInstance().mouseHandler.ypos();

            Vec3 rightVec = new Vec3(Minecraft.getInstance().gameRenderer.getMainCamera().getLeftVector()).scale(0.25d * (x - this.oldx));
            Vec3 upVec = new Vec3(Minecraft.getInstance().gameRenderer.getMainCamera().getUpVector()).scale(0.25d * (y - this.oldy));
            Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().move(MoverType.SELF, rightVec.add(upVec));

            this.oldx = x;
            this.oldy = y;
        }
    }
    @Override
    protected void postUpdate() {

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