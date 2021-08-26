package io.github.fallOut015.terraform.tool;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public class ToolPlacer extends Tool {
    public ToolPlacer() {
        super(0, 0, "placer");
    }

    @Override
    public void onPress() {
        Minecraft.getInstance().level.setBlock(new BlockPos(Minecraft.getInstance().hitResult.getLocation()), Blocks.STONE.defaultBlockState(), 0);
    }
    @Override
    public void onRelease() {

    }
}