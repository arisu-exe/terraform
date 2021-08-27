package io.github.fallOut015.terraform.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RenderHelper {
    public static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation("terraform", "textures/gui/widgets.png");

    public static void renderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, Entity mainCamera, double cameraX, double cameraY, double cameraZ, BlockPos blockPos, BlockState blockState, float r, float g, float b, float a) {
        renderShape(poseStack, vertexConsumer, Shapes.block(), (double)blockPos.getX() - cameraX, (double)blockPos.getY() - cameraY, (double)blockPos.getZ() - cameraZ, r, g, b, a);
    }
    public static void renderShape(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, float r, float g, float b, float a) {
        PoseStack.Pose posestack$pose = poseStack.last();
        voxelShape.forAllEdges((p_172987_, p_172988_, p_172989_, p_172990_, p_172991_, p_172992_) -> {
            float f = (float)(p_172990_ - p_172987_);
            float f1 = (float)(p_172991_ - p_172988_);
            float f2 = (float)(p_172992_ - p_172989_);
            float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
            f = f / f3;
            f1 = f1 / f3;
            f2 = f2 / f3;
            vertexConsumer.vertex(posestack$pose.pose(), (float)(p_172987_ + x), (float)(p_172988_ + y), (float)(p_172989_ + z)).color(r, g, b, a).normal(posestack$pose.normal(), f, f1, f2).endVertex();
            vertexConsumer.vertex(posestack$pose.pose(), (float)(p_172990_ + x), (float)(p_172991_ + y), (float)(p_172992_ + z)).color(r, g, b, a).normal(posestack$pose.normal(), f, f1, f2).endVertex();
        });
    }
}