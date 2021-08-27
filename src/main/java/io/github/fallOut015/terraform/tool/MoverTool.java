package io.github.fallOut015.terraform.tool;

import net.minecraftforge.client.event.RenderWorldLastEvent;

public class MoverTool extends Tool {
    public MoverTool() {
        super(100, 0, "mover");
    }

    @Override
    public void onPress() {

    }
    @Override
    protected void onUpdate() {

    }
    @Override
    public void onRelease() {

    }
    @Override
    public void renderOutline(final RenderWorldLastEvent event) {
    }
    @Override
    public boolean rendersOutline() {
        return false;
    }
}